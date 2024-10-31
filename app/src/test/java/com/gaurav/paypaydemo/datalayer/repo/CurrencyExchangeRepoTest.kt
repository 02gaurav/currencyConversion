package com.gaurav.paypaydemo.datalayer.repo

import com.gaurav.paypaydemo.datalayer.local.AppDb
import com.gaurav.paypaydemo.datalayer.local.CurrencyExchangeRateDao
import com.gaurav.paypaydemo.datalayer.remote.CurrencyExchangeService
import com.gaurav.paypaydemo.datalayer.util.RateLimiter
import com.gaurav.paypaydemo.stub.getCountryListDataDbStub
import com.gaurav.paypaydemo.stub.getCurrencyExchangeDataDbStub
import com.gaurav.paypaydemo.stub.mockResponse
import com.gaurav.paypaydemo.stub.mockResponseError
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestScope
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.kotlin.mock
import org.mockito.ArgumentMatchers.anyLong
import org.mockito.Mockito.doReturn
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class CurrencyExchangeRepoTest {
    private lateinit var testScope: TestScope
    private val service: CurrencyExchangeService = mock()
    private val appDb: AppDb = mock()
    private val rateLimiter:RateLimiter = mock()
    private lateinit var repo: CurrencyExchangeRepo
    private val dao:CurrencyExchangeRateDao = mock()

    @Before
    fun setUp() {
        testScope = TestScope()
        repo = CurrencyExchangeRepo(service, appDb, rateLimiter)
    }

    @Test
    fun fetchExchangeRate_success_DB() = runBlocking {
        doReturn(false).`when`(rateLimiter).shouldFetch(anyLong(), anyLong())
        doReturn(dao).`when`(appDb).currencyExchangeDao()
        doReturn(getCurrencyExchangeDataDbStub()).`when`(dao).getAllExchangeRates()
        val res = repo.fetchCurrencyExchangeData("USD",1.0)
        assert(res?.rates?.exchangeData?.size == 4)
        assert(res?.base == "USD")
    }

    @Test
    fun fetchExchangeRate_success_DB_diff_currency() = runBlocking {

        doReturn(false).`when`(rateLimiter).shouldFetch(anyLong(), anyLong())
        doReturn(dao).`when`(appDb).currencyExchangeDao()
        doReturn(getCurrencyExchangeDataDbStub()).`when`(dao).getAllExchangeRates()
        // Now base is INR
        val res = repo.fetchCurrencyExchangeData("INR",1.0)
        assert(res?.rates?.exchangeData?.size == 4)
        assert(res?.base == "INR")
        // 1USD = 83INR,
        // 1USD = 0.77 YEN
        // 83 INR = 0.77 YEN
        // 1 INR = (0.77/83) YEN
        assert(res?.rates?.exchangeData?.get(0)?.code == "YEN")
        assert(res?.rates?.exchangeData?.get(0)?.value == 0.00927710843373494)
        // 1 INR ==1 INR
        assert(res?.rates?.exchangeData?.get(1)?.code == "INR")
        assert(res?.rates?.exchangeData?.get(1)?.value == 1.0)
    }

    @Test
    fun fetchExchangeRate_success_DB_diff_Amount() = runBlocking {

        doReturn(false).`when`(rateLimiter).shouldFetch(anyLong(), anyLong())
        doReturn(dao).`when`(appDb).currencyExchangeDao()
        doReturn(getCurrencyExchangeDataDbStub()).`when`(dao).getAllExchangeRates()
        // Now base is INR
        val res = repo.fetchCurrencyExchangeData("INR",10.0)
        assert(res?.rates?.exchangeData?.size == 4)
        assert(res?.base == "INR")
        // 1USD = 83INR,
        // 1USD = 0.77 YEN
        // 83 INR = 0.77 YEN
        // 1 INR = (0.77/83) YEN
        // 10 INR = (0.77/83) YEN * 10
        assert(res?.rates?.exchangeData?.get(0)?.code == "YEN")
        assert(res?.rates?.exchangeData?.get(0)?.value == 0.0927710843373494)
        // 1 INR ==1 INR
        assert(res?.rates?.exchangeData?.get(1)?.code == "INR")
        assert(res?.rates?.exchangeData?.get(1)?.value == 10.0)
    }

    @Test
    fun fetchExchangeRate_success_remote() = runBlocking {
        doReturn(true).`when`(rateLimiter).shouldFetch(anyLong(), anyLong())
        doReturn(dao).`when`(appDb).currencyExchangeDao()
        doReturn(getCurrencyExchangeDataDbStub()).`when`(dao).getAllExchangeRates()
        doReturn(mockResponse).`when`(service).getExchangeRatesLive()
        val res = repo.fetchCurrencyExchangeData("USD",1.0)
        assert(res?.rates?.exchangeData?.size == 5)
        assert(res?.base == "USD")
    }

    @Test
    fun fetchExchangeRate_success_remote_diff_currency() = runBlocking {
        doReturn(true).`when`(rateLimiter).shouldFetch(anyLong(), anyLong())
        doReturn(dao).`when`(appDb).currencyExchangeDao()
        doReturn(getCurrencyExchangeDataDbStub()).`when`(dao).getAllExchangeRates()
        doReturn(mockResponse).`when`(service).getExchangeRatesLive()
        val res = repo.fetchCurrencyExchangeData("INR",1.0)
        assert(res?.rates?.exchangeData?.size == 5)
        assert(res?.base == "INR")
        // 1USD = 84INR,
        // 1USD = 0.84 YEN
        // 84 INR = 0.84 YEN
        // 1 INR = (0.84/84) YEN
        // 10 INR = (0.84/84) YEN * 1
        assert(res?.rates?.exchangeData?.get(0)?.code == "YEN")
        assert(res?.rates?.exchangeData?.get(0)?.value == 0.01)
        // 1 INR ==1 INR
        assert(res?.rates?.exchangeData?.get(1)?.code == "INR")
        assert(res?.rates?.exchangeData?.get(1)?.value == 1.0)
    }

    @Test
    fun fetchExchangeRate_success_remote_diff_amount() = runBlocking {
        doReturn(true).`when`(rateLimiter).shouldFetch(anyLong(), anyLong())
        doReturn(dao).`when`(appDb).currencyExchangeDao()
        doReturn(getCurrencyExchangeDataDbStub()).`when`(dao).getAllExchangeRates()
        doReturn(mockResponse).`when`(service).getExchangeRatesLive()
        val res = repo.fetchCurrencyExchangeData("INR",100.0)
        assert(res?.rates?.exchangeData?.size == 5)
        assert(res?.base == "INR")
        // 1USD = 84INR,
        // 1USD = 0.84 YEN
        // 84 INR = 0.84 YEN
        // 1 INR = (0.84/84) YEN
        // 10 INR = (0.84/84) YEN * 100
        assert(res?.rates?.exchangeData?.get(0)?.code == "YEN")
        assert(res?.rates?.exchangeData?.get(0)?.value == 1.0)
        // 1 INR ==1 INR
        assert(res?.rates?.exchangeData?.get(1)?.code == "INR")
        assert(res?.rates?.exchangeData?.get(1)?.value == 100.0)
    }

    @Test
    fun fetchCountryListData_success_DB() = runBlocking {
        doReturn(dao).`when`(appDb).currencyExchangeDao()
        doReturn(getCountryListDataDbStub()).`when`(dao).fetchCountryList()
        val res = repo.fetchCountryListData()
        assert(res.size == 4)
        assert(res[0] == "YEN")
        assert(res[1] == "INR")
    }

    @Test
    fun fetchCountryListData_success_DB_empty_fetch_remote() = runBlocking {
        doReturn(dao).`when`(appDb).currencyExchangeDao()
        doReturn(listOf<String>()).`when`(dao).fetchCountryList()
        doReturn(mockResponse).`when`(service).getExchangeRatesLive()
        val res = repo.fetchCountryListData()
        assert(res.size == 5)
        assert(res[0] == "YEN")
        assert(res[1] == "INR")
    }

    @Test
    fun fetchCountryListData_success_DB_empty_fetch_remote_error() = runBlocking {
        doReturn(dao).`when`(appDb).currencyExchangeDao()
        doReturn(listOf<String>()).`when`(dao).fetchCountryList()
        doReturn(mockResponseError).`when`(service).getExchangeRatesLive()
        val res = repo.fetchCountryListData()
        assert(res.size == 0)
    }

    @After
    fun tearDown() {
        testScope.cancel()
    }

}