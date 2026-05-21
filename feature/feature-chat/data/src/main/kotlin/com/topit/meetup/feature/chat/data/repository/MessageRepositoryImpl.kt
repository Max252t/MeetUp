package com.topit.meetup.feature.chat.data.repository

import com.topit.meetup.core.common.DomainResult
import com.topit.meetup.feature.chat.domain.model.Message
import com.topit.meetup.feature.chat.domain.model.MessageStatus
import com.topit.meetup.feature.chat.domain.repository.MessageRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MessageRepositoryImpl @Inject constructor() : MessageRepository {

    private val store = ConcurrentHashMap<String, MutableStateFlow<List<Message>>>(
        buildMap {
            put("chat-1", MutableStateFlow(FAKE_MESSAGES_CHAT_1))
            put("chat-2", MutableStateFlow(FAKE_MESSAGES_CHAT_2))
            put("chat-3", MutableStateFlow(FAKE_MESSAGES_CHAT_3))
        }
    )

    override fun getMessages(chatId: String): Flow<List<Message>> =
        store.getOrPut(chatId) { MutableStateFlow(emptyList()) }

    override suspend fun sendMessage(chatId: String, text: String): DomainResult<Message> {
        val message = Message(
            id = UUID.randomUUID().toString(),
            chatId = chatId,
            senderId = "fake-user-id",
            text = text,
            timestamp = System.currentTimeMillis(),
            status = MessageStatus.SENT,
        )
        store.getOrPut(chatId) { MutableStateFlow(emptyList()) }
            .update { it + message }
        return DomainResult.Success(message)
    }

    companion object {
        private val now = System.currentTimeMillis()

        private val FAKE_MESSAGES_CHAT_1 = listOf(
            Message("m1", "chat-1", "user-1", "Привет!", now - 3_700_000L, MessageStatus.SENT),
            Message("m2", "chat-1", "fake-user-id", "Привет! Как дела?", now - 3_600_000L, MessageStatus.SENT),
            Message("m3", "chat-1", "user-1", "Хорошо, спасибо!", now - 3_500_000L, MessageStatus.SENT),
        )
        private val FAKE_MESSAGES_CHAT_2 = listOf(
            Message("m4", "chat-2", "user-2", "Как дела?", now - 7_200_000L, MessageStatus.SENT),
        )
        private val FAKE_MESSAGES_CHAT_3 = listOf(
            Message("m5", "chat-3", "user-3", "Увидимся в выходные?", now - 86_400_000L, MessageStatus.SENT),
        )
    }
}
