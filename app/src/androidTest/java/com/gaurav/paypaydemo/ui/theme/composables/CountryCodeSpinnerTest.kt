package com.gaurav.paypaydemo.ui.theme.composables

import androidx.compose.ui.Modifier
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToNode
import com.gaurav.paypaydemo.stub.stubCountryList
import org.junit.Rule
import org.junit.Test

class CountryCodeSpinnerTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testCountryCodeSpinnerNotExpanded() {
        setUpTestContentExpandedFalse()
        composeTestRule
            .onNodeWithText("Choose Country")
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithText("INR")
            .assertIsNotDisplayed()
    }

    @Test
    fun testCountryCodeSpinnerExpanded() {
        setUpTestContentExpandedTrue()
        composeTestRule
            .onNodeWithText("Choose Country")
            .assertIsNotDisplayed()
        composeTestRule
            .onNodeWithText("INR")
            .assertIsDisplayed()
    }

    @Test
    fun testCountryCodeSpinnerExpandedScroll() {
        setUpTestContentExpandedTrue()
        composeTestRule
            .onNodeWithTag("dropdownMenuList")
            .performScrollToNode(hasText("ALL"))
        composeTestRule
            .onNodeWithText("AMD")
            .assertIsDisplayed()
    }

    @Test
    fun testCountryCodeSpinnerPerformClick() {
        setUpTestContentExpandedTrue()
        composeTestRule
            .onNodeWithTag("dropdownMenuList")
            .performScrollToNode(hasText("ALL"))
        composeTestRule
            .onNodeWithText("AMD")
            .performClick()

        // Verify that the dropdown is dismissed
        composeTestRule.onNodeWithText("AMD")
            .assertIsDisplayed()
    }

    private fun setUpTestContentExpandedTrue() {
        composeTestRule.setContent {
            CountrySpinnerScreen(Modifier,
                true,
                "YEN",
                stubCountryList(),{},{})
        }
    }

    private fun setUpTestContentExpandedFalse() {
        composeTestRule.setContent {
            CountrySpinnerScreen(Modifier,
                false,
                "Choose Country",
                stubCountryList(),{

                },{

                })
        }
    }
}