package com.topit.meetup.feature.search.domain.model

data class UserSummary(
    val id: String,
    val name: String,
    val age: Int,
    val avatarUrl: String?,
    val interests: List<Interest>,
    val location: String?,
)
