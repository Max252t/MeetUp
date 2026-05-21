package com.topit.meetup.feature.chats.domain.usecase

import com.topit.meetup.feature.chats.domain.model.ChatPreview
import com.topit.meetup.feature.chats.domain.repository.ChatsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetChatsUseCase @Inject constructor(
    private val repository: ChatsRepository,
) {
    operator fun invoke(): Flow<List<ChatPreview>> = repository.getChats()
}
