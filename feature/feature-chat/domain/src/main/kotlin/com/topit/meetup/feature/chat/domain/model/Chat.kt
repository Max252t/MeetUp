package com.topit.meetup.feature.chat.domain.model

data class Chat(
    val id: String,
    val participantId: String,
    val participantName: String,
    val participantAvatarUrl: String?,
)
