package com.smartsplit.app.domain.usecase.auth

import com.smartsplit.app.domain.repository.UserRepository

class CheckInitialStateUseCase(private val userRepository: UserRepository) {
    operator fun invoke(): InitialState {
        return when {
            !userRepository.hasSeenOnboarding -> InitialState.ONBOARDING
            userRepository.isLoggedIn -> InitialState.HOME
            else -> InitialState.LOGIN
        }
    }
}

enum class InitialState {
    ONBOARDING, LOGIN, HOME
}