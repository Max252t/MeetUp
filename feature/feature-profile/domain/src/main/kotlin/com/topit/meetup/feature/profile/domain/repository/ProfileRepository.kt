package com.topit.meetup.feature.profile.domain.repository

import com.topit.meetup.core.common.DomainResult
import com.topit.meetup.feature.profile.domain.model.Interest
import com.topit.meetup.feature.profile.domain.model.User
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {
    fun getProfile(): Flow<User?>
    suspend fun updateProfile(user: User): DomainResult<Unit>
    suspend fun getAvailableInterests(): DomainResult<List<Interest>>
    suspend fun uploadAvatar(imageBytes: ByteArray): DomainResult<String>
}
