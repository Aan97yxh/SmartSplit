package com.smartsplit.app.domain.repository

interface CurrencyRepository {
    suspend fun getExchangeRate(fromCurrency: String): Double?
}