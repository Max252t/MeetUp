package com.topit.meetup.feature.chat.data.remote

import com.topit.meetup.feature.chat.data.remote.dto.MessageDto
import com.topit.meetup.feature.chat.data.remote.dto.MessagePageDto
import com.topit.meetup.feature.chat.data.remote.dto.SendMessageRequestDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ChatApi {
    @GET("v1/chat/conversations/{conversationId}/messages")
    suspend fun getMessages(
        @Path("conversationId") conversationId: String,
        @Query("cursor") cursor: String? = null,
        @Query("limit") limit: Int = 50,
    ): MessagePageDto

    @POST("v1/chat/conversations/{conversationId}/messages")
    suspend fun sendMessage(
        @Path("conversationId") conversationId: String,
        @Body request: SendMessageRequestDto,
    ): MessageDto
}
