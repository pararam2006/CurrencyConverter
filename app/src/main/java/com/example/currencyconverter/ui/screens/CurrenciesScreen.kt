package com.example.currencyconverter.ui.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.currencyconverter.data.dataSource.remote.dto.RateDto
import com.example.currencyconverter.data.manager.CurrencyRatesManager
import com.example.currencyconverter.ui.viewmodels.CurrenciesScreenViewModel
import java.math.BigDecimal
import java.math.RoundingMode

@Composable
fun CurrenciesScreen(
    modifier: Modifier = Modifier,
    vm: CurrenciesScreenViewModel,
    navController: NavController
) {
    val accounts by vm.accounts.collectAsState()
    val rates by vm.rates.collectAsState()
    val isLoading by vm.isLoading.collectAsState()
    var isChoosingCurrencyToTrade by remember { mutableStateOf(false) }
    var selectedCurrencyAmount by remember { mutableStateOf(BigDecimal(vm.amount).setScale(5, RoundingMode.HALF_EVEN)) }

    // Сброс состояния при изменении выбранной валюты
    LaunchedEffect(vm.selectedCurrency) {
        isChoosingCurrencyToTrade = false
        selectedCurrencyAmount = BigDecimal(vm.amount).setScale(5, RoundingMode.HALF_EVEN)
        Log.d("CurrenciesScreen", "Selected currency changed to: ${vm.selectedCurrency}")
    }

    // Обновляем базовую валюту и сумму при изменении
    LaunchedEffect(vm.selectedCurrency, vm.amount) {
        CurrencyRatesManager.updateBaseCurrency(vm.selectedCurrency)
        CurrencyRatesManager.updateAmount(vm.amount)
    }

    Column(modifier = modifier) {
        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        } else {
            LazyColumn {
                items(rates) { item: RateDto ->
                    val roundedRate = BigDecimal(item.value).setScale(5, RoundingMode.HALF_EVEN).toString()
                    val balance = accounts.find { it.code == item.currency }?.amount ?: 0.0
                    val roundedBalance = BigDecimal(balance).setScale(5, RoundingMode.HALF_EVEN).toString()
                    val isRub = item.currency == "RUB" // Отладка для рубля

                    OutlinedButton(
                        onClick = {
                            if (!isChoosingCurrencyToTrade) {
                                vm.selectCurrency(item.currency)
                                CurrencyRatesManager.updateBaseCurrency(item.currency)
                                CurrencyRatesManager.updateAmount(1.0)
                            } else {
                                if (vm.selectedCurrencyToTrade != vm.selectedCurrency) {
                                    navController.navigate(
                                        "trading/${vm.selectedCurrency}/${item.currency}/${selectedCurrencyAmount.toDouble()}?rates=${rates.joinToString(",") { "${it.currency},${it.value}" }}"
                                    )
                                }
                            }
                        },
                        shape = RectangleShape,
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceAround
                        ) {
                            Image(
                                painter = painterResource(vm.getCountryIcon(item.currency)),
                                contentDescription = "Icon"
                            )

                            Spacer(modifier = Modifier.width(10.dp))

                            Column {
                                Text(text = item.currency)
                                Spacer(Modifier.height(3.dp))
                                Text(text = vm.getCurrencyFullName(item.currency))
                                Spacer(Modifier.height(3.dp))
                                Text(text = "Баланс: ${vm.getCurrencySymbol(item.currency)}$roundedBalance")
                            }

                            Spacer(modifier = Modifier.weight(1f))

                            if (vm.selectedCurrency == item.currency) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    BasicTextField(
                                        value = selectedCurrencyAmount.toString(),
                                        onValueChange = { newText: String ->
                                            try {
                                                selectedCurrencyAmount = BigDecimal(newText)
                                                vm.updateAmount(selectedCurrencyAmount.toDouble())
                                            } catch (err: NumberFormatException) {
                                                Log.d("Error", err.cause.toString())
                                            }
                                        },
                                        textStyle = TextStyle(textAlign = TextAlign.End),
                                        modifier = Modifier.width(100.dp),
                                        interactionSource = remember { MutableInteractionSource() }
                                            .also { interactionSource ->
                                                LaunchedEffect(interactionSource) {
                                                    interactionSource.interactions.collect {
                                                        if (it is PressInteraction.Release) {
                                                            Log.d(
                                                                "Interaction",
                                                                "Release detected for ${item.currency}, setting isChoosingCurrencyToTrade to true"
                                                            )
                                                            isChoosingCurrencyToTrade = true
                                                        }
                                                    }
                                                }
                                            }
                                    )
                                    if (isChoosingCurrencyToTrade) {
                                        Icon(
                                            imageVector = Icons.Filled.Close,
                                            contentDescription = "Close",
                                            modifier = Modifier
                                                .clickable {
                                                    Log.d("Icon", "Cancel button clicked for ${item.currency}")
                                                    isChoosingCurrencyToTrade = false
                                                    vm.amount = 1.0
                                                    selectedCurrencyAmount = BigDecimal(1.0).setScale(
                                                        5,
                                                        RoundingMode.HALF_EVEN
                                                    )
                                                }.size(20.dp) // Фиксированный размер
                                                .then(if (isRub) Modifier.padding(start = 4.dp) else Modifier) // Дополнительное пространство для рубля
                                        )
                                    }
                                }
                            } else {
                                Text(text = "${vm.getCurrencySymbol(item.currency)}$roundedRate")
                            }
                        }
                    }
                    Spacer(Modifier.height(10.dp))
                }
            }
        }
    }
}