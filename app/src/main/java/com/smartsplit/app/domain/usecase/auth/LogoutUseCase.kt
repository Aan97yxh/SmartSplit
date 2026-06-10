package com.smartsplit.app.domain.usecase.auth

import com.smartsplit.app.domain.repository.UserRepository

class LogoutUseCase(private val userRepository: UserRepository) {
    operator fun invoke() {
        userRepository.isLoggedIn = false
    }
}