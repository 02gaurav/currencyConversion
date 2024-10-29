package com.gaurav.paypaydemo.ui.theme.composables

import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gaurav.paypaydemo.datalayer.CountryCurrencyData

@Composable
fun CountryCurrencyScreen(countryData: List<CountryCurrencyData>, listType: ListType) {
    Log.d("Composition", "CountryCurrencyScreen")
    when (listType) {
        ListType.HorizontalList -> {
            HorizontalList(countryData)
        }

        ListType.VerticalList -> {
            VerticalList(countryData)
        }

        ListType.HorizontalGRID -> {
            HorizontalGridList(countryData)
        }

        ListType.VerticalGRID -> {
            VerticalGridList(countryData)
        }
    }

}

@Composable
fun HorizontalList(countryData: List<CountryCurrencyData>) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(countryData) { country ->
            CountryWithCurrency(country, CurrencyViewOrientation.Horizontal)
        }
    }
}


@Composable
fun VerticalList(countryData: List<CountryCurrencyData>) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(countryData) { country ->
            CountryWithCurrency(country,CurrencyViewOrientation.Horizontal)
        }
    }
}

@Composable
fun HorizontalGridList(countryData: List<CountryCurrencyData>) {
    LazyHorizontalGrid(rows = GridCells.Fixed(3),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
        items(countryData) { country ->
            CountryWithCurrency(country, CurrencyViewOrientation.Horizontal)
        }
    }
}


@Composable
fun VerticalGridList(countryData: List<CountryCurrencyData>) {
    LazyVerticalGrid(columns = GridCells.Fixed(3),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)) {
        items(countryData) { country ->
            CountryWithCurrency(country, CurrencyViewOrientation.Vertical)
        }
    }
}

@Composable
fun CountryWithCurrency(countryData: CountryCurrencyData, orientation:CurrencyViewOrientation) {
    if(orientation == CurrencyViewOrientation.Horizontal){
        Row(Modifier.padding(8.dp).border(
            width = 1.dp,                   // Stroke width
            color = Color.Black,              // Stroke color
            shape = RoundedCornerShape(2.dp)) // Optional: Rounded corners
            .padding(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            /*AsyncImage(
                model = "https://flagcdn.com/216x162/in.png",
                contentDescription = "flag",
                Modifier.size(36.dp)
            )
            Spacer(modifier = Modifier.size(8.dp))*/
            Text(text = countryData.code)
            Spacer(modifier = Modifier.size(8.dp))
            Text(text = countryData.value?.toString()?:"")
        }
    } else{
        Column(Modifier.padding(8.dp).border(
            width = 1.dp,                   // Stroke width
            color = Color.Black,              // Stroke color
            shape = RoundedCornerShape(2.dp)) // Optional: Rounded corners
            .padding(4.dp),
            horizontalAlignment = Alignment.CenterHorizontally

        ) {
            /*AsyncImage(
                model = "https://flagcdn.com/16x12/in.webp",
                contentDescription = "flag",
                Modifier.size(36.dp)
            )
            Spacer(modifier = Modifier.size(8.dp))*/
            Text(text = countryData.code)
            Spacer(modifier = Modifier.size(8.dp))
            Text(text = countryData.value?.toString()?:"")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CountryCurrencyScreenHorizontalPreview() {
    CountryCurrencyScreen(listOf(), ListType.HorizontalList)
}

@Preview(showBackground = true)
@Composable
fun CountryCurrencyScreenVerticalPreview() {
    CountryCurrencyScreen(listOf(), ListType.VerticalList)
}

@Preview(showBackground = true)
@Composable
fun CountryCurrencyScreenVerticalGridPreview() {
    CountryCurrencyScreen(listOf(), ListType.VerticalGRID)
}

@Preview(showBackground = true)
@Composable
fun CountryCurrencyScreenHorizontalGridPreview() {
    CountryCurrencyScreen(listOf(), ListType.HorizontalGRID)
}

enum class ListType {
    HorizontalList,
    VerticalList,
    HorizontalGRID,
    VerticalGRID
}

enum class CurrencyViewOrientation {
    Horizontal,
    Vertical

}