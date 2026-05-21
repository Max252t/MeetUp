package com.topit.meetup.feature.search.domain

import com.topit.meetup.core.common.DomainError
import com.topit.meetup.core.common.DomainResult
import com.topit.meetup.feature.search.domain.model.AgeRange
import com.topit.meetup.feature.search.domain.model.Gender
import com.topit.meetup.feature.search.domain.model.Interest
import com.topit.meetup.feature.search.domain.model.SearchFilters
import com.topit.meetup.feature.search.domain.model.UserSummary
import com.topit.meetup.feature.search.domain.repository.SearchRepository
import com.topit.meetup.feature.search.domain.usecase.GetInterestsUseCase
import com.topit.meetup.feature.search.domain.usecase.GetSavedFiltersUseCase
import com.topit.meetup.feature.search.domain.usecase.SaveFiltersUseCase
import com.topit.meetup.feature.search.domain.usecase.SearchUsersUseCase
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class SearchUseCasesTest {

    private val stubFilters = SearchFilters(
        gender = Gender.FEMALE,
        ageRange = AgeRange(20, 30),
        interests = emptyList(),
    )
    private val stubUser = UserSummary("u1", "Dana", 24, null, emptyList(), null)
    private val stubInterests = listOf(Interest("i1", "Music"), Interest("i2", "Travel"))

    // --- SearchUsersUseCase ---

    @Test
    fun `SearchUsersUseCase returns users from repository`() = runTest {
        val repo = object : FakeSearchRepository() {
            override suspend fun searchUsers(query: String, filters: SearchFilters) =
                DomainResult.Success(listOf(stubUser))
        }
        val result = SearchUsersUseCase(repo)("dana", stubFilters)
        assertEquals(DomainResult.Success(listOf(stubUser)), result)
    }

    @Test
    fun `SearchUsersUseCase passes query and filters to repository`() = runTest {
        var receivedQuery: String? = null
        var receivedFilters: SearchFilters? = null
        val repo = object : FakeSearchRepository() {
            override suspend fun searchUsers(query: String, filters: SearchFilters): DomainResult<List<UserSummary>> {
                receivedQuery = query
                receivedFilters = filters
                return DomainResult.Success(emptyList())
            }
        }
        SearchUsersUseCase(repo)("test", stubFilters)
        assertEquals("test", receivedQuery)
        assertEquals(stubFilters, receivedFilters)
    }

    @Test
    fun `SearchUsersUseCase propagates Error from repository`() = runTest {
        val error = DomainError.ServerError(500, "server error")
        val repo = object : FakeSearchRepository() {
            override suspend fun searchUsers(query: String, filters: SearchFilters): DomainResult<List<UserSummary>> =
                DomainResult.Error(error)
        }
        val result = SearchUsersUseCase(repo)("x", stubFilters)
        assertEquals(DomainResult.Error(error), result)
    }

    // --- GetInterestsUseCase ---

    @Test
    fun `GetInterestsUseCase returns interests from repository`() = runTest {
        val repo = object : FakeSearchRepository() {
            override suspend fun getInterests() = DomainResult.Success(stubInterests)
        }
        val result = GetInterestsUseCase(repo)()
        assertEquals(DomainResult.Success(stubInterests), result)
    }

    // --- SaveFiltersUseCase ---

    @Test
    fun `SaveFiltersUseCase returns Success from repository`() = runTest {
        val repo = object : FakeSearchRepository() {
            override suspend fun saveFilters(filters: SearchFilters) = DomainResult.Success(Unit)
        }
        val result = SaveFiltersUseCase(repo)(stubFilters)
        assertEquals(DomainResult.Success(Unit), result)
    }

    @Test
    fun `SaveFiltersUseCase passes filters to repository`() = runTest {
        var received: SearchFilters? = null
        val repo = object : FakeSearchRepository() {
            override suspend fun saveFilters(filters: SearchFilters): DomainResult<Unit> {
                received = filters
                return DomainResult.Success(Unit)
            }
        }
        SaveFiltersUseCase(repo)(stubFilters)
        assertEquals(stubFilters, received)
    }

    // --- GetSavedFiltersUseCase ---

    @Test
    fun `GetSavedFiltersUseCase returns saved filters from repository`() = runTest {
        val repo = object : FakeSearchRepository() {
            override suspend fun getSavedFilters() = DomainResult.Success(stubFilters)
        }
        val result = GetSavedFiltersUseCase(repo)()
        assertEquals(DomainResult.Success(stubFilters), result)
    }

    @Test
    fun `GetSavedFiltersUseCase propagates Error when no saved filters`() = runTest {
        val error = DomainError.NotFound
        val repo = object : FakeSearchRepository() {
            override suspend fun getSavedFilters(): DomainResult<SearchFilters> =
                DomainResult.Error(error)
        }
        val result = GetSavedFiltersUseCase(repo)()
        assertEquals(DomainResult.Error(error), result)
    }
}

private abstract class FakeSearchRepository : SearchRepository {
    override suspend fun searchUsers(query: String, filters: SearchFilters): DomainResult<List<UserSummary>> =
        DomainResult.Success(emptyList())
    override suspend fun getInterests(): DomainResult<List<Interest>> =
        DomainResult.Success(emptyList())
    override suspend fun saveFilters(filters: SearchFilters): DomainResult<Unit> =
        DomainResult.Success(Unit)
    override suspend fun getSavedFilters(): DomainResult<SearchFilters> =
        DomainResult.Success(SearchFilters())
}
