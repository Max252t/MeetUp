package com.topit.meetup.feature.profile.data.repository

import com.topit.meetup.core.common.DomainResult
import com.topit.meetup.feature.profile.domain.model.Interest
import com.topit.meetup.feature.profile.domain.model.User
import com.topit.meetup.feature.profile.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProfileRepositoryImpl @Inject constructor() : ProfileRepository {

    private val _profile = MutableStateFlow<User?>(null)

    override fun getProfile(): Flow<User?> = _profile

    override suspend fun updateProfile(user: User): DomainResult<Unit> {
        _profile.update { user }
        return DomainResult.Success(Unit)
    }

    override suspend fun getAvailableInterests(): DomainResult<List<Interest>> =
        DomainResult.Success(INTERESTS)

    override suspend fun uploadAvatar(imageBytes: ByteArray): DomainResult<String> =
        DomainResult.Success("https://cdn.meetup.app/avatars/placeholder.jpg")

    companion object {
        val INTERESTS = listOf(
            Interest("1", "Hiking"),
            Interest("2", "Coffee"),
            Interest("3", "Photography"),
            Interest("4", "Yoga"),
            Interest("5", "Travel"),
            Interest("6", "Music"),
            Interest("7", "Art"),
            Interest("8", "Cooking"),
            Interest("9", "Sports"),
            Interest("10", "Reading"),
        )
    }
}
