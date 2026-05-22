package com.topit.meetup.feature.search.data.remote

import com.topit.meetup.feature.search.data.remote.dto.SearchProfileDto
import retrofit2.http.GET
import retrofit2.http.Path

interface SearchProfileApi {
    @GET("v1/profiles/{userId}")
    suspend fun getProfile(@Path("userId") userId: String): SearchProfileDto
}
