package com.smartsplit.app.domain.usecase.settings

import com.smartsplit.app.domain.repository.UserRepository

class UpdateProfileUseCase(private val userRepository: UserRepository) {
    operator fun invoke(newName: String, newPhotoUri: String?) {
        userRepository.userName = newName
        userRepository.userPhotoUri = newPhotoUri
    }
}