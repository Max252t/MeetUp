package com.topit.meetup.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val id: String,
    val name: String,
    val age: Int,
    val bio: String,
    val avatarUrl: String?,
    val interests: String,
    val location: String?,
)
