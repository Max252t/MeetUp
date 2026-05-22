package com.topit.meetup.feature.chats.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class ParticipantProfileDto(
    val id: String,
    val displayName: String,
    val photoUrls: List<String> = emptyList(),
)
