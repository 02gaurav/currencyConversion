package com.gaurav.paypaydemo.stub

import com.gaurav.paypaydemo.datalayer.CountryCurrencyData
import com.gaurav.paypaydemo.datalayer.local.CurrencyExchangeRate
import com.gaurav.paypaydemo.datalayer.remote.ExchangeRates
import com.gaurav.paypaydemo.datalayer.remote.ExchangeRatesResponse
import com.gaurav.paypaydemo.ui.theme.uistate.CurrencyExchangeData
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody
import retrofit2.Response

fun getExchangeRates() = ExchangeRates(
    "USD", rates = CurrencyExchangeData(
        exchangeData = listOf(
            CountryCurrencyData("YEN", 0.77),
            CountryCurrencyData("INR", 83.0),
            CountryCurrencyData("AED", 3.673),
        ),
        timestamp = System.currentTimeMillis()
    )
)

fun getCurrencyExchangeDataDbStub(): List<CurrencyExchangeRate> =
    listOf(
        CurrencyExchangeRate("YEN", 0.77, System.currentTimeMillis(), 123456),
        CurrencyExchangeRate("INR", 83.0, System.currentTimeMillis(), 123456),
        CurrencyExchangeRate("AED", 3.673, System.currentTimeMillis(), 123456),
        CurrencyExchangeRate("USD", 1.0, System.currentTimeMillis(), 123456),
    )

fun getCountryListDataDbStub() = listOf("YEN", "INR", "AED", "USD")

// Creating a successful mock response
val exchangeRatesResponse = ExchangeRatesResponse(
    "USD",
    mapOf( "YEN" to 0.84,"INR" to 84.0, "AED" to 3.5, "USD" to 1.0, "AUD" to 1.5),
    System.currentTimeMillis()
)
val mockResponse = Response.success(exchangeRatesResponse)

val errorResponseBody = ResponseBody.create("application/json".toMediaTypeOrNull(), "{\"error\":\"Something went wrong\"}")

val mockResponseError = Response.error<ExchangeRatesResponse>(400, errorResponseBody)