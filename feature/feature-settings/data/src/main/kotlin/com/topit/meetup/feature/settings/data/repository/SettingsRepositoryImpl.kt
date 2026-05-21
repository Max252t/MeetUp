package com.topit.meetup.feature.settings.data.repository

import com.topit.meetup.core.common.DomainResult
import com.topit.meetup.feature.settings.domain.model.AppSettings
import com.topit.meetup.feature.settings.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SettingsRepositoryImpl @Inject constructor() : SettingsRepository {

    private val _settings = MutableStateFlow(AppSettings())

    override fun getSettings(): Flow<AppSettings> = _settings

    override suspend fun updateSettings(settings: AppSettings): DomainResult<Unit> {
        _settings.update { settings }
        return DomainResult.Success(Unit)
    }
}
