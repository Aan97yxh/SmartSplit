package com.smartsplit.app.data.repository

import com.smartsplit.app.data.remote.api.CurrencyApiService
import com.smartsplit.app.domain.repository.CurrencyRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber

class CurrencyRepositoryImpl(private val apiService: CurrencyApiService) : CurrencyRepository {
    override suspend fun getExchangeRate(fromCurrency: String): Double? {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getLatestRates(from = fromCurrency, to = "IDR")
                val rate = response.rates["IDR"]
                Timber.d("Kurs didapat: 1 $fromCurrency = Rp $rate")
                rate
            } catch (e: Exception) {
                Timber.e(e, "Gagal mengambil data kurs dari API")
                null
            }
        }
    }
}