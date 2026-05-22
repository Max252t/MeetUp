package com.topit.meetup.feature.auth.data.remote

import com.topit.meetup.feature.auth.data.remote.dto.MeDto
import retrofit2.http.GET

interface MeApi {
    @GET("v1/profiles/me")
    suspend fun getMe(): MeDto
}
