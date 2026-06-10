package com.smartsplit.app.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class CurrencyResponse(
    val amount: Double,
    val base: String,
    val date: String,
    val rates: Map<String, Double>
)