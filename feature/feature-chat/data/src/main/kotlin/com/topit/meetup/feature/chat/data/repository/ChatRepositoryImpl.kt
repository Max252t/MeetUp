package com.topit.meetup.feature.chat.data.repository

import com.topit.meetup.core.common.DomainError
import com.topit.meetup.core.common.DomainResult
import com.topit.meetup.core.common.TokenStorage
import com.topit.meetup.core.network.safeApiCall
import com.topit.meetup.feature.chat.data.remote.ConversationsApi
import com.topit.meetup.feature.chat.data.remote.ParticipantProfileApi
import com.topit.meetup.feature.chat.domain.model.Chat
import com.topit.meetup.feature.chat.domain.repository.ChatRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChatRepositoryImpl @Inject constructor(
    private val conversationsApi: ConversationsApi,
    private val profileApi: ParticipantProfileApi,
    private val tokenStorage: TokenStorage,
) : ChatRepository {

    override suspend fun getChat(chatId: String): DomainResult<Chat> {
        val conversationsResult = safeApiCall { conversationsApi.getConversations() }
        if (conversationsResult is DomainResult.Error) return conversationsResult

        val conversations = (conversationsResult as DomainResult.Success).data
        val conversation = conversations.firstOrNull { it.id == chatId }
            ?: return DomainResult.Error(DomainError.NotFound)

        val currentUserId = tokenStorage.userId
        val participantId = conversation.participants.firstOrNull { it != currentUserId }
            ?: return DomainResult.Error(DomainError.NotFound)

        val profileResult = safeApiCall { profileApi.getProfile(participantId) }
        val profile = (profileResult as? DomainResult.Success)?.data

        return DomainResult.Success(
            Chat(
                id = chatId,
                participantId = participantId,
                participantName = profile?.displayName ?: participantId,
                participantAvatarUrl = profile?.photoUrls?.firstOrNull(),
            )
        )
    }
}
