package com.topit.meetup.feature.auth.domain.model

data class Credentials(
    val phone: String,
    val code: String,
)
