package com.topit.meetup.feature.settings.domain.usecase

import com.topit.meetup.core.common.DomainResult
import com.topit.meetup.feature.settings.domain.model.AppSettings
import com.topit.meetup.feature.settings.domain.repository.SettingsRepository
import javax.inject.Inject

class UpdateSettingsUseCase @Inject constructor(
    private val repository: SettingsRepository,
) {
    suspend operator fun invoke(settings: AppSettings): DomainResult<Unit> =
        repository.updateSettings(settings)
}
