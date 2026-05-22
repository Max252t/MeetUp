package com.topit.meetup.feature.recommendations.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class RecommendedProfileDto(
    val id: String,
    val displayName: String,
    val bio: String? = null,
    val city: String? = null,
    val country: String? = null,
    val interests: List<String> = emptyList(),
    val photoUrls: List<String> = emptyList(),
    val birthDate: String? = null,
)
