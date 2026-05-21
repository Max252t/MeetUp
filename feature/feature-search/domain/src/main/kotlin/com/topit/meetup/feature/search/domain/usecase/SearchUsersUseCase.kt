package com.topit.meetup.feature.search.domain.usecase

import com.topit.meetup.core.common.DomainResult
import com.topit.meetup.feature.search.domain.model.SearchFilters
import com.topit.meetup.feature.search.domain.model.UserSummary
import com.topit.meetup.feature.search.domain.repository.SearchRepository
import javax.inject.Inject

class SearchUsersUseCase @Inject constructor(
    private val repository: SearchRepository,
) {
    suspend operator fun invoke(query: String, filters: SearchFilters): DomainResult<List<UserSummary>> =
        repository.searchUsers(query, filters)
}
