package com.topit.meetup.feature.profile.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class RequestUploadUrlRequestDto(
    val mimeType: String,
    val sizeBytes: Long,
)

@Serializable
data class UploadUrlResponseDto(
    val mediaId: String,
    val uploadUrl: String,
    val expiresIn: Int,
)

@Serializable
data class MediaObjectDto(
    val id: String,
    val url: String? = null,
)
