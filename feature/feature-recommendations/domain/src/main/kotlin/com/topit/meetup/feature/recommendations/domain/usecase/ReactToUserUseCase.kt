package com.topit.meetup.feature.recommendations.domain.usecase

import com.topit.meetup.core.common.DomainResult
import com.topit.meetup.feature.recommendations.domain.model.ReactionType
import com.topit.meetup.feature.recommendations.domain.repository.RecommendationsRepository
import javax.inject.Inject

class ReactToUserUseCase @Inject constructor(
    private val repository: RecommendationsRepository,
) {
    suspend operator fun invoke(userId: String, reaction: ReactionType): DomainResult<Unit> =
        repository.reactToUser(userId, reaction)
}
