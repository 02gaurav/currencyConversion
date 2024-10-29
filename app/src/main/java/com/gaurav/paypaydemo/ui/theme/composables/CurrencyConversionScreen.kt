package com.gaurav.paypaydemo.ui.theme.composables

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.gaurav.paypaydemo.ui.theme.Black
import com.gaurav.paypaydemo.ui.theme.Gray
import com.gaurav.paypaydemo.ui.theme.Purple
import com.gaurav.paypaydemo.ui.theme.viewmodels.CurrencyExchangeViewModel

@Composable

fun CurrencyConversionScreen(
    modifier: Modifier,
    onCurrencySelected: (String) -> Unit,
    onAmountChanged: (String) -> Unit
) {
    Log.d("Composition", "CurrencyConversionScreen")

    val viewmodel = hiltViewModel<CurrencyExchangeViewModel>()
    val state = viewmodel.exchangeState.collectAsStateWithLifecycle()

    Column {
        Column(modifier = modifier
            .padding(16.dp)) {
            CurrencyInput(Modifier, onAmountChanged)
            Spacer(modifier = Modifier.height(16.dp))
            CurrencySpinner(
                Modifier
                    .wrapContentHeight()
                    .align(alignment = Alignment.End)
                    .background(color = Gray, shape = RoundedCornerShape(4.dp))
                    .padding(8.dp),
            ){
                onCurrencySelected(it)
            }
        }
        when(state.value) {
            is com.gaurav.paypaydemo.ui.theme.uistate.CurrencyExchangeState.Success -> {
                val exchangeRates  = (state.value as com.gaurav.paypaydemo.ui.theme.uistate.CurrencyExchangeState.Success).data.exchangeData
                CountryCurrencyScreen(exchangeRates, listType = ListType.VerticalGRID)
            }
            is com.gaurav.paypaydemo.ui.theme.uistate.CurrencyExchangeState.Error -> {}
            is com.gaurav.paypaydemo.ui.theme.uistate.CurrencyExchangeState.Loading -> {}
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CurrencyInput(modifier: Modifier, onAmountChanged: (String) -> Unit) {
    Log.d("Composition", "CurrencyInput")
    val input = remember { mutableStateOf("") }
    Column {
        OutlinedTextField(
            value = input.value,
            onValueChange = { newValue ->
                // Ensure only numbers are allowed
                if (newValue.all { it.isDigit() }) {
                    input.value = newValue
                    println("New value == ${input.value}")
                    onAmountChanged.invoke(input.value)
                }
            },
            label = { Text("Enter amount") },
            shape = RoundedCornerShape(8.dp),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Purple,
                unfocusedBorderColor = Gray,
                focusedTextColor = Black,
            ),
        )
    }
}

@Preview
@Composable
fun CurrencyConversionScreenPreview() {
    CurrencyConversionScreen(Modifier.Companion.padding(16.dp),{},{})
}