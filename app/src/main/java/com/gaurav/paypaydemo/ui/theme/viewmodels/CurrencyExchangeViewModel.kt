package com.gaurav.paypaydemo.ui.theme.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gaurav.paypaydemo.datalayer.CountryCurrencyData
import com.gaurav.paypaydemo.datalayer.repo.CurrencyExchangeRepo
import com.gaurav.paypaydemo.ui.theme.uistate.CurrencyExchangeData
import com.gaurav.paypaydemo.ui.theme.uistate.CurrencyExchangeState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CurrencyExchangeViewModel
@Inject
constructor(private val repo: CurrencyExchangeRepo) : ViewModel() {

    var baseCurrency = "USD"
    var selectedAmount = ""

    private fun exchangeRates(): MutableStateFlow<CurrencyExchangeState> =
        MutableStateFlow(CurrencyExchangeState.Loading)

    val countryList: MutableStateFlow<List<String>> = MutableStateFlow(listOf())
    val exchangeState: MutableStateFlow<CurrencyExchangeState> =
        MutableStateFlow(CurrencyExchangeState.Loading)

    fun getCountryList(): StateFlow<List<String>> = countryList
        .onStart { fetchCountryList(baseCurrency) }
        .stateIn(
            viewModelScope, SharingStarted.WhileSubscribed(5000L),
            listOf()
        )

    private fun fetchCountryList(base: String) {
        viewModelScope.launch {
            countryList.value = repo.fetchCountryListData()
        }
    }

/*    fun getExchangeRates(): StateFlow<CurrencyExchangeState> = exchangeRates()
        .onStart { fetchCurrencyExchangeData(baseCurrency) }
        .stateIn(
            viewModelScope, SharingStarted.WhileSubscribed(5000L),
            CurrencyExchangeState.Loading
        )*/


    fun fetchCurrencyExchangeData(countrySelected: String? = null, amount: String? = null) {
        viewModelScope.launch {
            exchangeState.value = CurrencyExchangeState.Loading
            if(countrySelected != null){
                baseCurrency = countrySelected
            }
            if(amount != null) {
                selectedAmount = amount
            }

            val response = repo.fetchCurrencyExchangeData(
                baseCurrency,
                if (selectedAmount == "") 1.0 else selectedAmount.toDouble() // check for number format exception
            )
            if (response != null) {
                exchangeState.value = CurrencyExchangeState.Success(
                    response?.rates ?: CurrencyExchangeData(
                        listOf<CountryCurrencyData>(), 0
                    )
                )
            } else {
                exchangeState.value =
                    CurrencyExchangeState.Error(com.gaurav.paypaydemo.ui.theme.uistate.ERROR.NO_DATA)
            }
        }
    }

}
// 1 USD = 3.673 AED
// 1 USD = 84.1 INR
// 1 AED = 84.1/3.673 INR === 22.896814593 INR
// 0.011189