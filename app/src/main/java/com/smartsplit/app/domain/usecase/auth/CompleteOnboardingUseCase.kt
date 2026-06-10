package com.smartsplit.app.domain.usecase.auth

import com.smartsplit.app.domain.repository.UserRepository

class CompleteOnboardingUseCase(private val userRepository: UserRepository) {
    operator fun invoke() {
        userRepository.hasSeenOnboarding = true
    }
}