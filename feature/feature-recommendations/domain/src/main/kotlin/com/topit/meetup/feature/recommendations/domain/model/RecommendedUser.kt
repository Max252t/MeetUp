package com.topit.meetup.feature.recommendations.domain.model

data class RecommendedUser(
    val id: String,
    val name: String,
    val age: Int,
    val bio: String,
    val avatarUrl: String?,
    val interests: List<String>,
    val location: String?,
    val matchScore: Float,
)
