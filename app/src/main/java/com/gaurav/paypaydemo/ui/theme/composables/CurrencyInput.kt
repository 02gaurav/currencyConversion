package com.gaurav.paypaydemo.ui.theme.composables

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.gaurav.paypaydemo.ui.theme.Black
import com.gaurav.paypaydemo.ui.theme.Gray
import com.gaurav.paypaydemo.ui.theme.Purple


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CurrencyInput(modifier: Modifier, onAmountChanged: (String) -> Unit) {
    Log.d("Composition", "CurrencyInput")
    // val showMaxRangeToast = remember { mutableStateOf(false) }
    // val context = LocalContext.current
    val input = rememberSaveable { mutableStateOf("") }
    Column {
        OutlinedTextField(
            value = input.value,
            onValueChange = { newValue ->
                // Ensure only numbers are allowed
                val isValidNumber = newValue.matches(Regex("^\\d*\\.?\\d*$"))
                // val withinIntRange = newValue.toLongOrNull()?.let { it <= Int.MAX_VALUE } ?: true
                if (isValidNumber ) {
                    input.value = newValue
                    onAmountChanged.invoke(input.value)
                    // showMaxRangeToast.value = false
                } else {
                    // showMaxRangeToast.value = true
                }
            },
            label = { Text("Enter amount") },
            shape = RoundedCornerShape(8.dp),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = modifier
                .testTag("currencyInput")
                .fillMaxWidth()
                .wrapContentHeight(),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Purple,
                unfocusedBorderColor = Gray,
                focusedTextColor = Black,
            ),
        )
    }

//    LaunchedEffect(showMaxRangeToast.value) {
//        if(showMaxRangeToast.value) {
//            Toast.makeText(context, "Max range you can check is 2147483647", Toast.LENGTH_SHORT).show()
//        }
//    }
}
