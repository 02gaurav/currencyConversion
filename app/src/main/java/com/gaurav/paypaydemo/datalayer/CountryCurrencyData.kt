package com.gaurav.paypaydemo.datalayer

import androidx.compose.runtime.Stable

@Stable
data class CountryCurrencyData(
    val code:String,
    val value:Double?=null,
)