package com.topit.meetup.feature.search.domain.usecase

import com.topit.meetup.core.common.DomainResult
import com.topit.meetup.feature.search.domain.model.Interest
import com.topit.meetup.feature.search.domain.repository.SearchRepository
import javax.inject.Inject

class GetInterestsUseCase @Inject constructor(
    private val repository: SearchRepository,
) {
    suspend operator fun invoke(): DomainResult<List<Interest>> =
        repository.getInterests()
}
