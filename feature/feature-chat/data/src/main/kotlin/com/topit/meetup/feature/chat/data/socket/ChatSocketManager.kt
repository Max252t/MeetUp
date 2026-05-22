package com.topit.meetup.feature.chat.data.socket

import com.topit.meetup.core.common.TokenStorage
import io.socket.client.IO
import io.socket.client.Socket
import org.json.JSONObject
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChatSocketManager @Inject constructor(
    private val tokenStorage: TokenStorage,
) {
    private var socket: Socket? = null

    private fun requireSocket(): Socket {
        return socket ?: run {
            val options = IO.Options.builder()
                .setExtraHeaders(
                    mapOf("Authorization" to listOf("Bearer ${tokenStorage.accessToken}"))
                )
                .build()
            IO.socket("http://10.0.2.2:3000", options).also { s ->
                socket = s
                s.connect()
            }
        }
    }

    fun sendMessage(conversationId: String, content: String) {
        val payload = JSONObject().apply {
            put("conversationId", conversationId)
            put("content", content)
        }
        requireSocket().emit("send_message", payload)
    }

    fun disconnect() {
        socket?.disconnect()
        socket = null
    }
}
