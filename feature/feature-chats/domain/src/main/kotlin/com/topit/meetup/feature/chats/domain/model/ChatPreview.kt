package com.topit.meetup.feature.chats.domain.model

data class ChatPreview(
    val chatId: String,
    val participant: ChatParticipant,
    val lastMessage: String,
    val lastMessageTimestamp: Long,
    val unreadCount: Int,
)
