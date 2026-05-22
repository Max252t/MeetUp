package com.topit.meetup.feature.recommendations.data.remote

import com.topit.meetup.feature.recommendations.data.remote.dto.RecommendedProfileDto
import retrofit2.http.GET
import retrofit2.http.Path

interface RecommendedProfileApi {
    @GET("v1/profiles/{userId}")
    suspend fun getProfile(@Path("userId") userId: String): RecommendedProfileDto
}
