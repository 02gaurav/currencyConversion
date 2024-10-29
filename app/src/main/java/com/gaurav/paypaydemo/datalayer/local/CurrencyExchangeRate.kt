package com.gaurav.paypaydemo.datalayer.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "currency_exchange_rates")
data class CurrencyExchangeRate(
    @PrimaryKey
    @ColumnInfo(name = "currency")
    val currencySymbol: String,
    @ColumnInfo(name = "exchange") // usd To current currency
    val exchangeRate: Double,
    @ColumnInfo(name = "timestamp")
    val timestamp: Long,
    @ColumnInfo(name = "last_fetch_time")
    val lastFetchTime: Long
)