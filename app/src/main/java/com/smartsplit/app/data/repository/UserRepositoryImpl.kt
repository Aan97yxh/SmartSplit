package com.smartsplit.app.data.repository

import com.smartsplit.app.data.local.dao.UserDao
import com.smartsplit.app.data.preferences.AppPreferences
import com.smartsplit.app.domain.model.User
import com.smartsplit.app.domain.repository.UserRepository

class UserRepositoryImpl(
    private val prefs: AppPreferences,
    private val userDao: UserDao
) : UserRepository {

    override var isLoggedIn: Boolean
        get() = prefs.isLoggedIn
        set(value) { prefs.isLoggedIn = value }

    override var hasSeenOnboarding: Boolean
        get() = prefs.hasSeenOnboarding
        set(value) { prefs.hasSeenOnboarding = value }

    override var userName: String
        get() = prefs.userName
        set(value) { prefs.userName = value }

    override var userEmail: String
        get() = prefs.userEmail
        set(value) { prefs.userEmail = value }

    override var userPhotoUri: String?
        get() = prefs.userPhotoUri
        set(value) { prefs.userPhotoUri = value }

    override var isDarkMode: Boolean
        get() = prefs.isDarkMode
        set(value) { prefs.isDarkMode = value }

    override var languageCode: String
        get() = prefs.languageCode
        set(value) { prefs.languageCode = value }

    override var notificationsEnabled: Boolean
        get() = prefs.notificationsEnabled
        set(value) { prefs.notificationsEnabled = value }

    override suspend fun registerUser(user: User) {
        userDao.insertUser(user)
    }

    override suspend fun getUserByEmail(email: String): User? {
        return userDao.getUserByEmail(email)
    }
}