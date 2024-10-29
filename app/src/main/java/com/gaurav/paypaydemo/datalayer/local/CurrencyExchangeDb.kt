package com.gaurav.paypaydemo.datalayer.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [CurrencyExchangeRate::class], version = 1)
abstract class AppDb : RoomDatabase() {

    abstract fun currencyExchangeDao(): CurrencyExchangeRateDao

    companion object {

        @Volatile
        private var INSTANCE: AppDb? = null

        fun getInstance(context: Context): AppDb =
            INSTANCE ?: synchronized(this) {
                INSTANCE
                    ?: buildDatabase(context).also { INSTANCE = it }
            }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                AppDb::class.java, "PayPay.db"
            ).build()
    }
}