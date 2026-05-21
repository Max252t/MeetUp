package com.topit.meetup.feature.chats.domain.repository

import com.topit.meetup.core.common.DomainResult
import com.topit.meetup.feature.chats.domain.model.ChatPreview
import kotlinx.coroutines.flow.Flow

interface ChatsRepository {
    fun getChats(): Flow<List<ChatPreview>>
    suspend fun deleteChat(chatId: String): DomainResult<Unit>
}
