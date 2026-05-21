package com.topit.meetup.feature.recommendations.domain.usecase

import com.topit.meetup.feature.recommendations.domain.model.RecommendedUser
import com.topit.meetup.feature.recommendations.domain.repository.RecommendationsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetRecommendationsUseCase @Inject constructor(
    private val repository: RecommendationsRepository,
) {
    operator fun invoke(): Flow<List<RecommendedUser>> = repository.getRecommendations()
}
