package com.topit.meetup.feature.settings.domain.model

data class AppSettings(
    val theme: AppTheme = AppTheme.SYSTEM,
    val language: AppLanguage = AppLanguage.SYSTEM,
    val notificationsEnabled: Boolean = true,
)
