package com.topit.meetup.feature.chats.domain

import com.topit.meetup.core.common.DomainError
import com.topit.meetup.core.common.DomainResult
import com.topit.meetup.feature.chats.domain.model.ChatParticipant
import com.topit.meetup.feature.chats.domain.model.ChatPreview
import com.topit.meetup.feature.chats.domain.repository.ChatsRepository
import com.topit.meetup.feature.chats.domain.usecase.DeleteChatUseCase
import com.topit.meetup.feature.chats.domain.usecase.GetChatsUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class ChatsUseCasesTest {

    private val stubChat = ChatPreview(
        chatId = "c1",
        participant = ChatParticipant("u1", "Bob", null),
        lastMessage = "Hey",
        lastMessageTimestamp = 1000L,
        unreadCount = 2,
    )

    // --- GetChatsUseCase ---

    @Test
    fun `GetChatsUseCase emits list from repository`() = runTest {
        val repo = object : FakeChatsRepository() {
            override fun getChats(): Flow<List<ChatPreview>> = flowOf(listOf(stubChat))
        }
        val result = GetChatsUseCase(repo)().first()
        assertEquals(listOf(stubChat), result)
    }

    @Test
    fun `GetChatsUseCase emits empty list when no chats`() = runTest {
        val repo = object : FakeChatsRepository() {
            override fun getChats(): Flow<List<ChatPreview>> = flowOf(emptyList())
        }
        val result = GetChatsUseCase(repo)().first()
        assertEquals(emptyList<ChatPreview>(), result)
    }

    // --- DeleteChatUseCase ---

    @Test
    fun `DeleteChatUseCase returns Success from repository`() = runTest {
        val repo = object : FakeChatsRepository() {
            override suspend fun deleteChat(chatId: String) = DomainResult.Success(Unit)
        }
        val result = DeleteChatUseCase(repo)("c1")
        assertEquals(DomainResult.Success(Unit), result)
    }

    @Test
    fun `DeleteChatUseCase propagates Error from repository`() = runTest {
        val error = DomainError.NotFound
        val repo = object : FakeChatsRepository() {
            override suspend fun deleteChat(chatId: String): DomainResult<Unit> =
                DomainResult.Error(error)
        }
        val result = DeleteChatUseCase(repo)("c1")
        assertEquals(DomainResult.Error(error), result)
    }

    @Test
    fun `DeleteChatUseCase passes correct chatId to repository`() = runTest {
        var received: String? = null
        val repo = object : FakeChatsRepository() {
            override suspend fun deleteChat(chatId: String): DomainResult<Unit> {
                received = chatId
                return DomainResult.Success(Unit)
            }
        }
        DeleteChatUseCase(repo)("c42")
        assertEquals("c42", received)
    }
}

private abstract class FakeChatsRepository : ChatsRepository {
    override fun getChats(): Flow<List<ChatPreview>> = flowOf(emptyList())
    override suspend fun deleteChat(chatId: String): DomainResult<Unit> = DomainResult.Success(Unit)
}
