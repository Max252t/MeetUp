package com.topit.meetup.feature.profile.domain.usecase

import com.topit.meetup.feature.profile.domain.model.User
import com.topit.meetup.feature.profile.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetProfileUseCase @Inject constructor(
    private val repository: ProfileRepository,
) {
    operator fun invoke(): Flow<User?> = repository.getProfile()
}
