package com.topit.meetup.core.common

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class DomainErrorTest {

    @Test
    fun `NetworkError carries message`() {
        val error = DomainError.NetworkError("no connection")
        assertEquals("no connection", error.message)
    }

    @Test
    fun `ServerError carries code and message`() {
        val error = DomainError.ServerError(404, "not found")
        assertEquals(404, error.code)
        assertEquals("not found", error.message)
    }

    @Test
    fun `UnknownError carries message`() {
        val error = DomainError.UnknownError("oops")
        assertEquals("oops", error.message)
    }

    @Test
    fun `NotFound has non-null message`() {
        assertTrue(DomainError.NotFound.message!!.isNotBlank())
    }

    @Test
    fun `Unauthorized has non-null message`() {
        assertTrue(DomainError.Unauthorized.message!!.isNotBlank())
    }

    @Test
    fun `DomainError is an Exception subtype`() {
        val error: Exception = DomainError.NetworkError("fail")
        assertTrue(error is Exception)
    }
}
