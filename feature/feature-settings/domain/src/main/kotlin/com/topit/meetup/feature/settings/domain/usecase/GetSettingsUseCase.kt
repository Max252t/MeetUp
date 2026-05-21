package com.topit.meetup.feature.settings.domain.usecase

import com.topit.meetup.feature.settings.domain.model.AppSettings
import com.topit.meetup.feature.settings.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetSettingsUseCase @Inject constructor(
    private val repository: SettingsRepository,
) {
    operator fun invoke(): Flow<AppSettings> = repository.getSettings()
}
