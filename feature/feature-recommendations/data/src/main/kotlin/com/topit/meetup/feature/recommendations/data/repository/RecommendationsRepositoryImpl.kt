package com.topit.meetup.feature.recommendations.data.repository

import com.topit.meetup.core.common.DomainResult
import com.topit.meetup.core.network.safeApiCall
import com.topit.meetup.feature.recommendations.data.remote.RecommendedProfileApi
import com.topit.meetup.feature.recommendations.data.remote.RecommendationsApi
import com.topit.meetup.feature.recommendations.data.remote.dto.RecommendedProfileDto
import com.topit.meetup.feature.recommendations.domain.model.ReactionType
import com.topit.meetup.feature.recommendations.domain.model.RecommendedUser
import com.topit.meetup.feature.recommendations.domain.repository.RecommendationsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.Year
import java.time.ZoneId
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RecommendationsRepositoryImpl @Inject constructor(
    private val recommendationsApi: RecommendationsApi,
    private val profileApi: RecommendedProfileApi,
) : RecommendationsRepository {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private val _recommendations = MutableStateFlow<List<RecommendedUser>>(emptyList())

    init {
        scope.launch { refresh() }
    }

    private suspend fun refresh() {
        val userIdsResult = safeApiCall { recommendationsApi.getRecommendations() }
        if (userIdsResult !is DomainResult.Success) return

        val users = userIdsResult.data.mapNotNull { userId ->
            val profileResult = safeApiCall { profileApi.getProfile(userId) }
            (profileResult as? DomainResult.Success)?.data?.toRecommendedUser()
        }
        _recommendations.value = users
    }

    override fun getRecommendations(): Flow<List<RecommendedUser>> = _recommendations

    override suspend fun reactToUser(userId: String, reaction: ReactionType): DomainResult<Unit> {
        // No react endpoint in the API — remove from local state only
        _recommendations.update { list -> list.filter { it.id != userId } }
        return DomainResult.Success(Unit)
    }
}

private fun RecommendedProfileDto.toRecommendedUser(): RecommendedUser {
    val birthYear = birthDate?.let { dateStr ->
        runCatching {
            Instant.parse(dateStr).atZone(ZoneId.of("UTC")).year
        }.getOrNull()
    }
    val age = birthYear?.let { Year.now().value - it } ?: 0
    return RecommendedUser(
        id = id,
        name = displayName,
        age = age,
        bio = bio ?: "",
        avatarUrl = photoUrls.firstOrNull(),
        interests = interests,
        location = listOfNotNull(city, country).joinToString(", ").ifEmpty { null },
        matchScore = 0f,
    )
}
