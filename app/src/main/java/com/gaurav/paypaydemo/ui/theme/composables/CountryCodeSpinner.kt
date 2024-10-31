package com.gaurav.paypaydemo.ui.theme.composables

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.gaurav.paypaydemo.ui.theme.Purple
import com.gaurav.paypaydemo.ui.theme.viewmodels.CurrencyExchangeViewModel

@Composable
fun CurrencySpinner(
    modifier: Modifier,
    onCurrencySelected: (String) -> Unit
) {
    Log.d("Composition", "CurrencySpinner")
    val viewmodel = hiltViewModel<CurrencyExchangeViewModel>()
    val state = viewmodel.fetchCountryList().collectAsStateWithLifecycle()

    var expanded by rememberSaveable { mutableStateOf(false) }
    var selectedOption by rememberSaveable { mutableStateOf("Choose Country") }

    val options: List<String> = state.value
    CountrySpinnerScreen(modifier, expanded, selectedOption, options, onExpandedChange = {
        expanded = it
    }, onCurrencySelected = {
        selectedOption = it
        onCurrencySelected(it)
    })
}

@Composable
fun CountrySpinnerScreen(modifier: Modifier,
                          expanded: Boolean,
                          selectedOption: String,
                          options: List<String>,
                          onExpandedChange: (Boolean) -> Unit,
                          onCurrencySelected: (String) -> Unit,) {
    Row(modifier
        .testTag("currencySpinner")
        .clickable { // Anchor view
            onExpandedChange(!expanded)
        }) { // Anchor view
        Text(text = selectedOption) // City name label
        Icon(imageVector = Icons.Filled.ArrowDropDown, contentDescription = "Dropdown icon")
        CountrySpinnerList(modifier.align(Alignment.CenterVertically),
            expanded, selectedOption, options, onExpandedChange, onCurrencySelected)
    }

}

@Composable
fun CountrySpinnerList(
    modifier: Modifier,
    expanded: Boolean,
    selectedOption: String,
    options: List<String>,
    onExpandedChange: (Boolean) -> Unit,
    onCurrencySelected: (String) -> Unit,
) {
    DropdownMenu(
        modifier = modifier
            .testTag("dropdownMenuList"),
        expanded = expanded,
        onDismissRequest = {
            onExpandedChange(false)
        }
    ) {
        options.forEach { option ->
            DropdownMenuItem(
                onClick = {
                    onExpandedChange(false)
                    onCurrencySelected(option)
                },
                text = {
                    Text(
                        option,
                        modifier = Modifier.wrapContentWidth(),
                        style = if (option == selectedOption) {
                            MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = Purple
                            )
                        } else {
                            MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Normal,
                                color = MaterialTheme.colorScheme.secondary
                            )
                        }
                    )
                }
            )
        }
    }
}


@Preview
@Composable
fun CurrencyInputPreview() {
    CurrencySpinner(Modifier.padding(end = 16.dp) /*, listOf(
        "INR",
        "USD",
        "EUR",
        "GBP",
        "JPY",
        "AUD",
        "CAD",
        "CHF",
        "CNY",
        "HKD",
        "NZD",
        "RUB",
        "SGD",
        "ZAR"
    )*/, {})
}
