package com.topit.meetup.feature.search.domain.usecase

import com.topit.meetup.core.common.DomainResult
import com.topit.meetup.feature.search.domain.model.SearchFilters
import com.topit.meetup.feature.search.domain.repository.SearchRepository
import javax.inject.Inject

class SaveFiltersUseCase @Inject constructor(
    private val repository: SearchRepository,
) {
    suspend operator fun invoke(filters: SearchFilters): DomainResult<Unit> =
        repository.saveFilters(filters)
}
