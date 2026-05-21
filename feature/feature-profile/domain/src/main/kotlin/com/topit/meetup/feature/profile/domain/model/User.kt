package com.topit.meetup.feature.profile.domain.model

data class User(
    val id: String,
    val name: String,
    val age: Int,
    val bio: String,
    val avatarUrl: String?,
    val interests: List<Interest>,
    val location: String?,
)
