package com.topit.meetup.feature.recommendations.domain.repository

import com.topit.meetup.core.common.DomainResult
import com.topit.meetup.feature.recommendations.domain.model.ReactionType
import com.topit.meetup.feature.recommendations.domain.model.RecommendedUser
import kotlinx.coroutines.flow.Flow

interface RecommendationsRepository {
    fun getRecommendations(): Flow<List<RecommendedUser>>
    suspend fun reactToUser(userId: String, reaction: ReactionType): DomainResult<Unit>
}
