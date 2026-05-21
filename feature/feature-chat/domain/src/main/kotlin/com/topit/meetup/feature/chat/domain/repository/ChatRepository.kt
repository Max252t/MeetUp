package com.topit.meetup.feature.chat.domain.repository

import com.topit.meetup.core.common.DomainResult
import com.topit.meetup.feature.chat.domain.model.Chat

interface ChatRepository {
    suspend fun getChat(chatId: String): DomainResult<Chat>
}
