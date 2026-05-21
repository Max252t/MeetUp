package com.topit.meetup.feature.auth.data.repository

import com.topit.meetup.core.common.DomainError
import com.topit.meetup.core.common.DomainResult
import com.topit.meetup.core.common.TokenStorage
import com.topit.meetup.feature.auth.domain.model.AuthState
import com.topit.meetup.feature.auth.domain.model.Credentials
import com.topit.meetup.feature.auth.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val tokenStorage: TokenStorage,
) : AuthRepository {

    override fun getAuthState(): Flow<AuthState> =
        tokenStorage.userIdFlow.map { userId ->
            if (userId != null) AuthState.Authenticated(userId) else AuthState.Unauthenticated
        }

    override suspend fun login(credentials: Credentials): DomainResult<Unit> {
        if (credentials.phone.isBlank() || credentials.code.isBlank()) {
            return DomainResult.Error(DomainError.NetworkError("Invalid credentials"))
        }
        tokenStorage.accessToken = "fake-access-token"
        tokenStorage.refreshToken = "fake-refresh-token"
        tokenStorage.userId = "fake-user-id"
        return DomainResult.Success(Unit)
    }

    override suspend fun logout(): DomainResult<Unit> {
        tokenStorage.clear()
        return DomainResult.Success(Unit)
    }

    override suspend fun refreshToken(): DomainResult<Unit> {
        if (tokenStorage.refreshToken == null) {
            return DomainResult.Error(DomainError.Unauthorized)
        }
        tokenStorage.accessToken = "fake-access-token-refreshed"
        return DomainResult.Success(Unit)
    }
}
