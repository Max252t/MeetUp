package com.topit.meetup.feature.chat.domain

import com.topit.meetup.core.common.DomainError
import com.topit.meetup.core.common.DomainResult
import com.topit.meetup.feature.chat.domain.model.Chat
import com.topit.meetup.feature.chat.domain.model.Message
import com.topit.meetup.feature.chat.domain.model.MessageStatus
import com.topit.meetup.feature.chat.domain.repository.ChatRepository
import com.topit.meetup.feature.chat.domain.repository.MessageRepository
import com.topit.meetup.feature.chat.domain.usecase.GetChatUseCase
import com.topit.meetup.feature.chat.domain.usecase.GetMessagesUseCase
import com.topit.meetup.feature.chat.domain.usecase.SendMessageUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class ChatUseCasesTest {

    private val stubChat = Chat("c1", "u2", "Carol", null)
    private val stubMessage = Message("m1", "c1", "u1", "Hello", 2000L, MessageStatus.SENT)

    // --- GetChatUseCase ---

    @Test
    fun `GetChatUseCase returns Success with chat`() = runTest {
        val repo = object : FakeChatRepository() {
            override suspend fun getChat(chatId: String) = DomainResult.Success(stubChat)
        }
        val result = GetChatUseCase(repo)("c1")
        assertEquals(DomainResult.Success(stubChat), result)
    }

    @Test
    fun `GetChatUseCase propagates Error when chat not found`() = runTest {
        val repo = object : FakeChatRepository() {
            override suspend fun getChat(chatId: String): DomainResult<Chat> =
                DomainResult.Error(DomainError.NotFound)
        }
        val result = GetChatUseCase(repo)("missing")
        assertEquals(DomainResult.Error(DomainError.NotFound), result)
    }

    @Test
    fun `GetChatUseCase passes chatId to repository`() = runTest {
        var received: String? = null
        val repo = object : FakeChatRepository() {
            override suspend fun getChat(chatId: String): DomainResult<Chat> {
                received = chatId
                return DomainResult.Success(stubChat)
            }
        }
        GetChatUseCase(repo)("c99")
        assertEquals("c99", received)
    }

    // --- GetMessagesUseCase ---

    @Test
    fun `GetMessagesUseCase emits messages from repository`() = runTest {
        val repo = object : FakeMessageRepository() {
            override fun getMessages(chatId: String): Flow<List<Message>> =
                flowOf(listOf(stubMessage))
        }
        val result = GetMessagesUseCase(repo)("c1").first()
        assertEquals(listOf(stubMessage), result)
    }

    @Test
    fun `GetMessagesUseCase passes chatId to repository`() = runTest {
        var received: String? = null
        val repo = object : FakeMessageRepository() {
            override fun getMessages(chatId: String): Flow<List<Message>> {
                received = chatId
                return flowOf(emptyList())
            }
        }
        GetMessagesUseCase(repo)("c55").first()
        assertEquals("c55", received)
    }

    // --- SendMessageUseCase ---

    @Test
    fun `SendMessageUseCase returns Success with sent message`() = runTest {
        val repo = object : FakeMessageRepository() {
            override suspend fun sendMessage(chatId: String, text: String) =
                DomainResult.Success(stubMessage)
        }
        val result = SendMessageUseCase(repo)("c1", "Hello")
        assertEquals(DomainResult.Success(stubMessage), result)
    }

    @Test
    fun `SendMessageUseCase propagates Error on failure`() = runTest {
        val error = DomainError.NetworkError("offline")
        val repo = object : FakeMessageRepository() {
            override suspend fun sendMessage(chatId: String, text: String): DomainResult<Message> =
                DomainResult.Error(error)
        }
        val result = SendMessageUseCase(repo)("c1", "Hi")
        assertEquals(DomainResult.Error(error), result)
    }

    @Test
    fun `SendMessageUseCase passes chatId and text to repository`() = runTest {
        var receivedId: String? = null
        var receivedText: String? = null
        val repo = object : FakeMessageRepository() {
            override suspend fun sendMessage(chatId: String, text: String): DomainResult<Message> {
                receivedId = chatId
                receivedText = text
                return DomainResult.Success(stubMessage)
            }
        }
        SendMessageUseCase(repo)("c7", "world")
        assertEquals("c7", receivedId)
        assertEquals("world", receivedText)
    }
}

private abstract class FakeChatRepository : ChatRepository {
    override suspend fun getChat(chatId: String): DomainResult<Chat> =
        DomainResult.Success(Chat("", "", "", null))
}

private abstract class FakeMessageRepository : MessageRepository {
    override fun getMessages(chatId: String): Flow<List<Message>> = flowOf(emptyList())
    override suspend fun sendMessage(chatId: String, text: String): DomainResult<Message> =
        DomainResult.Success(Message("", "", "", "", 0L, MessageStatus.SENT))
}
