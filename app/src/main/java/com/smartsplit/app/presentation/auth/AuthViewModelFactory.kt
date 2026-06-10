package com.smartsplit.app.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.smartsplit.app.domain.repository.UserRepository
import com.smartsplit.app.domain.usecase.auth.*
import com.smartsplit.app.domain.usecase.settings.GetUserProfileUseCase
import com.smartsplit.app.domain.usecase.settings.UpdateProfileUseCase

class AuthViewModelFactory(private val userRepository: UserRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AuthViewModel(
                checkInitialStateUseCase = CheckInitialStateUseCase(userRepository),
                getUserProfileUseCase = GetUserProfileUseCase(userRepository),
                loginUseCase = LoginUseCase(userRepository),
                registerUseCase = RegisterUseCase(userRepository),
                logoutUseCase = LogoutUseCase(userRepository),
                completeOnboardingUseCase = CompleteOnboardingUseCase(userRepository),
                updateProfileUseCase = UpdateProfileUseCase(userRepository)
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel: ${modelClass.name}")
    }
}