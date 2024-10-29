package com.gaurav.paypaydemo.dep

import android.content.Context
import androidx.room.Room
import com.gaurav.paypaydemo.datalayer.local.AppDb
import com.gaurav.paypaydemo.datalayer.local.CurrencyExchangeRateDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Singleton
    @Provides
    fun provideCurrencyExchangeDatabase(@ApplicationContext context: Context): AppDb =
        Room
            .databaseBuilder(context, AppDb::class.java, "paypay.db")
            .build()

    @Singleton
    @Provides
    fun provideCurrencyExchangeDao(appDb: AppDb): CurrencyExchangeRateDao =
        appDb.currencyExchangeDao()
}
