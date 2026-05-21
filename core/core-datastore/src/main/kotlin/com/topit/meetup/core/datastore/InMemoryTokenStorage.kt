package com.topit.meetup.core.datastore

import com.topit.meetup.core.common.TokenStorage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InMemoryTokenStorage @Inject constructor() : TokenStorage {
    private val _userId = MutableStateFlow<String?>(null)

    override var accessToken: String? = null
    override var refreshToken: String? = null

    override var userId: String?
        get() = _userId.value
        set(value) { _userId.value = value }

    override val userIdFlow: Flow<String?> = _userId

    override fun clear() {
        accessToken = null
        refreshToken = null
        _userId.value = null
    }
}
