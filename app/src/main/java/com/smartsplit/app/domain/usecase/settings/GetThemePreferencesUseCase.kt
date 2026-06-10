package com.smartsplit.app.domain.usecase.settings

import com.smartsplit.app.domain.repository.UserRepository

class GetThemePreferencesUseCase(private val userRepository: UserRepository) {
    fun isDarkModeEnabled(): Boolean = userRepository.isDarkMode

    fun setDarkMode(enabled: Boolean) {
        userRepository.isDarkMode = enabled
    }
}