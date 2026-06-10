package com.smartsplit.app.domain.usecase.settings

import com.smartsplit.app.domain.repository.UserRepository
import com.smartsplit.app.domain.model.UserProfile

class GetUserProfileUseCase(private val userRepository: UserRepository) {
    operator fun invoke(): UserProfile {
        return UserProfile(
            name = userRepository.userName,
            email = userRepository.userEmail,
            photoUri = userRepository.userPhotoUri
        )
    }
}