package com.example.currencyconverter.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.currencyconverter.ui.screens.CurrenciesScreen
import com.example.currencyconverter.ui.screens.TradingScreen
import com.example.currencyconverter.ui.screens.TransactionsScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyApp(navController: NavHostController) {
    val currentBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry.value?.destination?.route
    Scaffold(
        topBar = {
            TopAppBar(title = {
                Text(text = when(currentRoute) {
                    "currencies" -> "Валюты"
                    "trading" -> "Обмен"
                    "transactions" -> "Транзакции"
                    else -> "Приложение"
                })
            })
        },
        content = { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = "currencies"
            ) {
                composable("currencies") {
                    CurrenciesScreen(
                        navController = navController,
                        modifier = Modifier.padding(innerPadding)
                    )
                }

                composable("trading") {
                    TradingScreen(
                        navController = navController,
                        modifier = Modifier.padding(innerPadding)
                    )
                }

                composable("transactions") {
                    TransactionsScreen(
                        navController = navController,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        },
        bottomBar = {
            BottomAppBar(
                content = {
                    Row {

                    }
                }
            )
        }
    )
}