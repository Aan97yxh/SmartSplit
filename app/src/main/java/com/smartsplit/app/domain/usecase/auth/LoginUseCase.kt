package com.smartsplit.app.domain.usecase.auth

import com.smartsplit.app.domain.repository.UserRepository

class LoginUseCase(private val userRepository: UserRepository) {
    suspend operator fun invoke(email: String, password: String): Boolean {
        val user = userRepository.getUserByEmail(email)

        return if (user != null && user.password == password) {
            userRepository.userName = user.name
            userRepository.userEmail = user.email
            userRepository.userPhotoUri = user.photoUri
            userRepository.isLoggedIn = true
            true
        } else {
            false
        }
    }
}