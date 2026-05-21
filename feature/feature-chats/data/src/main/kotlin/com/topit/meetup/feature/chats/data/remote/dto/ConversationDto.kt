package com.topit.meetup.feature.chats.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class ConversationDto(
    val id: String,
    val participants: List<String>,
    val createdAt: String,
)

@Serializable
data class StartConversationRequestDto(
    val targetUserId: String,
)
