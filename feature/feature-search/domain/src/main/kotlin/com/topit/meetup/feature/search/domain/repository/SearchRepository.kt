package com.topit.meetup.feature.search.domain.repository

import com.topit.meetup.core.common.DomainResult
import com.topit.meetup.feature.search.domain.model.Interest
import com.topit.meetup.feature.search.domain.model.SearchFilters
import com.topit.meetup.feature.search.domain.model.UserSummary

interface SearchRepository {
    suspend fun searchUsers(query: String, filters: SearchFilters): DomainResult<List<UserSummary>>
    suspend fun getInterests(): DomainResult<List<Interest>>
    suspend fun saveFilters(filters: SearchFilters): DomainResult<Unit>
    suspend fun getSavedFilters(): DomainResult<SearchFilters>
}
