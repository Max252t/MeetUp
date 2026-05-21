package com.topit.meetup.feature.chat.data.repository

import com.topit.meetup.core.common.DomainError
import com.topit.meetup.core.common.DomainResult
import com.topit.meetup.feature.chat.domain.model.Chat
import com.topit.meetup.feature.chat.domain.repository.ChatRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChatRepositoryImpl @Inject constructor() : ChatRepository {

    override suspend fun getChat(chatId: String): DomainResult<Chat> {
        val chat = FAKE_CHATS[chatId]
            ?: return DomainResult.Error(DomainError.NotFound)
        return DomainResult.Success(chat)
    }

    companion object {
        val FAKE_CHATS = mapOf(
            "chat-1" to Chat("chat-1", "user-1", "Анна", null),
            "chat-2" to Chat("chat-2", "user-2", "Дмитрий", null),
            "chat-3" to Chat("chat-3", "user-3", "Мария", null),
        )
    }
}
