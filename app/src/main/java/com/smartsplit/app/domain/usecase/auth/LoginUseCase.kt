package com.smartsplit.app.domain.usecase.auth

import com.smartsplit.app.domain.repository.UserRepository

class LoginUseCase(private val userRepository: UserRepository) {
    operator fun invoke(email: String, password: String): Boolean {
        return if (email == userRepository.userEmail && password == userRepository.userPassword) {
            userRepository.isLoggedIn = true
            true
        } else {
            false
        }
    }
}