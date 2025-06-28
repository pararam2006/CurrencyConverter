package com.example.currencyconverter.ui.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.example.currencyconverter.ui.viewmodels.CurrenciesScreenViewModel
import java.math.BigDecimal
import java.math.RoundingMode

@Composable
fun CurrenciesScreen(modifier: Modifier = Modifier, vm: CurrenciesScreenViewModel, navController: NavController) {
    var isChoosingCurrencyToTrade by remember { mutableStateOf(false) }
    val rates = vm.rates.collectAsState()
    var selectedCurrencyAmount = remember { mutableStateOf(BigDecimal(vm.amount).setScale(5, RoundingMode.HALF_EVEN)) }

    LazyColumn(modifier = modifier) {
        items(rates.value) { item: RateDto ->
            val rounded = BigDecimal(item.value).setScale(5, RoundingMode.HALF_EVEN).toString()

            OutlinedButton(
                onClick = {
                    if (!isChoosingCurrencyToTrade) {
                        vm.selectCurrency(item.currency)
                    } else {
                        if (vm.selectedCurrencyToTrade != vm.selectedCurrency) {
                            navController.navigate(
                                "trading/${vm.selectedCurrency}/${vm.selectedCurrencyToTrade}/${vm.amount}"
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
                        Text(text = item.currency) //Короткое название валюты
                        Spacer(Modifier.height(3.dp))
                        Text(text =vm.getCurrencyFullName(item.currency)) //Полное название валюты
                        Spacer(Modifier.height(3.dp))
                        Text(text = "БАЛАНС ТУТ")
                    }

                    Spacer(modifier = Modifier.weight(1F, fill = true))

                    if (vm.selectedCurrency == item.currency) {
                        BasicTextField(
                            value = selectedCurrencyAmount.value.toString(),
                            onValueChange = { newText: String ->
                                try {
                                    selectedCurrencyAmount.value = BigDecimal(newText)
                                    vm.updateAmount(selectedCurrencyAmount.value.toDouble())
                                }
                                catch (err: NumberFormatException) {
                                    Log.d("Error", err.cause.toString())
                                }
                            },
                            textStyle = TextStyle(textAlign = TextAlign.End),
                            modifier = Modifier.width(150.dp),
                            interactionSource = remember { MutableInteractionSource() }
                                .also { interactionSource ->
                                    LaunchedEffect(interactionSource) {
                                        interactionSource.interactions.collect {
                                            if (it is PressInteraction.Release) {
                                                // works like onClick
                                                isChoosingCurrencyToTrade = true
                                            }
                                        }
                                    }
                                }
                        )
                        if (isChoosingCurrencyToTrade) {
                            IconButton(
                                onClick = {
                                    isChoosingCurrencyToTrade = false
                                    vm.amount = 1.0
                                    selectedCurrencyAmount.value = BigDecimal(1.0).setScale(5, RoundingMode.HALF_EVEN)
                                }
                            ) {
                                Icon(imageVector = Icons.Filled.Close, contentDescription = "Close")
                            }
                        }
                    } else {
                        Text(text = vm.getCurrencySymbol(item.currency) + rounded)
                    }
                }
            }
            Spacer(Modifier.height(10.dp))
        }
    }
}