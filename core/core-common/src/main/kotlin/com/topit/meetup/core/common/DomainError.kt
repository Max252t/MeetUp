package com.topit.meetup.core.common

sealed class DomainError(message: String) : Exception(message) {
    class NetworkError(message: String) : DomainError(message)
    class ServerError(val code: Int, message: String) : DomainError(message)
    class UnknownError(message: String) : DomainError(message)
    data object NotFound : DomainError("Not found")
    data object Unauthorized : DomainError("Unauthorized")
}
