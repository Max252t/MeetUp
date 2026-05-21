package com.topit.meetup.feature.chats.domain.usecase

import com.topit.meetup.core.common.DomainResult
import com.topit.meetup.feature.chats.domain.repository.ChatsRepository
import javax.inject.Inject

class DeleteChatUseCase @Inject constructor(
    private val repository: ChatsRepository,
) {
    suspend operator fun invoke(chatId: String): DomainResult<Unit> =
        repository.deleteChat(chatId)
}
