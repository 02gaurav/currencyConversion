package com.gaurav.paypaydemo.datalayer.repo

import com.gaurav.paypaydemo.datalayer.CountryCurrencyData
import com.gaurav.paypaydemo.datalayer.local.AppDb
import com.gaurav.paypaydemo.datalayer.local.CurrencyExchangeRate
import com.gaurav.paypaydemo.datalayer.remote.CurrencyExchangeService
import com.gaurav.paypaydemo.datalayer.remote.ExchangeRates
import com.gaurav.paypaydemo.ui.theme.uistate.CurrencyExchangeData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CurrencyExchangeRepo
@Inject
constructor(
    private val service: CurrencyExchangeService,
    private val appDb: AppDb
) {
    val timeOut = 1 * 60 * 1000 // 5 minutes
    val exchangeRatesMap: MutableMap<String, Double> = mutableMapOf() // base is USD

    // 1 USD = 83 INR
    // 1 INR  = 1/83 USD
    // 1 USD =  3.673 AED
    // 83 INR  = 3.673 AED
    // 1 INR  = 3.673/83 AED
    suspend fun fetchCurrencyExchangeData(
        countrySelected: String,
        amount: Double = 1.0
    ): ExchangeRates? {

        // Now base is countrySelected
        // calculate according to base USD rates in exchangeRatesMap

        return withContext(Dispatchers.Default) {
            try {
                val exchangeRatesDb = appDb.currencyExchangeDao().getAllExchangeRates()
                if (shouldFetch(exchangeRatesDb.firstOrNull()?.lastFetchTime)) {
                    // fetch from server
                    val exchangeRateRemote = fetchExchangeRateRemote()
                    // insert data in DB
                    insertDataInDB(exchangeRateRemote)
                    getExchangeRatesForCurrency(countrySelected, amount, exchangeRateRemote)
                } else {
                    // fetch from DB
                    getExchangeRatesForCurrency(countrySelected, amount, exchangeRatesDb)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }

    private fun getExchangeRatesForCurrency(
        countrySelected: String,
        amount: Double, exchangeRatesUsd: List<CurrencyExchangeRate>
    ): ExchangeRates {
        val exchangeData = ExchangeRates()
        val rates = arrayListOf<CountryCurrencyData>()
        exchangeRatesUsd.forEach {
            exchangeRatesMap[it.currencySymbol] = it.exchangeRate
        }
        exchangeRatesMap.forEach {
            val newValue = it.value / exchangeRatesMap[countrySelected]!!
            rates.add(CountryCurrencyData(it.key, newValue * amount))
        }
        exchangeData.apply {
            this.base = countrySelected
            this.rates = CurrencyExchangeData(rates, System.currentTimeMillis())
        }
        return exchangeData
    }


    private suspend fun insertDataInDB(data: List<CurrencyExchangeRate>) {
        withContext(Dispatchers.IO) {
            appDb.currencyExchangeDao().insertExchangeRateList(data)
        }
    }

    private suspend fun fetchExchangeRateRemote(): List<CurrencyExchangeRate> {
        return withContext(Dispatchers.IO) {
            try {
                println("Making API call ..... ")
                val response = service.getExchangeRatesLive()
                val exchangeRate = arrayListOf<CurrencyExchangeRate>()
                response.body()?.rates?.map { it ->
                    exchangeRate.add(
                        CurrencyExchangeRate(
                            it.key,
                            it.value,
                            response.body()?.timestamp ?: System.currentTimeMillis(),
                            System.currentTimeMillis()
                        )
                    )
                }
                exchangeRate
            } catch (e: Exception) {
                e.printStackTrace()
                listOf()
            }
        }
    }

    suspend fun fetchCountryListData(): List<String> {
        val data = arrayListOf<String>()
        return withContext(Dispatchers.IO) {
            try {
                val countryDataDb = appDb.currencyExchangeDao().fetchCountryList()
                countryDataDb.ifEmpty {
                    // fetch from Remote
                    val exchangeRate = fetchExchangeRateRemote()
                    appDb.currencyExchangeDao().insertExchangeRateList(exchangeRate)
                    exchangeRate.forEach {
                        data.add(it.currencySymbol)
                    }
                    data
                }

            } catch (e: Exception) {
                e.printStackTrace()
                println("here ======")
                listOf("USD")
            }

        }
    }

    private fun shouldFetch(lastFetched: Long?): Boolean {
        println("=====> lastFetched $lastFetched")
        val now = System.currentTimeMillis()
        if (lastFetched == null) {
            return true
        }
        val diff = now - lastFetched
        if (diff > timeOut) {
            return true
        }
        return false
    }
}
