package com.smartsplit.app.presentation.auth

import androidx.lifecycle.ViewModel
import com.smartsplit.app.domain.model.UserProfile
import com.smartsplit.app.domain.usecase.auth.*
import com.smartsplit.app.domain.usecase.settings.GetUserProfileUseCase
import com.smartsplit.app.domain.usecase.settings.UpdateProfileUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import timber.log.Timber

class AuthViewModel(
    private val checkInitialStateUseCase: CheckInitialStateUseCase,
    private val getUserProfileUseCase: GetUserProfileUseCase,
    private val loginUseCase: LoginUseCase,
    private val registerUseCase: RegisterUseCase,
    private val logoutUseCase: LogoutUseCase,
    private val completeOnboardingUseCase: CompleteOnboardingUseCase,
    private val updateProfileUseCase: UpdateProfileUseCase
) : ViewModel() {

    private val initialState = checkInitialStateUseCase()

    private val _isLoggedIn = MutableStateFlow(initialState == InitialState.HOME)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()
    private val _hasSeenOnboarding = MutableStateFlow(initialState != InitialState.ONBOARDING)
    val hasSeenOnboarding: StateFlow<Boolean> = _hasSeenOnboarding.asStateFlow()
    private val _user = MutableStateFlow(getUserProfileUseCase())
    val user: StateFlow<UserProfile> = _user.asStateFlow()

    suspend fun login(email: String, password: String): Boolean {
        val success = loginUseCase(email, password)

        if (success) {
            _user.value = getUserProfileUseCase()
            _isLoggedIn.value = true
            Timber.d("Login success: ${_user.value.email}")
        } else {
            Timber.w("Login failed for: $email")
        }
        return success
    }

    suspend fun register(name: String, email: String, password: String): Boolean {
        if (name.isBlank() || email.isBlank() || password.length < 6) return false

        registerUseCase(name, email, password)
        _user.value = getUserProfileUseCase()
        _isLoggedIn.value = true

        Timber.d("Register success: $email")
        return true
    }

    fun logout() {
        logoutUseCase()
        _isLoggedIn.value = false
        _user.value = UserProfile()
        Timber.d("User logged out")
    }

    fun markOnboardingDone() {
        completeOnboardingUseCase()
        _hasSeenOnboarding.value = true
    }

    fun updateUser(profile: UserProfile) {
        updateProfileUseCase(profile.name, profile.photoUri)
        _user.value = getUserProfileUseCase()
        Timber.d("User profile updated: ${profile.name}")
    }
}