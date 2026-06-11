package com.smartsplit.app.domain.usecase.auth

import com.smartsplit.app.domain.model.User
import com.smartsplit.app.domain.repository.UserRepository

class RegisterUseCase(private val userRepository: UserRepository) {
    suspend operator fun invoke(name: String, email: String, password: String) {
        val newUser = User(
            email = email,
            name = name,
            password = password
        )

        userRepository.registerUser(newUser)
        userRepository.userName = name
        userRepository.userEmail = email
        userRepository.userPhotoUri = null
        userRepository.isLoggedIn = true
    }
}