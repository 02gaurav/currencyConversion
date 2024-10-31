package com.gaurav.paypaydemo.ui.theme.viewmodels

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gaurav.paypaydemo.datalayer.repo.CurrencyExchangeRepo
import com.gaurav.paypaydemo.datalayer.util.NetworkConnectionFlow
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
constructor(
    private val repo: CurrencyExchangeRepo,
    private val networkConnectionFlow: NetworkConnectionFlow
) : ViewModel() {

    private var baseCurrency = "USD"
    private var selectedAmount = ""
    private var selectedCountry = ""

    @VisibleForTesting
    val _countryList: MutableStateFlow<List<String>> = MutableStateFlow(listOf())
    val countryList: StateFlow<List<String>> = _countryList
    private val _exchangeState: MutableStateFlow<CurrencyExchangeState> =
        MutableStateFlow(CurrencyExchangeState.EmptyState)
    val exchangeState: StateFlow<CurrencyExchangeState> = _exchangeState

    private val _connection: MutableStateFlow<Boolean> = MutableStateFlow(true)
    val connection: StateFlow<Boolean> = _connection

    init {
        observeNetworkStatus()
    }

    private fun observeNetworkStatus() {
        viewModelScope.launch {
            networkConnectionFlow.observeNetworkStatus().collect { isConnected ->
                _connection.value = isConnected
            }
        }
    }

    fun fetchCountryList(): StateFlow<List<String>> = _countryList
        .onStart { fetchCountryListData() }
        .stateIn(
            viewModelScope, SharingStarted.WhileSubscribed(5000L),
            listOf()
        )

    fun fetchCountryListData() {
        viewModelScope.launch {
            _countryList.value = repo.fetchCountryListData()
        }
    }

    fun fetchCurrencyExchangeData(countrySelected: String? = null, amount: String? = null) {
        println("==== fetchCurrencyExchangeData( countrySelected $countrySelected amount $amount)")
        viewModelScope.launch {
            _exchangeState.value = CurrencyExchangeState.Loading
            if(!countrySelected.isNullOrBlank()) {
                selectedCountry = countrySelected ?: ""
            }
            if(!amount.isNullOrBlank()) {
                selectedAmount = amount ?: ""
            }

            if (selectedCountry.isBlank() || selectedCountry.isEmpty()) {
                _exchangeState.value = CurrencyExchangeState.EmptyState
                return@launch
            }
            try {
                if (!countrySelected.isNullOrBlank()) {
                    baseCurrency = countrySelected
                }
                if (!amount.isNullOrBlank()) {
                    selectedAmount = amount
                }
                val response = repo.fetchCurrencyExchangeData(
                    baseCurrency,
                    if (selectedAmount == "") 1.0 else selectedAmount.toDouble() // check for number format exception
                )
                if (response != null) {
                    _exchangeState.value = CurrencyExchangeState.Success(
                        response.rates ?: CurrencyExchangeData(
                            listOf(), 0
                        )
                    )
                } else {
                    _exchangeState.value =
                        CurrencyExchangeState.Error(com.gaurav.paypaydemo.ui.theme.uistate.ERROR.NO_DATA)
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

}