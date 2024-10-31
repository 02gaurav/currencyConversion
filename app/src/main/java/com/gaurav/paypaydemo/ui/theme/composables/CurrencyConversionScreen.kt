package com.gaurav.paypaydemo.ui.theme.composables

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.gaurav.paypaydemo.ui.theme.Gray
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
    val networkConnection = viewmodel.connection.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val isDataEmpty = viewmodel.countryList.collectAsStateWithLifecycle()


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
            if(isDataEmpty.value.isEmpty()) {
                Button(onClick = {
                    viewmodel.fetchCountryListData()
                }) {
                    Text("Retry")
                }
            }
        }
        when(state.value) {
            is com.gaurav.paypaydemo.ui.theme.uistate.CurrencyExchangeState.Success -> {
                val exchangeRates  = (state.value as com.gaurav.paypaydemo.ui.theme.uistate.CurrencyExchangeState.Success).data.exchangeData
                CountryCurrencyScreen(exchangeRates, listType = ListType.VerticalGRID)
            }
            is com.gaurav.paypaydemo.ui.theme.uistate.CurrencyExchangeState.Error -> {}
            is com.gaurav.paypaydemo.ui.theme.uistate.CurrencyExchangeState.Loading -> {
                LoadingData(modifier)
            }
            is com.gaurav.paypaydemo.ui.theme.uistate.CurrencyExchangeState.EmptyState -> {}
        }
    }
    LaunchedEffect(networkConnection.value) {
        if(!networkConnection.value) {
            Toast.makeText(context, "Internet connection lost", Toast.LENGTH_SHORT).show()
        }
    }
}

@Composable
fun LoadingData(modifier: Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            modifier = Modifier
                .padding(8.dp),
            text = "Loading..."
        )

        CircularProgressIndicator(color = Color.Black)
    }
}

@Preview
@Composable
fun CurrencyConversionScreenPreview() {
    CurrencyConversionScreen(Modifier.Companion.padding(16.dp),{},{})
}