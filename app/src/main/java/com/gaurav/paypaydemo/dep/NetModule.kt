package com.gaurav.paypaydemo.dep

import android.content.Context
import com.gaurav.paypaydemo.MyApplication
import com.gaurav.paypaydemo.datalayer.remote.CurrencyExchangeService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetModule {

    val baseUrl = "https://openexchangerates.org/api/" // base url
    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideCatBreedService(retrofit: Retrofit): CurrencyExchangeService {
        return retrofit.create(CurrencyExchangeService::class.java)
    }

    @Provides
    @Singleton
    fun provideContext(): Context {
        return MyApplication::class.java.newInstance()
    }

}