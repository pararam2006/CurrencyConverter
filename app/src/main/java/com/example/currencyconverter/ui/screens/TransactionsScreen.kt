package com.example.currencyconverter.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.currencyconverter.ui.viewmodels.TransactionsScreenViewModel
import java.math.BigDecimal
import java.math.RoundingMode

@Composable
fun TransactionsScreen(modifier: Modifier = Modifier, vm: TransactionsScreenViewModel, navController: NavController) {
    val transactions = vm.transactionsList.collectAsState()

    if (transactions.value.isEmpty()) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier.fillMaxSize()
        ) {
            Text(
                text = "Вы еще не совершали транзакций. \n Самое время это исправить!",
                textAlign = TextAlign.Center,
                fontSize = 20.sp
            )

            Spacer(modifier = Modifier.padding(top = 20.dp))

            OutlinedButton(onClick = {
                navController.navigate("trading")
            }) {
                Text("К обмену валют!")
            }
        }
    } else {
        Row(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Text("ID")
            Text("From")
            Text("To")
            Text("dateTime")
            Text("fromAmount")
            Text("toAmount")
        }
        LazyColumn(
            modifier = modifier.fillMaxSize().padding(top = 30.dp)
        ) {
            items(transactions.value) { item ->
                val splittedDateTime = item.dateTime.toString().split("T")
                val date = splittedDateTime[0]
                val time = splittedDateTime[1]
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Text("${item.id}")
                    Text("${item.from}")
                    Text("${item.to}")
                    Text("$date\n$time")
                    Text("${BigDecimal(item.fromAmount).setScale(5, RoundingMode.HALF_EVEN)}")
                    Text("${BigDecimal(item.toAmount).setScale(5, RoundingMode.HALF_EVEN)}")
                }
            }
        }
    }
}