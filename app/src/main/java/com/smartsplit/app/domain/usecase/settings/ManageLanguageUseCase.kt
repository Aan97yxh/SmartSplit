package com.smartsplit.app.domain.usecase.settings

import com.smartsplit.app.domain.repository.UserRepository

class ManageLanguageUseCase(private val userRepository: UserRepository) {
    fun getLanguageCode(): String = userRepository.languageCode

    fun setLanguageCode(code: String) {
        userRepository.languageCode = code
    }
}