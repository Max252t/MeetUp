package com.topit.meetup.feature.chat.domain.model

data class Message(
    val id: String,
    val chatId: String,
    val senderId: String,
    val text: String,
    val timestamp: Long,
    val status: MessageStatus,
)
