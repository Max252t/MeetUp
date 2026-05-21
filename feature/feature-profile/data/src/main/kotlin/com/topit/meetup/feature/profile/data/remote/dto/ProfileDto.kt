package com.topit.meetup.feature.profile.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class ProfileDto(
    val id: String,
    val displayName: String,
    val bio: String? = null,
    val city: String? = null,
    val country: String? = null,
    val gender: String? = null,
    val birthDate: String? = null,
    val interests: List<String> = emptyList(),
    val photoUrls: List<String> = emptyList(),
    val isActive: Boolean = true,
    val createdAt: String = "",
    val updatedAt: String = "",
)

@Serializable
data class UpsertProfileRequestDto(
    val displayName: String,
    val bio: String? = null,
    val city: String? = null,
    val country: String? = null,
    val gender: String? = null,
    val birthDate: String? = null,
    val interests: List<String> = emptyList(),
)
