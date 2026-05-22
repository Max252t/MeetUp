package com.topit.meetup.feature.search.data.repository

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.topit.meetup.core.common.DomainResult
import com.topit.meetup.core.network.safeApiCall
import com.topit.meetup.feature.search.data.remote.SearchApi
import com.topit.meetup.feature.search.data.remote.SearchProfileApi
import com.topit.meetup.feature.search.data.remote.dto.SearchProfileDto
import com.topit.meetup.feature.search.domain.model.AgeRange
import com.topit.meetup.feature.search.domain.model.Gender
import com.topit.meetup.feature.search.domain.model.Interest
import com.topit.meetup.feature.search.domain.model.SearchFilters
import com.topit.meetup.feature.search.domain.model.UserSummary
import com.topit.meetup.feature.search.domain.repository.SearchRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import java.time.Instant
import java.time.ZoneId
import javax.inject.Inject
import javax.inject.Singleton

private val Context.searchFiltersDataStore by preferencesDataStore("search_filters")

@Singleton
class SearchRepositoryImpl @Inject constructor(
    private val searchApi: SearchApi,
    private val profileApi: SearchProfileApi,
    @ApplicationContext private val context: Context,
) : SearchRepository {

    private val dataStore = context.searchFiltersDataStore

    private object Keys {
        val GENDER = stringPreferencesKey("gender")
        val AGE_MIN = intPreferencesKey("age_min")
        val AGE_MAX = intPreferencesKey("age_max")
        val LOCATION = stringPreferencesKey("location")
    }

    override suspend fun searchUsers(query: String, filters: SearchFilters): DomainResult<List<UserSummary>> {
        val genderParam = when (filters.gender) {
            Gender.MALE -> "male"
            Gender.FEMALE -> "female"
            Gender.OTHER -> "other"
            Gender.ANY -> null
        }

        val userIdsResult = safeApiCall {
            searchApi.searchProfiles(
                query = query.ifBlank { null },
                gender = genderParam,
                minAge = filters.ageRange.min.takeIf { it > 18 },
                maxAge = filters.ageRange.max.takeIf { it < 99 },
                city = filters.location,
                interests = filters.interests.map { it.id }.takeIf { it.isNotEmpty() },
            )
        }
        if (userIdsResult is DomainResult.Error) return userIdsResult

        val userIds = (userIdsResult as DomainResult.Success).data
        val users = userIds.mapNotNull { userId ->
            val profileResult = safeApiCall { profileApi.getProfile(userId) }
            (profileResult as? DomainResult.Success)?.data?.toUserSummary()
        }
        return DomainResult.Success(users)
    }

    override suspend fun getInterests(): DomainResult<List<Interest>> =
        DomainResult.Success(INTERESTS)

    override suspend fun saveFilters(filters: SearchFilters): DomainResult<Unit> {
        dataStore.edit { prefs ->
            prefs[Keys.GENDER] = filters.gender.name
            prefs[Keys.AGE_MIN] = filters.ageRange.min
            prefs[Keys.AGE_MAX] = filters.ageRange.max
            if (filters.location != null) prefs[Keys.LOCATION] = filters.location
            else prefs.remove(Keys.LOCATION)
        }
        return DomainResult.Success(Unit)
    }

    override suspend fun getSavedFilters(): DomainResult<SearchFilters> {
        val prefs = dataStore.data.first()
        val gender = prefs[Keys.GENDER]?.let { name ->
            runCatching { Gender.valueOf(name) }.getOrDefault(Gender.ANY)
        } ?: Gender.ANY
        val ageMin = prefs[Keys.AGE_MIN] ?: 18
        val ageMax = prefs[Keys.AGE_MAX] ?: 99
        val location = prefs[Keys.LOCATION]
        return DomainResult.Success(
            SearchFilters(
                gender = gender,
                ageRange = AgeRange(min = ageMin, max = ageMax),
                location = location,
            )
        )
    }

    companion object {
        val INTERESTS = listOf(
            Interest("hiking", "Hiking"),
            Interest("coffee", "Coffee"),
            Interest("photography", "Photography"),
            Interest("yoga", "Yoga"),
            Interest("travel", "Travel"),
            Interest("music", "Music"),
            Interest("art", "Art"),
            Interest("cooking", "Cooking"),
            Interest("sports", "Sports"),
            Interest("reading", "Reading"),
        )
    }
}

private fun SearchProfileDto.toUserSummary(): UserSummary {
    val birthYear = birthDate?.let { dateStr ->
        runCatching {
            Instant.parse(dateStr).atZone(ZoneId.of("UTC")).year
        }.getOrNull()
    }
    val age = birthYear?.let { java.time.Year.now().value - it } ?: 0
    return UserSummary(
        id = id,
        name = displayName,
        age = age,
        avatarUrl = photoUrls.firstOrNull(),
        interests = interests.mapIndexed { i, name -> Interest(id = name, name = name) },
        location = listOfNotNull(city, country).joinToString(", ").ifEmpty { null },
    )
}
