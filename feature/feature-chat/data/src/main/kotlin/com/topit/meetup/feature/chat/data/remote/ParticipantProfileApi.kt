package com.topit.meetup.feature.chat.data.remote

import com.topit.meetup.feature.chat.data.remote.dto.ParticipantProfileDto
import retrofit2.http.GET
import retrofit2.http.Path

interface ParticipantProfileApi {
    @GET("v1/profiles/{userId}")
    suspend fun getProfile(@Path("userId") userId: String): ParticipantProfileDto
}
