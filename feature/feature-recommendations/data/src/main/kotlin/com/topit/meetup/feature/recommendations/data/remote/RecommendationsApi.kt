package com.topit.meetup.feature.recommendations.data.remote

import retrofit2.http.POST
import retrofit2.http.GET

interface RecommendationsApi {
    @GET("v1/recommendations")
    suspend fun getRecommendations(): List<String>

    @POST("v1/recommendations/refresh")
    suspend fun refreshRecommendations()
}
