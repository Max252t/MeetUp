package com.topit.meetup.feature.chat.data.repository

import com.topit.meetup.core.common.DomainResult
import com.topit.meetup.core.common.TokenStorage
import com.topit.meetup.core.network.safeApiCall
import com.topit.meetup.feature.chat.data.remote.ChatApi
import com.topit.meetup.feature.chat.data.socket.ChatSocketManager
import com.topit.meetup.feature.chat.domain.model.Message
import com.topit.meetup.feature.chat.domain.model.MessageStatus
import com.topit.meetup.feature.chat.domain.repository.MessageRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Instant
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MessageRepositoryImpl @Inject constructor(
    private val chatApi: ChatApi,
    private val socketManager: ChatSocketManager,
    private val tokenStorage: TokenStorage,
) : MessageRepository {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private val store = ConcurrentHashMap<String, MutableStateFlow<List<Message>>>()

    override fun getMessages(chatId: String): Flow<List<Message>> {
        return store.getOrPut(chatId) {
            MutableStateFlow<List<Message>>(emptyList()).also { flow ->
                scope.launch { loadMessages(chatId, flow) }
            }
        }
    }

    private suspend fun loadMessages(chatId: String, flow: MutableStateFlow<List<Message>>) {
        safeApiCall { chatApi.getMessages(chatId) }
            .onSuccess { page ->
                val messages = page.items.map { dto ->
                    val timestampMs = runCatching {
                        Instant.parse(dto.createdAt).toEpochMilli()
                    }.getOrDefault(System.currentTimeMillis())

                    Message(
                        id = dto.id,
                        chatId = dto.conversationId,
                        senderId = dto.senderId,
                        text = dto.content,
                        timestamp = timestampMs,
                        status = MessageStatus.SENT,
                    )
                }.reversed() // API returns newest-first; reverse to oldest-first
                flow.value = messages
            }
    }

    override suspend fun sendMessage(chatId: String, text: String): DomainResult<Message> {
        val message = Message(
            id = UUID.randomUUID().toString(),
            chatId = chatId,
            senderId = tokenStorage.userId ?: "",
            text = text,
            timestamp = System.currentTimeMillis(),
            status = MessageStatus.SENDING,
        )
        store.getOrPut(chatId) { MutableStateFlow(emptyList()) }
            .update { it + message }

        socketManager.sendMessage(chatId, text)

        return DomainResult.Success(message)
    }
}
