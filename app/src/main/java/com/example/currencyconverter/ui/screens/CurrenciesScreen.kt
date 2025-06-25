package com.example.currencyconverter.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.currencyconverter.data.dataSource.remote.dto.RateDto
import com.example.currencyconverter.ui.viewmodels.CurrenciesScreenViewModel

@Composable
fun CurrenciesScreen(modifier: Modifier = Modifier, vm: CurrenciesScreenViewModel) {
    val rates = vm.rates.collectAsState()
    LazyColumn(modifier = modifier) {
        items(rates.value) { item: RateDto ->
            Text("item.currency: ${item.currency}")
            Spacer(Modifier.height(3.dp))
            Text("item.value: ${item.value}")
            Spacer(Modifier.height(10.dp))
        }
    }
}

@SuppressLint("ViewModelConstructorInComposable")
@Preview
@Composable
fun CurrenciesScreenPreview() {
    CurrenciesScreen(vm = CurrenciesScreenViewModel())
}