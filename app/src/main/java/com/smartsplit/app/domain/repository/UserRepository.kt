package com.smartsplit.app.domain.repository

import com.smartsplit.app.domain.model.User

interface UserRepository {
    var hasSeenOnboarding: Boolean
    var isDarkMode: Boolean
    var languageCode: String
    var notificationsEnabled: Boolean

    var isLoggedIn: Boolean
    var userEmail: String
    var userName: String
    var userPhotoUri: String?

    suspend fun registerUser(user: User)
    suspend fun getUserByEmail(email: String): User?
}