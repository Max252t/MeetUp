package com.topit.meetup.feature.chat.domain.usecase

import com.topit.meetup.core.common.DomainResult
import com.topit.meetup.feature.chat.domain.model.Chat
import com.topit.meetup.feature.chat.domain.repository.ChatRepository
import javax.inject.Inject

class GetChatUseCase @Inject constructor(
    private val repository: ChatRepository,
) {
    suspend operator fun invoke(chatId: String): DomainResult<Chat> =
        repository.getChat(chatId)
}
