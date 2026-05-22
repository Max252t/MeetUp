package com.topit.meetup.feature.chat.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class ConversationDto(
    val id: String,
    val participants: List<String>,
    val createdAt: String,
)
