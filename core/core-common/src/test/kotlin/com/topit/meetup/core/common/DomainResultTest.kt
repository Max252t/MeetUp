package com.topit.meetup.core.common

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class DomainResultTest {

    @Test
    fun `onSuccess invokes action when Success`() {
        var called = false
        DomainResult.Success(42).onSuccess { called = true }
        assertTrue(called)
    }

    @Test
    fun `onSuccess skips action when Error`() {
        var called = false
        DomainResult.Error(DomainError.NotFound).onSuccess { called = true }
        assertFalse(called)
    }

    @Test
    fun `onSuccess passes correct value to action`() {
        var received: String? = null
        DomainResult.Success("hello").onSuccess { received = it }
        assertEquals("hello", received)
    }

    @Test
    fun `onFailure invokes action when Error`() {
        var called = false
        DomainResult.Error(DomainError.NotFound).onFailure { called = true }
        assertTrue(called)
    }

    @Test
    fun `onFailure skips action when Success`() {
        var called = false
        DomainResult.Success(1).onFailure { called = true }
        assertFalse(called)
    }

    @Test
    fun `onFailure passes correct error to action`() {
        val error = DomainError.NetworkError("timeout")
        var received: DomainError? = null
        DomainResult.Error(error).onFailure { received = it }
        assertEquals(error, received)
    }

    @Test
    fun `map transforms Success value`() {
        val result = DomainResult.Success(5).map { it * 2 }
        assertEquals(DomainResult.Success(10), result)
    }

    @Test
    fun `map preserves Error unchanged`() {
        val error = DomainError.Unauthorized
        val result: DomainResult<Int> = DomainResult.Error(error)
        val mapped = result.map { it * 2 }
        assertTrue(mapped is DomainResult.Error && mapped.error == error)
    }

    @Test
    fun `onSuccess returns original result for chaining`() {
        val original = DomainResult.Success(7)
        val returned = original.onSuccess { }
        assertEquals(original, returned)
    }

    @Test
    fun `onFailure returns original result for chaining`() {
        val original: DomainResult<Int> = DomainResult.Error(DomainError.NotFound)
        val returned = original.onFailure { }
        assertEquals(original, returned)
    }
}
