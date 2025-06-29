package com.example.currencyconverter.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.currencyconverter.ui.viewmodels.TradingScreenViewModel
import java.math.BigDecimal
import java.math.RoundingMode

@Composable
fun TradingScreen(
    modifier: Modifier = Modifier,
    vm: TradingScreenViewModel,
    navController: NavController,
    currencyFrom: String = "",
    currencyTo: String = "",
    currencyFromAmount: Double = 1.0
) {
    val balances = vm.balances.collectAsState().value

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Если пришли сюда не из "Валют"
        if (currencyTo == "" || currencyFrom == "") {
            Column(
                modifier = modifier.fillMaxHeight().padding(start = 30.dp, end = 30.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    "Введите сумму для покупки на экране Валюты, выберите валюту для обмена и попадите сюда!",
                    fontSize = 20.sp,
                    softWrap = true
                )
            }
        } else {
            // Инициализация ViewModel данными
            vm.currencyFrom = currencyFrom
            vm.currencyTo = currencyTo
            vm.amountFrom = currencyFromAmount
            val rate = vm.getRate(currencyFrom, currencyTo) ?: 1.0
            vm.amountTo = currencyFromAmount * rate

            // Отображение валюты, которую продаем
            OutlinedButton(
                onClick = { /* Нет действия, только отображение */ },
                modifier = Modifier.align(Alignment.Start),
                shape = RectangleShape,
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(vm.getCountryIcon(currencyFrom)),
                        contentDescription = "Icon for $currencyFrom"
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(text = currencyFrom)
                        Text(text = vm.getCurrencyFullName(currencyFrom), fontSize = 12.sp)
                        Text(text = "Баланс: ${vm.getCurrencySymbol(currencyFrom)}${balances[currencyFrom] ?: 0.0}")
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = "-${BigDecimal(currencyFromAmount).setScale(5, RoundingMode.HALF_EVEN)} ${vm.getCurrencySymbol(currencyFrom)}",
                        textAlign = TextAlign.End
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Отображение валюты, которую покупаем
            OutlinedButton(
                onClick = { /* Нет действия, только отображение */ },
                modifier = Modifier.align(Alignment.Start),
                shape = RectangleShape
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(vm.getCountryIcon(currencyTo)),
                        contentDescription = "Icon for $currencyTo"
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(text = currencyTo)
                        Text(text = vm.getCurrencyFullName(currencyTo), fontSize = 12.sp)
                        Text(text = "Баланс: ${vm.getCurrencySymbol(currencyTo)}${BigDecimal(balances[currencyTo] ?: 0.0).setScale(5, RoundingMode.HALF_EVEN).toDouble()}")
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = "+${BigDecimal(vm.amountTo).setScale(5, RoundingMode.HALF_EVEN)} ${vm.getCurrencySymbol(currencyTo)}",
                        textAlign = TextAlign.End
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Обменный курс
            Text(
                text = "Обменный курс: 1 $currencyFrom = ${BigDecimal(rate).setScale(5, RoundingMode.HALF_EVEN)} $currencyTo",
                fontSize = 16.sp
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Кнопка для выполнения обмена
            Button(
                onClick = {
                    vm.trade()
                    navController.popBackStack("currencies", false) // Возвращаемся на экран "Валюты"
                },
                enabled = (balances[currencyFrom] ?: 0.0) >= currencyFromAmount // Проверяем достаточно ли средств
            ) {
                Text("Купить $currencyTo за $currencyFrom")
            }
        }
    }
}