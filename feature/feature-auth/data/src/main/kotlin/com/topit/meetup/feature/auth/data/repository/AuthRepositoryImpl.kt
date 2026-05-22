package com.topit.meetup.feature.auth.data.repository

import com.topit.meetup.core.common.DomainError
import com.topit.meetup.core.common.DomainResult
import com.topit.meetup.core.common.TokenStorage
import com.topit.meetup.core.network.safeApiCall
import com.topit.meetup.feature.auth.data.remote.AuthApi
import com.topit.meetup.feature.auth.data.remote.MeApi
import com.topit.meetup.feature.auth.data.remote.dto.LoginRequestDto
import com.topit.meetup.feature.auth.data.remote.dto.LogoutRequestDto
import com.topit.meetup.feature.auth.data.remote.dto.RefreshRequestDto
import com.topit.meetup.feature.auth.domain.model.AuthState
import com.topit.meetup.feature.auth.domain.model.Credentials
import com.topit.meetup.feature.auth.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authApi: AuthApi,
    private val meApi: MeApi,
    private val tokenStorage: TokenStorage,
) : AuthRepository {

    override fun getAuthState(): Flow<AuthState> =
        tokenStorage.userIdFlow.map { userId ->
            if (userId != null) AuthState.Authenticated(userId) else AuthState.Unauthenticated
        }

    override suspend fun login(credentials: Credentials): DomainResult<Unit> {
        val tokenResult = safeApiCall {
            authApi.login(
                LoginRequestDto(
                    email = credentials.phone,
                    password = credentials.code,
                )
            )
        }
        if (tokenResult is DomainResult.Error) return tokenResult

        val tokenPair = (tokenResult as DomainResult.Success).data
        tokenStorage.accessToken = tokenPair.accessToken
        tokenStorage.refreshToken = tokenPair.refreshToken

        val meResult = safeApiCall { meApi.getMe() }
        if (meResult is DomainResult.Error) {
            tokenStorage.clear()
            return meResult
        }

        tokenStorage.userId = (meResult as DomainResult.Success).data.id
        return DomainResult.Success(Unit)
    }

    override suspend fun logout(): DomainResult<Unit> {
        val userId = tokenStorage.userId
        val refreshToken = tokenStorage.refreshToken
        if (userId != null && refreshToken != null) {
            safeApiCall {
                authApi.logout(LogoutRequestDto(userId = userId, refreshToken = refreshToken))
            }
        }
        tokenStorage.clear()
        return DomainResult.Success(Unit)
    }

    override suspend fun refreshToken(): DomainResult<Unit> {
        val currentRefreshToken = tokenStorage.refreshToken
            ?: return DomainResult.Error(DomainError.Unauthorized)

        val result = safeApiCall {
            authApi.refresh(RefreshRequestDto(refreshToken = currentRefreshToken))
        }
        if (result is DomainResult.Error) {
            tokenStorage.clear()
            return result
        }

        val tokenPair = (result as DomainResult.Success).data
        tokenStorage.accessToken = tokenPair.accessToken
        tokenStorage.refreshToken = tokenPair.refreshToken
        return DomainResult.Success(Unit)
    }
}
