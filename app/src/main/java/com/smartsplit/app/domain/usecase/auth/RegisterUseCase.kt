package com.smartsplit.app.domain.usecase.auth

import com.smartsplit.app.domain.repository.UserRepository

class RegisterUseCase(private val userRepository: UserRepository) {
    operator fun invoke(name: String, email: String, password: String) {
        userRepository.userName = name
        userRepository.userEmail = email
        userRepository.userPassword = password
        userRepository.isLoggedIn = true
    }
}