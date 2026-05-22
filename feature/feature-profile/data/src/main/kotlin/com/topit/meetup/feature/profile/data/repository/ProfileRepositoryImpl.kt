package com.topit.meetup.feature.profile.data.repository

import com.topit.meetup.core.common.DomainError
import com.topit.meetup.core.common.DomainResult
import com.topit.meetup.core.network.safeApiCall
import com.topit.meetup.feature.profile.data.mapper.toDomain
import com.topit.meetup.feature.profile.data.mapper.toUpsertDto
import com.topit.meetup.feature.profile.data.remote.MediaApi
import com.topit.meetup.feature.profile.data.remote.ProfileApi
import com.topit.meetup.feature.profile.data.remote.dto.RequestUploadUrlRequestDto
import com.topit.meetup.feature.profile.domain.model.Interest
import com.topit.meetup.feature.profile.domain.model.User
import com.topit.meetup.feature.profile.domain.repository.ProfileRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProfileRepositoryImpl @Inject constructor(
    private val profileApi: ProfileApi,
    private val mediaApi: MediaApi,
) : ProfileRepository {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private val _profile = MutableStateFlow<User?>(null)

    init {
        scope.launch { loadProfile() }
    }

    private suspend fun loadProfile() {
        safeApiCall { profileApi.getMyProfile() }
            .onSuccess { dto -> _profile.value = dto.toDomain() }
    }

    override fun getProfile(): Flow<User?> = _profile

    override suspend fun updateProfile(user: User): DomainResult<Unit> {
        val result = safeApiCall { profileApi.upsertProfile(user.toUpsertDto()) }
        result.onSuccess { dto -> _profile.update { dto.toDomain() } }
        return result.map { }
    }

    override suspend fun getAvailableInterests(): DomainResult<List<Interest>> =
        DomainResult.Success(INTERESTS)

    override suspend fun uploadAvatar(imageBytes: ByteArray): DomainResult<String> {
        val urlResult = safeApiCall {
            mediaApi.requestUploadUrl(
                RequestUploadUrlRequestDto(
                    mimeType = "image/jpeg",
                    sizeBytes = imageBytes.size.toLong(),
                )
            )
        }
        if (urlResult is DomainResult.Error) return urlResult

        val uploadUrlResponse = (urlResult as DomainResult.Success).data
        val s3PutResult = withContext(Dispatchers.IO) {
            runCatching {
                val client = OkHttpClient()
                val request = Request.Builder()
                    .url(uploadUrlResponse.uploadUrl)
                    .put(imageBytes.toRequestBody("image/jpeg".toMediaType()))
                    .build()
                client.newCall(request).execute().use { response ->
                    if (!response.isSuccessful) throw IOException("S3 PUT failed: ${response.code}")
                }
            }
        }
        if (s3PutResult.isFailure) {
            return DomainResult.Error(
                DomainError.NetworkError(s3PutResult.exceptionOrNull()?.message ?: "Upload failed")
            )
        }

        val confirmResult = safeApiCall { mediaApi.confirmUpload(uploadUrlResponse.mediaId) }
        if (confirmResult is DomainResult.Error) return confirmResult

        val mediaUrl = (confirmResult as DomainResult.Success).data.url
            ?: return DomainResult.Error(DomainError.ServerError(0, "No URL returned after upload"))

        return DomainResult.Success(mediaUrl)
    }

    companion object {
        val INTERESTS = listOf(
            Interest("hiking", "Hiking"),
            Interest("coffee", "Coffee"),
            Interest("photography", "Photography"),
            Interest("yoga", "Yoga"),
            Interest("travel", "Travel"),
            Interest("music", "Music"),
            Interest("art", "Art"),
            Interest("cooking", "Cooking"),
            Interest("sports", "Sports"),
            Interest("reading", "Reading"),
        )
    }
}
