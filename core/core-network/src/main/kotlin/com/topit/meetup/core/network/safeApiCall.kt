package com.topit.meetup.core.network

import com.topit.meetup.core.common.DomainError
import com.topit.meetup.core.common.DomainResult
import retrofit2.HttpException
import java.io.IOException

suspend fun <T> safeApiCall(call: suspend () -> T): DomainResult<T> = try {
    DomainResult.Success(call())
} catch (e: HttpException) {
    when (e.code()) {
        401 -> DomainResult.Error(DomainError.Unauthorized)
        404 -> DomainResult.Error(DomainError.NotFound)
        else -> DomainResult.Error(DomainError.ServerError(e.code(), e.message ?: "Server error"))
    }
} catch (e: IOException) {
    DomainResult.Error(DomainError.NetworkError(e.message ?: "Network error"))
} catch (e: Exception) {
    DomainResult.Error(DomainError.UnknownError(e.message ?: "Unknown error"))
}
