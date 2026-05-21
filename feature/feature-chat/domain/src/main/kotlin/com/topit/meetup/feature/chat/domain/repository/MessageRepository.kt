package com.topit.meetup.feature.chat.domain.repository

import com.topit.meetup.core.common.DomainResult
import com.topit.meetup.feature.chat.domain.model.Message
import kotlinx.coroutines.flow.Flow

interface MessageRepository {
    fun getMessages(chatId: String): Flow<List<Message>>
    suspend fun sendMessage(chatId: String, text: String): DomainResult<Message>
}
