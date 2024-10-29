package com.gaurav.paypaydemo.ui.theme.viewmodels

import app.cash.turbine.test
import com.gaurav.paypaydemo.datalayer.repo.CurrencyExchangeRepo
import com.gaurav.paypaydemo.stub.getCountryListStub
import com.gaurav.paypaydemo.stub.getExchangeRates
import com.gaurav.paypaydemo.ui.theme.uistate.CurrencyExchangeState
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import kotlin.coroutines.ContinuationInterceptor

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class CurrencyExchangeViewModelTest {
    private lateinit var viewModel: CurrencyExchangeViewModel
    private lateinit var testScope: TestScope
    private val repo: CurrencyExchangeRepo = mockk()
    private val mainThreadSurrogate = newSingleThreadContext("UI thread")

    @Before
    fun setUp() {
        testScope = TestScope(StandardTestDispatcher())
        Dispatchers.setMain(testScope.coroutineContext[ContinuationInterceptor] as CoroutineDispatcher)
        viewModel = CurrencyExchangeViewModel(repo)
    }


    @Test
    fun fetchCountryList() {
        testScope.runTest {
            coEvery { repo.fetchCountryListData() } returns getCountryListStub()
            viewModel.getCountryList().test {
                // First emission should be the default empty list
                assertEquals(emptyList<String>(), awaitItem())
                testScope.advanceUntilIdle()
                assert(awaitItem() == getCountryListStub())
                assert(viewModel.countryList.value == getCountryListStub())
                cancelAndIgnoreRemainingEvents()
            }
        }
    }

    @Test
    fun fetchExchangeRate_USD() {
        testScope.runTest {
            coEvery { repo.fetchCurrencyExchangeData("USD",1.0) } returns getExchangeRates()
            viewModel.fetchCurrencyExchangeData("USD","1.0")
            viewModel._exchangeState.test {
                // First state is Loading state
                assertEquals(CurrencyExchangeState.Loading, awaitItem())
                testScope.advanceUntilIdle()
                assert(awaitItem() is CurrencyExchangeState.Success)
                assert((viewModel._exchangeState.value as CurrencyExchangeState.Success).data.exchangeData == getExchangeRates().rates?.exchangeData)
                cancelAndIgnoreRemainingEvents()
            }
        }
    }
    @Test
    fun fetchExchangeRate_failure() {
        testScope.runTest {
            coEvery { repo.fetchCurrencyExchangeData("USD",1.0) } returns null
            viewModel.fetchCurrencyExchangeData("USD","1.0")
            viewModel._exchangeState.test {
                // First state is Loading state
                assertEquals(CurrencyExchangeState.Loading, awaitItem())
                testScope.advanceUntilIdle()
                assert(awaitItem() is CurrencyExchangeState.Error)
                cancelAndIgnoreRemainingEvents()
            }
        }
    }

    @Test
    fun fetchExchangeRate_No_amount_selected_default_1() {
        testScope.runTest {
            coEvery { repo.fetchCurrencyExchangeData("USD",1.0) } returns getExchangeRates()
            viewModel.fetchCurrencyExchangeData("USD","")
            viewModel._exchangeState.test {
                // First state is Loading state
                assertEquals(CurrencyExchangeState.Loading, awaitItem())
                testScope.advanceUntilIdle()
                assert(awaitItem() is CurrencyExchangeState.Success)
                cancelAndIgnoreRemainingEvents()
            }
        }
    }

    @Test
    fun fetchExchangeRate_No_country_selected_default_USD() {
        testScope.runTest {
            coEvery { repo.fetchCurrencyExchangeData("USD",1.0) } returns getExchangeRates()
            viewModel.fetchCurrencyExchangeData("","")
            viewModel._exchangeState.test {
                // First state is Loading state
                assertEquals(CurrencyExchangeState.Loading, awaitItem())
                testScope.advanceUntilIdle()
                assert(awaitItem() is CurrencyExchangeState.Success)
                cancelAndIgnoreRemainingEvents()
            }
        }
    }


    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testScope.cancel()
    }

}