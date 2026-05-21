package com.topit.meetup.feature.search.domain.model

data class SearchFilters(
    val gender: Gender = Gender.ANY,
    val ageRange: AgeRange = AgeRange(),
    val interests: List<Interest> = emptyList(),
    val location: String? = null,
)
