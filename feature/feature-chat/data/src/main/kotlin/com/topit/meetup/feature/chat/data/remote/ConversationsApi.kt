package com.topit.meetup.feature.chat.data.remote

import com.topit.meetup.feature.chat.data.remote.dto.ConversationDto
import retrofit2.http.GET

interface ConversationsApi {
    @GET("v1/chat/conversations")
    suspend fun getConversations(): List<ConversationDto>
}
