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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.gaurav.paypaydemo.ui.theme.Purple
import com.gaurav.paypaydemo.ui.theme.viewmodels.CurrencyExchangeViewModel

@Composable
fun CurrencySpinner(modifier: Modifier,
                    onCurrencySelected: (String) -> Unit) {
    Log.d("Composition", "CurrencySpinner")
    val viewmodel = hiltViewModel<CurrencyExchangeViewModel>()
    val state = viewmodel.countryList.collectAsStateWithLifecycle()

    var expanded by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf("Choose Country") }
    val options:List<String> = state.value

        Row(modifier
            .clickable { // Anchor view
            expanded = !expanded
        }) { // Anchor view
            Text(text = selectedOption) // City name label
            Icon(imageVector = Icons.Filled.ArrowDropDown, contentDescription = "Dropdown icon")
            DropdownMenu(
                modifier = modifier
                    .align(Alignment.CenterVertically),
                expanded = expanded,
                onDismissRequest = {
                    expanded = false
                }
            ) {
                options.forEach { option ->
                    DropdownMenuItem(
                        onClick = {
                            selectedOption = option
                            expanded = false // Close the menu
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
