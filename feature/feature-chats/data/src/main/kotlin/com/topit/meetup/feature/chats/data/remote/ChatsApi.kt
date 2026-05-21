package com.topit.meetup.feature.chats.data.remote

import com.topit.meetup.feature.chats.data.remote.dto.ConversationDto
import com.topit.meetup.feature.chats.data.remote.dto.StartConversationRequestDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ChatsApi {
    @GET("v1/chat/conversations")
    suspend fun getConversations(): List<ConversationDto>

    @POST("v1/chat/conversations")
    suspend fun startConversation(@Body request: StartConversationRequestDto): ConversationDto

    @DELETE("v1/chat/conversations/{conversationId}")
    suspend fun deleteConversation(@Path("conversationId") conversationId: String)
}
