package com.topit.meetup.feature.chat.domain.usecase

import com.topit.meetup.core.common.DomainResult
import com.topit.meetup.feature.chat.domain.model.Message
import com.topit.meetup.feature.chat.domain.repository.MessageRepository
import javax.inject.Inject

class SendMessageUseCase @Inject constructor(
    private val repository: MessageRepository,
) {
    suspend operator fun invoke(chatId: String, text: String): DomainResult<Message> =
        repository.sendMessage(chatId, text)
}
