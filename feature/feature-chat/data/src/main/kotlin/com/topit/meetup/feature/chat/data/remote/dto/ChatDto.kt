package com.topit.meetup.feature.chat.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class MessageDto(
    val id: String,
    val conversationId: String,
    val senderId: String,
    val content: String,
    val isEncrypted: Boolean = false,
    val createdAt: String,
)

@Serializable
data class MessagePageDto(
    val items: List<MessageDto>,
    val nextCursor: String? = null,
    val hasMore: Boolean = false,
)

@Serializable
data class SendMessageRequestDto(
    val content: String,
    val isEncrypted: Boolean = false,
)
