package com.topit.meetup.feature.settings.domain

import com.topit.meetup.core.common.DomainError
import com.topit.meetup.core.common.DomainResult
import com.topit.meetup.feature.settings.domain.model.AppLanguage
import com.topit.meetup.feature.settings.domain.model.AppSettings
import com.topit.meetup.feature.settings.domain.model.AppTheme
import com.topit.meetup.feature.settings.domain.repository.SettingsRepository
import com.topit.meetup.feature.settings.domain.usecase.GetSettingsUseCase
import com.topit.meetup.feature.settings.domain.usecase.UpdateSettingsUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class SettingsUseCasesTest {

    private val defaultSettings = AppSettings(
        theme = AppTheme.DARK,
        language = AppLanguage.RU,
        notificationsEnabled = true,
    )

    // --- GetSettingsUseCase ---

    @Test
    fun `GetSettingsUseCase emits settings from repository`() = runTest {
        val repo = object : FakeSettingsRepository() {
            override fun getSettings(): Flow<AppSettings> = flowOf(defaultSettings)
        }
        val result = GetSettingsUseCase(repo)().first()
        assertEquals(defaultSettings, result)
    }

    @Test
    fun `GetSettingsUseCase emits default settings`() = runTest {
        val defaults = AppSettings()
        val repo = object : FakeSettingsRepository() {
            override fun getSettings(): Flow<AppSettings> = flowOf(defaults)
        }
        val result = GetSettingsUseCase(repo)().first()
        assertEquals(defaults, result)
    }

    // --- UpdateSettingsUseCase ---

    @Test
    fun `UpdateSettingsUseCase returns Success from repository`() = runTest {
        val repo = object : FakeSettingsRepository() {
            override suspend fun updateSettings(settings: AppSettings) = DomainResult.Success(Unit)
        }
        val result = UpdateSettingsUseCase(repo)(defaultSettings)
        assertEquals(DomainResult.Success(Unit), result)
    }

    @Test
    fun `UpdateSettingsUseCase propagates Error from repository`() = runTest {
        val error = DomainError.UnknownError("disk full")
        val repo = object : FakeSettingsRepository() {
            override suspend fun updateSettings(settings: AppSettings): DomainResult<Unit> =
                DomainResult.Error(error)
        }
        val result = UpdateSettingsUseCase(repo)(defaultSettings)
        assertEquals(DomainResult.Error(error), result)
    }

    @Test
    fun `UpdateSettingsUseCase passes correct settings to repository`() = runTest {
        var received: AppSettings? = null
        val repo = object : FakeSettingsRepository() {
            override suspend fun updateSettings(settings: AppSettings): DomainResult<Unit> {
                received = settings
                return DomainResult.Success(Unit)
            }
        }
        UpdateSettingsUseCase(repo)(defaultSettings)
        assertEquals(defaultSettings, received)
    }
}

private abstract class FakeSettingsRepository : SettingsRepository {
    override fun getSettings(): Flow<AppSettings> = flowOf(AppSettings())
    override suspend fun updateSettings(settings: AppSettings): DomainResult<Unit> = DomainResult.Success(Unit)
}
