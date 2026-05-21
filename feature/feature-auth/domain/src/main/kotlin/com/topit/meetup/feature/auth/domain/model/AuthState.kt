package com.topit.meetup.feature.auth.domain.model

sealed interface AuthState {
    data class Authenticated(val userId: String) : AuthState
    data object Unauthenticated : AuthState
    data object Loading : AuthState
}
