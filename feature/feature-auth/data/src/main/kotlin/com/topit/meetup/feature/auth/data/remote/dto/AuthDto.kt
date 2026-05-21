package com.topit.meetup.feature.auth.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class RegisterRequestDto(
    val email: String,
    val password: String,
    val displayName: String,
    val phone: String? = null,
)

@Serializable
data class RegisterResponseDto(
    val userId: String,
    val message: String,
)

@Serializable
data class VerifyOtpRequestDto(
    val email: String,
    val code: String,
)

@Serializable
data class LoginRequestDto(
    val email: String,
    val password: String,
)

@Serializable
data class RefreshRequestDto(
    val refreshToken: String,
)

@Serializable
data class LogoutRequestDto(
    val userId: String,
    val refreshToken: String,
)

@Serializable
data class TokenPairDto(
    val accessToken: String,
    val refreshToken: String,
    val expiresIn: Int,
)
