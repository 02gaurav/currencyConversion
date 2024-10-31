package com.gaurav.paypaydemo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.gaurav.paypaydemo.datalayer.util.NetworkConnectionFlow
import com.gaurav.paypaydemo.ui.theme.PayPayDemoTheme
import com.gaurav.paypaydemo.ui.theme.composables.CurrencyConversionScreen
import com.gaurav.paypaydemo.ui.theme.viewmodels.CurrencyExchangeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       // enableEdgeToEdge()
        setContent {
            PayPayDemoTheme {
                val viewmodel = hiltViewModel<CurrencyExchangeViewModel>()
                viewmodel.fetchCountryList().collectAsStateWithLifecycle()
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    CurrencyConversionScreen(Modifier.padding(innerPadding), onCurrencySelected = {
                        viewmodel.fetchCurrencyExchangeData(countrySelected = it)
                    }, onAmountChanged = {
                        viewmodel.fetchCurrencyExchangeData(amount = it)
                    })
                }
            }
        }
    }
}