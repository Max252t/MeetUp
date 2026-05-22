package com.topit.meetup.feature.chat.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class ParticipantProfileDto(
    val id: String,
    val displayName: String,
    val photoUrls: List<String> = emptyList(),
)
