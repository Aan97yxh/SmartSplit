package com.smartsplit.app.domain.usecase.settings

import com.smartsplit.app.domain.repository.UserRepository

class ManageNotificationsUseCase(private val userRepository: UserRepository) {
    fun areNotificationsEnabled(): Boolean = userRepository.notificationsEnabled

    fun setNotificationsEnabled(enabled: Boolean) {
        userRepository.notificationsEnabled = enabled
    }
}