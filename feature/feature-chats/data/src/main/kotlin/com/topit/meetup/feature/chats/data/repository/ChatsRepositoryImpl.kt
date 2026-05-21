package com.topit.meetup.feature.chats.data.repository

import com.topit.meetup.core.common.DomainResult
import com.topit.meetup.feature.chats.domain.model.ChatParticipant
import com.topit.meetup.feature.chats.domain.model.ChatPreview
import com.topit.meetup.feature.chats.domain.repository.ChatsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChatsRepositoryImpl @Inject constructor() : ChatsRepository {

    private val _chats = MutableStateFlow(FAKE_CHATS)

    override fun getChats(): Flow<List<ChatPreview>> = _chats

    override suspend fun deleteChat(chatId: String): DomainResult<Unit> {
        _chats.update { list -> list.filter { it.chatId != chatId } }
        return DomainResult.Success(Unit)
    }

    companion object {
        private val now = System.currentTimeMillis()
        val FAKE_CHATS = listOf(
            ChatPreview(
                chatId = "chat-1",
                participant = ChatParticipant("user-1", "Анна", null),
                lastMessage = "Привет!",
                lastMessageTimestamp = now - 3_600_000L,
                unreadCount = 2,
            ),
            ChatPreview(
                chatId = "chat-2",
                participant = ChatParticipant("user-2", "Дмитрий", null),
                lastMessage = "Как дела?",
                lastMessageTimestamp = now - 7_200_000L,
                unreadCount = 0,
            ),
            ChatPreview(
                chatId = "chat-3",
                participant = ChatParticipant("user-3", "Мария", null),
                lastMessage = "Увидимся в выходные?",
                lastMessageTimestamp = now - 86_400_000L,
                unreadCount = 1,
            ),
        )
    }
}
