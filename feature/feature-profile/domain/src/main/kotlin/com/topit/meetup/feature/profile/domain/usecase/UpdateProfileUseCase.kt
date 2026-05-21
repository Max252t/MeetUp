package com.topit.meetup.feature.profile.domain.usecase

import com.topit.meetup.core.common.DomainResult
import com.topit.meetup.feature.profile.domain.model.User
import com.topit.meetup.feature.profile.domain.repository.ProfileRepository
import javax.inject.Inject

class UpdateProfileUseCase @Inject constructor(
    private val repository: ProfileRepository,
) {
    suspend operator fun invoke(user: User): DomainResult<Unit> =
        repository.updateProfile(user)
}
