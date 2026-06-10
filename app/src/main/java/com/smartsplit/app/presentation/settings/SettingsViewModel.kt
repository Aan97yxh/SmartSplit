package com.smartsplit.app.presentation.settings

import androidx.lifecycle.ViewModel
import com.smartsplit.app.domain.model.Language
import com.smartsplit.app.domain.usecase.settings.GetThemePreferencesUseCase
import com.smartsplit.app.domain.usecase.settings.ManageLanguageUseCase
import com.smartsplit.app.domain.usecase.settings.ManageNotificationsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import timber.log.Timber

class SettingsViewModel(
    private val themeUseCase: GetThemePreferencesUseCase,
    private val languageUseCase: ManageLanguageUseCase,
    private val notificationsUseCase: ManageNotificationsUseCase
) : ViewModel() {

    private val _isDarkMode = MutableStateFlow(themeUseCase.isDarkModeEnabled())
    val isDarkMode: StateFlow<Boolean> = _isDarkMode.asStateFlow()

    private val _language = MutableStateFlow(
        Language.values().find { it.code == languageUseCase.getLanguageCode() } ?: Language.INDONESIAN
    )
    val language: StateFlow<Language> = _language.asStateFlow()

    private val _notificationsEnabled = MutableStateFlow(notificationsUseCase.areNotificationsEnabled())
    val notificationsEnabled: StateFlow<Boolean> = _notificationsEnabled.asStateFlow()

    fun toggleDarkMode() {
        val new = !_isDarkMode.value
        _isDarkMode.value = new
        themeUseCase.setDarkMode(new)
        Timber.d("Dark mode: $new")
    }

    fun setLanguage(lang: Language) {
        _language.value = lang
        languageUseCase.setLanguageCode(lang.code)
        Timber.d("Language set: ${lang.code}")
    }

    fun toggleNotifications() {
        val new = !_notificationsEnabled.value
        _notificationsEnabled.value = new
        notificationsUseCase.setNotificationsEnabled(new)
        Timber.d("Notifications: $new")
    }
}