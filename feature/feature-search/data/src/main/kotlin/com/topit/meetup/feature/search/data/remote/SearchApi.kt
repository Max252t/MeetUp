package com.topit.meetup.feature.search.data.remote

import retrofit2.http.GET
import retrofit2.http.Query

interface SearchApi {
    @GET("v1/search/profiles")
    suspend fun searchProfiles(
        @Query("q") query: String? = null,
        @Query("gender") gender: String? = null,
        @Query("minAge") minAge: Int? = null,
        @Query("maxAge") maxAge: Int? = null,
        @Query("city") city: String? = null,
        @Query("country") country: String? = null,
        @Query("interests") interests: List<String>? = null,
        @Query("from") from: Int = 0,
        @Query("size") size: Int = 20,
    ): List<String>
}
