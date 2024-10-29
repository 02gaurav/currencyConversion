package com.gaurav.paypaydemo.ui.theme.uistate

import androidx.compose.runtime.Stable
import com.gaurav.paypaydemo.datalayer.CountryCurrencyData

sealed class CurrencyExchangeState {
    data class Success(val data: CurrencyExchangeData) : CurrencyExchangeState()
    data class Error(val error: ERROR) : CurrencyExchangeState()
    object Loading : CurrencyExchangeState()
}

enum class ERROR(val msg: String) {
    NO_INTERNET("no internet"),
    SERVER_ERROR("server not responding"),
    NO_DATA("no data available"),
    IO_ERROR(" io exception")
}

@Stable
data class CurrencyExchangeData(
    val exchangeData: List<CountryCurrencyData>,
    val timestamp: Long,
)