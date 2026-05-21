package com.topit.meetup.feature.search.data.repository

import com.topit.meetup.core.common.DomainResult
import com.topit.meetup.feature.search.domain.model.AgeRange
import com.topit.meetup.feature.search.domain.model.Gender
import com.topit.meetup.feature.search.domain.model.Interest
import com.topit.meetup.feature.search.domain.model.SearchFilters
import com.topit.meetup.feature.search.domain.model.UserSummary
import com.topit.meetup.feature.search.domain.repository.SearchRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SearchRepositoryImpl @Inject constructor() : SearchRepository {

    private var savedFilters = SearchFilters()

    override suspend fun searchUsers(query: String, filters: SearchFilters): DomainResult<List<UserSummary>> {
        val results = FAKE_USERS.filter { user ->
            val matchesQuery = query.isBlank() ||
                user.name.contains(query, ignoreCase = true) ||
                user.location?.contains(query, ignoreCase = true) == true
            val matchesLocation = filters.location == null ||
                user.location?.contains(filters.location, ignoreCase = true) == true
            val matchesAge = user.age in filters.ageRange.min..filters.ageRange.max
            val matchesGender = filters.gender == Gender.ANY
            val matchesInterests = filters.interests.isEmpty() ||
                filters.interests.any { fi -> user.interests.any { ui -> ui.id == fi.id } }
            matchesQuery && matchesLocation && matchesAge && matchesGender && matchesInterests
        }
        return DomainResult.Success(results)
    }

    override suspend fun getInterests(): DomainResult<List<Interest>> =
        DomainResult.Success(INTERESTS)

    override suspend fun saveFilters(filters: SearchFilters): DomainResult<Unit> {
        savedFilters = filters
        return DomainResult.Success(Unit)
    }

    override suspend fun getSavedFilters(): DomainResult<SearchFilters> =
        DomainResult.Success(savedFilters)

    companion object {
        val INTERESTS = listOf(
            Interest("1", "Hiking"),
            Interest("2", "Coffee"),
            Interest("3", "Photography"),
            Interest("4", "Yoga"),
            Interest("5", "Travel"),
            Interest("6", "Music"),
            Interest("7", "Art"),
            Interest("8", "Cooking"),
            Interest("9", "Sports"),
            Interest("10", "Reading"),
        )

        val FAKE_USERS = listOf(
            UserSummary("user-1", "Анна", 25, null, listOf(Interest("4", "Yoga"), Interest("2", "Coffee")), "Москва"),
            UserSummary("user-2", "Дмитрий", 28, null, listOf(Interest("3", "Photography"), Interest("5", "Travel")), "Санкт-Петербург"),
            UserSummary("user-3", "Мария", 23, null, listOf(Interest("1", "Hiking")), "Москва"),
            UserSummary("user-4", "Алексей", 30, null, listOf(Interest("9", "Sports"), Interest("8", "Cooking")), "Казань"),
            UserSummary("user-5", "Елена", 27, null, listOf(Interest("6", "Music"), Interest("7", "Art")), "Москва"),
        )
    }
}
