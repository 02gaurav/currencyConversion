package com.gaurav.paypaydemo.ui.theme.composables

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performScrollToIndex
import com.gaurav.paypaydemo.stub.stubCountryCurrencyData
import org.junit.Rule
import org.junit.Test

class CountryWithCurrencyTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testCountryWithCurrencyVerticalGrid() {
        setStubContentVerticalGrid()
        composeTestRule
            .onNodeWithText("INR")
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithText("83.0")
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithText("YEN")
            .assertIsDisplayed()
    }

    @Test
    fun testCountryWithCurrencyHorizontalGrid() {
        setStubContentHorizontalGrid()
        composeTestRule
            .onNodeWithText("INR")
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithText("83.0")
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithText("YEN")
            .assertIsDisplayed()
    }

    @Test
    fun testCountryWithCurrencyHorizontalList() {
        setStubContentHorizontalList()
        composeTestRule
            .onNodeWithText("INR")
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithText("83.0")
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithText("YEN")
            .assertIsDisplayed()
    }

    @Test
    fun testCountryWithCurrencyVerticalList() {
        setStubContentVerticalList()
        composeTestRule
            .onNodeWithText("INR")
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithText("83.0")
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithText("YEN")
            .assertIsDisplayed()
    }

    @Test
    fun testScrollingBehaviour() {
        setStubContentVerticalList()
        composeTestRule
            .onNodeWithTag("currencyList")
            .performScrollToIndex(4)
        composeTestRule
            .onNodeWithText("ALL")// assert index 5 and 6
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithText("AMD")// assert index 5 and 6
            .assertIsDisplayed()
    }

    private fun setStubContentVerticalGrid() {
        composeTestRule.setContent {
            CountryCurrencyScreen(stubCountryCurrencyData(),  listType = ListType.VerticalGRID)
        }
    }
    private fun setStubContentHorizontalGrid() {
        composeTestRule.setContent {
            CountryCurrencyScreen(stubCountryCurrencyData(),  listType = ListType.HorizontalGRID)
        }
    }
    private fun setStubContentHorizontalList() {
        composeTestRule.setContent {
            CountryCurrencyScreen(stubCountryCurrencyData(),  listType = ListType.HorizontalList)
        }
    }
    private fun setStubContentVerticalList() {
        composeTestRule.setContent {
            CountryCurrencyScreen(stubCountryCurrencyData(),  listType = ListType.VerticalList)
        }
    }

}
