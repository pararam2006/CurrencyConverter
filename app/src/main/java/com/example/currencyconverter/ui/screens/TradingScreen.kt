package com.example.currencyconverter.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.example.currencyconverter.ui.viewmodels.TradingScreenViewModel

@Composable
fun TradingScreen(
    modifier: Modifier = Modifier,
    vm: TradingScreenViewModel,
    currencyFrom: String = "",
    currencyTo: String = "",
    currencyFromAmount: Double = 1.0,
//    currencyToAmount: Double = 1.0,
) {
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        //Если пришли сюда не из "Валют"
        if (currencyTo == "" || currencyFrom == "") {
            Text("Навигация не с экрана Валюты еще не написана :(" , fontSize = 24.sp)
        } else {
            //Если пришли из "Валют"
            Text("$currencyFrom, $currencyTo, $currencyFromAmount", fontSize = 24.sp)
        }
    }
}

@SuppressLint("ViewModelConstructorInComposable")
@Preview
@Composable
fun TradingScreenPreview() {
    TradingScreen(vm = TradingScreenViewModel())
}