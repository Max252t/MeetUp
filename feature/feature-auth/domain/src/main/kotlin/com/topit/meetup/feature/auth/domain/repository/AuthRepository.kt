package com.topit.meetup.feature.auth.domain.repository

import com.topit.meetup.core.common.DomainResult
import com.topit.meetup.feature.auth.domain.model.AuthState
import com.topit.meetup.feature.auth.domain.model.Credentials
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun getAuthState(): Flow<AuthState>
    suspend fun login(credentials: Credentials): DomainResult<Unit>
    suspend fun logout(): DomainResult<Unit>
    suspend fun refreshToken(): DomainResult<Unit>
}
