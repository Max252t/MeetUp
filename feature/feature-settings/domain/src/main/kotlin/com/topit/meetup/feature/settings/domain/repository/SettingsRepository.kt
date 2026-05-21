package com.topit.meetup.feature.settings.domain.repository

import com.topit.meetup.core.common.DomainResult
import com.topit.meetup.feature.settings.domain.model.AppSettings
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    fun getSettings(): Flow<AppSettings>
    suspend fun updateSettings(settings: AppSettings): DomainResult<Unit>
}
