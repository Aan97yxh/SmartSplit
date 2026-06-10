package com.smartsplit.app.data.preferences

import android.content.Context

class AppPreferences(context: Context) {
    private val prefs = context.getSharedPreferences("smartsplit_prefs", Context.MODE_PRIVATE)

    var isLoggedIn: Boolean
        get() = prefs.getBoolean(KEY_LOGGED_IN, false)
        set(v) = prefs.edit().putBoolean(KEY_LOGGED_IN, v).apply()

    var hasSeenOnboarding: Boolean
        get() = prefs.getBoolean(KEY_ONBOARDING, false)
        set(v) = prefs.edit().putBoolean(KEY_ONBOARDING, v).apply()

    var userName: String
        get() = prefs.getString(KEY_NAME, "User") ?: "User"
        set(v) = prefs.edit().putString(KEY_NAME, v).apply()

    var userEmail: String
        get() = prefs.getString(KEY_EMAIL, "") ?: ""
        set(v) = prefs.edit().putString(KEY_EMAIL, v).apply()

    var userPhotoUri: String?
        get() = prefs.getString(KEY_PHOTO, null)
        set(v) = prefs.edit().putString(KEY_PHOTO, v).apply()

    var isDarkMode: Boolean
        get() = prefs.getBoolean(KEY_DARK_MODE, false)
        set(v) = prefs.edit().putBoolean(KEY_DARK_MODE, v).apply()

    var languageCode: String
        get() = prefs.getString(KEY_LANGUAGE, "id") ?: "id"
        set(v) = prefs.edit().putString(KEY_LANGUAGE, v).apply()

    var notificationsEnabled: Boolean
        get() = prefs.getBoolean(KEY_NOTIF, true)
        set(v) = prefs.edit().putBoolean(KEY_NOTIF, v).apply()

    var userPassword: String
        get() = prefs.getString(KEY_PASSWORD, "") ?: ""
        set(value) = prefs.edit().putString(KEY_PASSWORD, value).apply()

    companion object {
        private const val KEY_LOGGED_IN  = "is_logged_in"
        private const val KEY_ONBOARDING = "has_seen_onboarding"
        private const val KEY_NAME       = "user_name"
        private const val KEY_EMAIL      = "user_email"
        private const val KEY_PHOTO      = "user_photo"
        private const val KEY_DARK_MODE  = "dark_mode"
        private const val KEY_LANGUAGE   = "language"
        private const val KEY_NOTIF      = "notifications"
        private const val KEY_PASSWORD   = "user_password"
    }
}