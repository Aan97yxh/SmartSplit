package com.smartsplit.app.data.remote.api

import com.smartsplit.app.data.remote.model.CurrencyResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface CurrencyApiService {
    @GET("latest")
    suspend fun getLatestRates(
        @Query("from") from: String,
        @Query("to") to: String
    ): CurrencyResponse
}