package com.topit.meetup.core.common

import kotlinx.coroutines.flow.Flow

interface TokenStorage {
    var accessToken: String?
    var refreshToken: String?
    var userId: String?
    val userIdFlow: Flow<String?>
    fun clear()
}
