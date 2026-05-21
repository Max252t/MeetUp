package com.topit.meetup.feature.profile.data.remote

import com.topit.meetup.feature.profile.data.remote.dto.ProfileDto
import com.topit.meetup.feature.profile.data.remote.dto.UpsertProfileRequestDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

interface ProfileApi {
    @GET("v1/profiles/me")
    suspend fun getMyProfile(): ProfileDto

    @PUT("v1/profiles/me")
    suspend fun upsertProfile(@Body request: UpsertProfileRequestDto): ProfileDto

    @DELETE("v1/profiles/me")
    suspend fun deactivateProfile()

    @GET("v1/profiles/{userId}")
    suspend fun getProfile(@Path("userId") userId: String): ProfileDto
}
