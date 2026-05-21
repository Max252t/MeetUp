package com.topit.meetup.feature.auth.domain.usecase

import com.topit.meetup.core.common.DomainResult
import com.topit.meetup.feature.auth.domain.model.Credentials
import com.topit.meetup.feature.auth.domain.repository.AuthRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val repository: AuthRepository,
) {
    suspend operator fun invoke(credentials: Credentials): DomainResult<Unit> =
        repository.login(credentials)
}
