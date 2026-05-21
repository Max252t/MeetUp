package com.topit.meetup.feature.profile.domain

import com.topit.meetup.core.common.DomainError
import com.topit.meetup.core.common.DomainResult
import com.topit.meetup.feature.profile.domain.model.Interest
import com.topit.meetup.feature.profile.domain.model.User
import com.topit.meetup.feature.profile.domain.repository.ProfileRepository
import com.topit.meetup.feature.profile.domain.usecase.GetProfileUseCase
import com.topit.meetup.feature.profile.domain.usecase.UpdateProfileUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class ProfileUseCasesTest {

    private val stubUser = User(
        id = "1",
        name = "Alice",
        age = 25,
        bio = "Hi",
        avatarUrl = null,
        interests = emptyList(),
        location = null,
    )

    // --- GetProfileUseCase ---

    @Test
    fun `GetProfileUseCase returns flow from repository`() = runTest {
        val repo = object : FakeProfileRepository() {
            override fun getProfile(): Flow<User?> = flowOf(stubUser)
        }
        val result = GetProfileUseCase(repo)().first()
        assertEquals(stubUser, result)
    }

    @Test
    fun `GetProfileUseCase returns null when profile absent`() = runTest {
        val repo = object : FakeProfileRepository() {
            override fun getProfile(): Flow<User?> = flowOf(null)
        }
        val result = GetProfileUseCase(repo)().first()
        assertEquals(null, result)
    }

    // --- UpdateProfileUseCase ---

    @Test
    fun `UpdateProfileUseCase returns Success from repository`() = runTest {
        val repo = object : FakeProfileRepository() {
            override suspend fun updateProfile(user: User) = DomainResult.Success(Unit)
        }
        val result = UpdateProfileUseCase(repo)(stubUser)
        assertEquals(DomainResult.Success(Unit), result)
    }

    @Test
    fun `UpdateProfileUseCase propagates Error from repository`() = runTest {
        val error = DomainError.NetworkError("fail")
        val repo = object : FakeProfileRepository() {
            override suspend fun updateProfile(user: User): DomainResult<Unit> =
                DomainResult.Error(error)
        }
        val result = UpdateProfileUseCase(repo)(stubUser)
        assertEquals(DomainResult.Error(error), result)
    }

    @Test
    fun `UpdateProfileUseCase passes correct user to repository`() = runTest {
        var received: User? = null
        val repo = object : FakeProfileRepository() {
            override suspend fun updateProfile(user: User): DomainResult<Unit> {
                received = user
                return DomainResult.Success(Unit)
            }
        }
        UpdateProfileUseCase(repo)(stubUser)
        assertEquals(stubUser, received)
    }
}

private abstract class FakeProfileRepository : ProfileRepository {
    override fun getProfile(): Flow<User?> = flowOf(null)
    override suspend fun updateProfile(user: User): DomainResult<Unit> = DomainResult.Success(Unit)
    override suspend fun getAvailableInterests(): DomainResult<List<Interest>> = DomainResult.Success(emptyList())
    override suspend fun uploadAvatar(imageBytes: ByteArray): DomainResult<String> = DomainResult.Success("")
}
