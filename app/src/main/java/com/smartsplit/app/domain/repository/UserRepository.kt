package com.smartsplit.app.domain.repository

import kotlinx.coroutines.flow.Flow

interface UserRepository {
    var isLoggedIn: Boolean
    var hasSeenOnboarding: Boolean
    var userName: String
    var userEmail: String
    var userPassword: String
    var userPhotoUri: String?
    var isDarkMode: Boolean
    var languageCode: String
    var notificationsEnabled: Boolean
}