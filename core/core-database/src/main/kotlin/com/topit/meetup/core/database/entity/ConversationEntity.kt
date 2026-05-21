package com.topit.meetup.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "conversations")
data class ConversationEntity(
    @PrimaryKey val id: String,
    val participantId: String,
    val participantName: String,
    val participantAvatarUrl: String?,
    val lastMessage: String,
    val lastMessageTimestamp: Long,
    val unreadCount: Int,
)
