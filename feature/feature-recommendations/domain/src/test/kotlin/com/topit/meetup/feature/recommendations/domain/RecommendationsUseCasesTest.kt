package com.topit.meetup.feature.recommendations.domain

import com.topit.meetup.core.common.DomainError
import com.topit.meetup.core.common.DomainResult
import com.topit.meetup.feature.recommendations.domain.model.ReactionType
import com.topit.meetup.feature.recommendations.domain.model.RecommendedUser
import com.topit.meetup.feature.recommendations.domain.repository.RecommendationsRepository
import com.topit.meetup.feature.recommendations.domain.usecase.GetRecommendationsUseCase
import com.topit.meetup.feature.recommendations.domain.usecase.ReactToUserUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class RecommendationsUseCasesTest {

    private val stubUser = RecommendedUser(
        id = "u1",
        name = "Eve",
        age = 27,
        bio = "Hello",
        avatarUrl = null,
        interests = listOf("Hiking"),
        location = "Moscow",
        matchScore = 0.9f,
    )

    // --- GetRecommendationsUseCase ---

    @Test
    fun `GetRecommendationsUseCase emits list from repository`() = runTest {
        val repo = object : FakeRecommendationsRepository() {
            override fun getRecommendations(): Flow<List<RecommendedUser>> =
                flowOf(listOf(stubUser))
        }
        val result = GetRecommendationsUseCase(repo)().first()
        assertEquals(listOf(stubUser), result)
    }

    @Test
    fun `GetRecommendationsUseCase emits empty list when none available`() = runTest {
        val repo = object : FakeRecommendationsRepository() {
            override fun getRecommendations(): Flow<List<RecommendedUser>> = flowOf(emptyList())
        }
        val result = GetRecommendationsUseCase(repo)().first()
        assertEquals(emptyList<RecommendedUser>(), result)
    }

    // --- ReactToUserUseCase ---

    @Test
    fun `ReactToUserUseCase returns Success on like`() = runTest {
        val repo = object : FakeRecommendationsRepository() {
            override suspend fun reactToUser(userId: String, reaction: ReactionType) =
                DomainResult.Success(Unit)
        }
        val result = ReactToUserUseCase(repo)("u1", ReactionType.LIKE)
        assertEquals(DomainResult.Success(Unit), result)
    }

    @Test
    fun `ReactToUserUseCase returns Success on skip`() = runTest {
        val repo = object : FakeRecommendationsRepository() {
            override suspend fun reactToUser(userId: String, reaction: ReactionType) =
                DomainResult.Success(Unit)
        }
        val result = ReactToUserUseCase(repo)("u1", ReactionType.SKIP)
        assertEquals(DomainResult.Success(Unit), result)
    }

    @Test
    fun `ReactToUserUseCase passes userId and reaction to repository`() = runTest {
        var receivedId: String? = null
        var receivedReaction: ReactionType? = null
        val repo = object : FakeRecommendationsRepository() {
            override suspend fun reactToUser(userId: String, reaction: ReactionType): DomainResult<Unit> {
                receivedId = userId
                receivedReaction = reaction
                return DomainResult.Success(Unit)
            }
        }
        ReactToUserUseCase(repo)("u42", ReactionType.LIKE)
        assertEquals("u42", receivedId)
        assertEquals(ReactionType.LIKE, receivedReaction)
    }

    @Test
    fun `ReactToUserUseCase propagates Error from repository`() = runTest {
        val error = DomainError.NetworkError("timeout")
        val repo = object : FakeRecommendationsRepository() {
            override suspend fun reactToUser(userId: String, reaction: ReactionType): DomainResult<Unit> =
                DomainResult.Error(error)
        }
        val result = ReactToUserUseCase(repo)("u1", ReactionType.SKIP)
        assertEquals(DomainResult.Error(error), result)
    }
}

private abstract class FakeRecommendationsRepository : RecommendationsRepository {
    override fun getRecommendations(): Flow<List<RecommendedUser>> = flowOf(emptyList())
    override suspend fun reactToUser(userId: String, reaction: ReactionType): DomainResult<Unit> =
        DomainResult.Success(Unit)
}
