package com.gaurav.paypaydemo.ui.theme.composables

import androidx.compose.ui.Modifier
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performTextInput
import org.junit.Rule
import org.junit.Test

class CurrencyInputTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testCurrencyInputNoAmountEntered() {
        setStubCurrencyInput()
        composeTestRule
            .onNodeWithText("Enter amount")
            .assertIsDisplayed()
    }

    @Test
    fun testCurrencyInputChange() {
        setStubCurrencyInput()
        val textField =  composeTestRule.onNodeWithTag("currencyInput")
        textField.assertIsDisplayed()
        textField.performTextInput("100")
        textField.assert(hasText("100"))
    }

    @Test
    fun testCurrencyInputChangeAlphabets() {
        setStubCurrencyInput()
        val textField =  composeTestRule.onNodeWithTag("currencyInput")
        textField.assertIsDisplayed()
        textField.performTextInput("100")
        textField.assert(hasText("100"))
        textField.performTextInput("ABC") // not allow adding abc in text field
        textField.assert(hasText("100"))
    }

    @Test
    fun testCurrencyInputChangeAlphabetsAndNumber() {
        setStubCurrencyInput()
        val textField =  composeTestRule.onNodeWithTag("currencyInput")
        textField.assertIsDisplayed()
        textField.performTextInput("ABC")
        textField.assert(!hasText("ABC"))
        textField.performTextInput("25")
        textField.assert(hasText("25"))
        textField.performTextInput("10")
        textField.assert(hasText("2510"))
    }

    @Test
    fun testCurrencyInputChangeEnterDoubleValue() {
        setStubCurrencyInput()
        val textField =  composeTestRule.onNodeWithTag("currencyInput")
        textField.assertIsDisplayed()
        textField.performTextInput("10.1")
        textField.assert(hasText("10.1"))
        textField.performTextInput("ABC") // not allow adding abc in text field
        textField.assert(hasText("10.1"))
    }

    private fun setStubCurrencyInput() {
        composeTestRule.setContent {
            CurrencyInput(
                Modifier,
                {}
            )
        }
    }

}
