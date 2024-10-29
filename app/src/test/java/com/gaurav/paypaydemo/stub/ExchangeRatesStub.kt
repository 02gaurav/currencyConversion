package com.gaurav.paypaydemo.stub

import com.gaurav.paypaydemo.datalayer.CountryCurrencyData
import com.gaurav.paypaydemo.datalayer.remote.ExchangeRates
import com.gaurav.paypaydemo.ui.theme.uistate.CurrencyExchangeData

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