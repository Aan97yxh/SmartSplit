package com.smartsplit.app.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.smartsplit.app.domain.repository.UserRepository
import com.smartsplit.app.domain.usecase.settings.GetThemePreferencesUseCase
import com.smartsplit.app.domain.usecase.settings.ManageLanguageUseCase
import com.smartsplit.app.domain.usecase.settings.ManageNotificationsUseCase

class SettingsViewModelFactory(private val userRepository: UserRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SettingsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")

            return SettingsViewModel(
                themeUseCase = GetThemePreferencesUseCase(userRepository),
                languageUseCase = ManageLanguageUseCase(userRepository),
                notificationsUseCase = ManageNotificationsUseCase(userRepository)
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel: ${modelClass.name}")
    }
}