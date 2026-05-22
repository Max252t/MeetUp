package com.topit.meetup.feature.chats.data.repository

import com.topit.meetup.core.common.DomainResult
import com.topit.meetup.core.common.TokenStorage
import com.topit.meetup.core.network.safeApiCall
import com.topit.meetup.feature.chats.data.remote.ChatsApi
import com.topit.meetup.feature.chats.data.remote.ParticipantProfileApi
import com.topit.meetup.feature.chats.domain.model.ChatParticipant
import com.topit.meetup.feature.chats.domain.model.ChatPreview
import com.topit.meetup.feature.chats.domain.repository.ChatsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Instant
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChatsRepositoryImpl @Inject constructor(
    private val chatsApi: ChatsApi,
    private val profileApi: ParticipantProfileApi,
    private val tokenStorage: TokenStorage,
) : ChatsRepository {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private val _chats = MutableStateFlow<List<ChatPreview>>(emptyList())

    init {
        scope.launch { refresh() }
    }

    private suspend fun refresh() {
        val conversationsResult = safeApiCall { chatsApi.getConversations() }
        if (conversationsResult !is DomainResult.Success) return

        val currentUserId = tokenStorage.userId
        val previews = conversationsResult.data.mapNotNull { conversation ->
            val participantId = conversation.participants.firstOrNull { it != currentUserId }
                ?: return@mapNotNull null

            val profileResult = safeApiCall { profileApi.getProfile(participantId) }
            val profile = (profileResult as? DomainResult.Success)?.data

            val timestampMs = runCatching {
                Instant.parse(conversation.createdAt).toEpochMilli()
            }.getOrDefault(System.currentTimeMillis())

            ChatPreview(
                chatId = conversation.id,
                participant = ChatParticipant(
                    id = participantId,
                    name = profile?.displayName ?: participantId,
                    avatarUrl = profile?.photoUrls?.firstOrNull(),
                ),
                lastMessage = "",
                lastMessageTimestamp = timestampMs,
                unreadCount = 0,
            )
        }
        _chats.value = previews
    }

    override fun getChats(): Flow<List<ChatPreview>> = _chats

    override suspend fun deleteChat(chatId: String): DomainResult<Unit> {
        // No DELETE endpoint in the API — remove from local state only
        _chats.update { list -> list.filter { it.chatId != chatId } }
        return DomainResult.Success(Unit)
    }
}
