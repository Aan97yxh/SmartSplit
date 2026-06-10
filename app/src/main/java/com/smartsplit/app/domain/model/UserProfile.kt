package com.smartsplit.app.domain.model

data class UserProfile(
    val name: String      = "User",
    val email: String     = "",
    val photoUri: String? = null
)