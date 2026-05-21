package com.topit.meetup.feature.auth.domain.usecase

import com.topit.meetup.feature.auth.domain.model.AuthState
import com.topit.meetup.feature.auth.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAuthStateUseCase @Inject constructor(
    private val repository: AuthRepository,
) {
    operator fun invoke(): Flow<AuthState> = repository.getAuthState()
}
