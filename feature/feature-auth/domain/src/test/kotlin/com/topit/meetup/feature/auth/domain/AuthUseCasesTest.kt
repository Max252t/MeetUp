package com.topit.meetup.feature.auth.domain

import com.topit.meetup.core.common.DomainError
import com.topit.meetup.core.common.DomainResult
import com.topit.meetup.feature.auth.domain.model.AuthState
import com.topit.meetup.feature.auth.domain.model.Credentials
import com.topit.meetup.feature.auth.domain.repository.AuthRepository
import com.topit.meetup.feature.auth.domain.usecase.GetAuthStateUseCase
import com.topit.meetup.feature.auth.domain.usecase.LoginUseCase
import com.topit.meetup.feature.auth.domain.usecase.LogoutUseCase
import com.topit.meetup.feature.auth.domain.usecase.RefreshTokenUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class AuthUseCasesTest {

    private val stubCredentials = Credentials(phone = "+79001234567", code = "1234")

    // --- GetAuthStateUseCase ---

    @Test
    fun `GetAuthStateUseCase emits Authenticated state`() = runTest {
        val repo = object : FakeAuthRepository() {
            override fun getAuthState(): Flow<AuthState> =
                flowOf(AuthState.Authenticated("u1"))
        }
        val result = GetAuthStateUseCase(repo)().first()
        assertEquals(AuthState.Authenticated("u1"), result)
    }

    @Test
    fun `GetAuthStateUseCase emits Unauthenticated state`() = runTest {
        val repo = object : FakeAuthRepository() {
            override fun getAuthState(): Flow<AuthState> = flowOf(AuthState.Unauthenticated)
        }
        val result = GetAuthStateUseCase(repo)().first()
        assertEquals(AuthState.Unauthenticated, result)
    }

    @Test
    fun `GetAuthStateUseCase emits Loading state`() = runTest {
        val repo = object : FakeAuthRepository() {
            override fun getAuthState(): Flow<AuthState> = flowOf(AuthState.Loading)
        }
        val result = GetAuthStateUseCase(repo)().first()
        assertEquals(AuthState.Loading, result)
    }

    // --- LoginUseCase ---

    @Test
    fun `LoginUseCase returns Success from repository`() = runTest {
        val repo = object : FakeAuthRepository() {
            override suspend fun login(credentials: Credentials) = DomainResult.Success(Unit)
        }
        val result = LoginUseCase(repo)(stubCredentials)
        assertEquals(DomainResult.Success(Unit), result)
    }

    @Test
    fun `LoginUseCase propagates Error on wrong code`() = runTest {
        val error = DomainError.Unauthorized
        val repo = object : FakeAuthRepository() {
            override suspend fun login(credentials: Credentials): DomainResult<Unit> =
                DomainResult.Error(error)
        }
        val result = LoginUseCase(repo)(stubCredentials)
        assertEquals(DomainResult.Error(error), result)
    }

    @Test
    fun `LoginUseCase passes credentials to repository`() = runTest {
        var received: Credentials? = null
        val repo = object : FakeAuthRepository() {
            override suspend fun login(credentials: Credentials): DomainResult<Unit> {
                received = credentials
                return DomainResult.Success(Unit)
            }
        }
        LoginUseCase(repo)(stubCredentials)
        assertEquals(stubCredentials, received)
    }

    // --- LogoutUseCase ---

    @Test
    fun `LogoutUseCase returns Success from repository`() = runTest {
        val repo = object : FakeAuthRepository() {
            override suspend fun logout() = DomainResult.Success(Unit)
        }
        val result = LogoutUseCase(repo)()
        assertEquals(DomainResult.Success(Unit), result)
    }

    @Test
    fun `LogoutUseCase propagates Error from repository`() = runTest {
        val error = DomainError.NetworkError("no connection")
        val repo = object : FakeAuthRepository() {
            override suspend fun logout(): DomainResult<Unit> = DomainResult.Error(error)
        }
        val result = LogoutUseCase(repo)()
        assertEquals(DomainResult.Error(error), result)
    }

    // --- RefreshTokenUseCase ---

    @Test
    fun `RefreshTokenUseCase returns Success from repository`() = runTest {
        val repo = object : FakeAuthRepository() {
            override suspend fun refreshToken() = DomainResult.Success(Unit)
        }
        val result = RefreshTokenUseCase(repo)()
        assertEquals(DomainResult.Success(Unit), result)
    }

    @Test
    fun `RefreshTokenUseCase propagates Unauthorized when token expired`() = runTest {
        val error = DomainError.Unauthorized
        val repo = object : FakeAuthRepository() {
            override suspend fun refreshToken(): DomainResult<Unit> = DomainResult.Error(error)
        }
        val result = RefreshTokenUseCase(repo)()
        assertEquals(DomainResult.Error(error), result)
    }
}

private abstract class FakeAuthRepository : AuthRepository {
    override fun getAuthState(): Flow<AuthState> = flowOf(AuthState.Unauthenticated)
    override suspend fun login(credentials: Credentials): DomainResult<Unit> = DomainResult.Success(Unit)
    override suspend fun logout(): DomainResult<Unit> = DomainResult.Success(Unit)
    override suspend fun refreshToken(): DomainResult<Unit> = DomainResult.Success(Unit)
}
