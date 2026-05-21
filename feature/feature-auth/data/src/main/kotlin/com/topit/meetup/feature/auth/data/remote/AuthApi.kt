package com.topit.meetup.feature.auth.data.remote

import com.topit.meetup.feature.auth.data.remote.dto.LoginRequestDto
import com.topit.meetup.feature.auth.data.remote.dto.LogoutRequestDto
import com.topit.meetup.feature.auth.data.remote.dto.RefreshRequestDto
import com.topit.meetup.feature.auth.data.remote.dto.RegisterRequestDto
import com.topit.meetup.feature.auth.data.remote.dto.RegisterResponseDto
import com.topit.meetup.feature.auth.data.remote.dto.TokenPairDto
import com.topit.meetup.feature.auth.data.remote.dto.VerifyOtpRequestDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.POST

interface AuthApi {
    @POST("v1/auth/register")
    suspend fun register(@Body request: RegisterRequestDto): RegisterResponseDto

    @POST("v1/auth/verify-otp")
    suspend fun verifyOtp(@Body request: VerifyOtpRequestDto): TokenPairDto

    @POST("v1/auth/login")
    suspend fun login(@Body request: LoginRequestDto): TokenPairDto

    @POST("v1/auth/refresh")
    suspend fun refresh(@Body request: RefreshRequestDto): TokenPairDto

    @POST("v1/auth/logout")
    suspend fun logout(@Body request: LogoutRequestDto)

    @DELETE("v1/auth/account")
    suspend fun deleteAccount(@Body request: Map<String, String>)
}
