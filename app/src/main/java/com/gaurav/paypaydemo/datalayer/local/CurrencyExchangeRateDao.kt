package com.gaurav.paypaydemo.datalayer.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CurrencyExchangeRateDao {

    @Query("SELECT * FROM currency_exchange_rates")
    suspend fun getAllExchangeRates(): List<CurrencyExchangeRate>

    @Query("SELECT * FROM currency_exchange_rates WHERE currency = :currency")
    suspend fun getExchangeRateByCurrency(currency: String): CurrencyExchangeRate

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExchangeRate(exchangeRate: CurrencyExchangeRate)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExchangeRateList(exchangeRates: List<CurrencyExchangeRate>)

    @Query("DELETE FROM currency_exchange_rates")
    fun deleteAllExchangeRates()

    @Query("SELECT currency FROM currency_exchange_rates")
    suspend fun fetchCountryList(): List<String>
}