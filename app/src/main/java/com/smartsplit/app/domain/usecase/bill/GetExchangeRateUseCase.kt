package com.smartsplit.app.domain.usecase.bill

import com.smartsplit.app.domain.repository.CurrencyRepository

class GetExchangeRateUseCase(private val repository: CurrencyRepository) {
    suspend operator fun invoke(fromCurrency: String): Double? {
        return repository.getExchangeRate(fromCurrency)
    }
}