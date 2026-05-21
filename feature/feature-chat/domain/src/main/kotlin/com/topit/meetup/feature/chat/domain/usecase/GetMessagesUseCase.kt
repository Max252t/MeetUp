package com.topit.meetup.feature.chat.domain.usecase

import com.topit.meetup.feature.chat.domain.model.Message
import com.topit.meetup.feature.chat.domain.repository.MessageRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMessagesUseCase @Inject constructor(
    private val repository: MessageRepository,
) {
    operator fun invoke(chatId: String): Flow<List<Message>> =
        repository.getMessages(chatId)
}
