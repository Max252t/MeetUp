package com.topit.meetup.feature.auth.domain.usecase

import com.topit.meetup.core.common.DomainResult
import com.topit.meetup.feature.auth.domain.repository.AuthRepository
import javax.inject.Inject

class LogoutUseCase @Inject constructor(
    private val repository: AuthRepository,
) {
    suspend operator fun invoke(): DomainResult<Unit> = repository.logout()
}
