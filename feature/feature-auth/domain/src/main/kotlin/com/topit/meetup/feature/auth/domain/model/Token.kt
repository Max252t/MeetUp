package com.topit.meetup.feature.auth.domain.model

data class Token(
    val access: String,
    val refresh: String,
)
