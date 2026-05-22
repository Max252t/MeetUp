package com.topit.meetup.feature.profile.data.remote

import com.topit.meetup.feature.profile.data.remote.dto.MediaObjectDto
import com.topit.meetup.feature.profile.data.remote.dto.RequestUploadUrlRequestDto
import com.topit.meetup.feature.profile.data.remote.dto.UploadUrlResponseDto
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

interface MediaApi {
    @POST("v1/media/upload-url")
    suspend fun requestUploadUrl(@Body request: RequestUploadUrlRequestDto): UploadUrlResponseDto

    @POST("v1/media/{mediaId}/confirm")
    suspend fun confirmUpload(@Path("mediaId") mediaId: String): MediaObjectDto
}
