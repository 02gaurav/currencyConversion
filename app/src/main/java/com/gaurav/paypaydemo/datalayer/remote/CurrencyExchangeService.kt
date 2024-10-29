package com.gaurav.paypaydemo.datalayer.remote

import com.gaurav.paypaydemo.BuildConfig
import com.gaurav.paypaydemo.ui.theme.uistate.CurrencyExchangeData
import com.google.gson.annotations.SerializedName
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface CurrencyExchangeService {
    @GET("latest.json")
    suspend fun getExchangeRatesLive(
        @Query("app_id") apiKey: String = BuildConfig.API_KEY,
        @Query("base") base: String = "USD",
    ): Response<ExchangeRatesResponse>
}

data class ExchangeRatesResponse(
    @SerializedName("base") val base: String,
    @SerializedName("rates") val rates: Map<String, Double>,
    @SerializedName("timestamp") val timestamp: Long,
)

data class ExchangeRates(
    var base: String?=null,
    var rates: CurrencyExchangeData?= null
)