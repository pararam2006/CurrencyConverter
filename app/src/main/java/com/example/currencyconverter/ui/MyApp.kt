package com.example.currencyconverter.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.currencyconverter.ui.screens.CurrenciesScreen
import com.example.currencyconverter.ui.screens.TradingScreen
import com.example.currencyconverter.ui.screens.TransactionsScreen
import com.example.currencyconverter.ui.viewmodels.CurrenciesScreenViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyApp(navController: NavHostController, vm: CurrenciesScreenViewModel) {
    val currentBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry.value?.destination?.route
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = when (currentRoute) {
                            "currencies" -> "Валюты"
                            "trading" -> "Обмен"
                            "transactions" -> "Транзакции"
                            else -> "Приложение"
                        },
                        fontSize = 30.sp,
                    )
                },
            )
        },
        content = { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = "currencies"
            ) {
                composable("currencies") {
                    CurrenciesScreen(
                        modifier = Modifier.padding(innerPadding),
                        vm = vm
                    )
                }

                composable("trading") {
                    TradingScreen(
                        modifier = Modifier.padding(innerPadding),
                    )
                }

                composable("transactions") {
                    TransactionsScreen(
                        modifier = Modifier.padding(innerPadding),
                    )
                }
            }
        },
        bottomBar = {
            BottomAppBar(
                content = {
                    Row {
                        Spacer(modifier = Modifier.weight(1F, fill = true) )

                        IconButton(
                            content = { Icon(Icons.Filled.Menu, contentDescription = "") },
                            onClick = {
                                if (currentRoute != "transactions") {
                                    navController.navigate("transactions")
                                }
                            },
                        )

                        IconButton(
                            content = { Icon(Icons.Filled.Home, contentDescription = "") },
                            onClick = {
                                if(currentRoute != "currencies") {
                                    navController.navigate("currencies")
                                }
                            },
                        )

                        IconButton(
                            content = { Icon(Icons.Filled.ShoppingCart, contentDescription = "") },
                            onClick = {
                                if (currentRoute != "trading") {
                                    navController.navigate("trading")
                                }
                            },
                        )

                        Spacer(modifier = Modifier.weight(1F, fill = true) )
                    }
                }
            )
        },
        modifier = Modifier.fillMaxSize()
    )
}

@SuppressLint("ViewModelConstructorInComposable")
@Preview
@Composable
fun MyAppPreview() {
    MyApp(navController = rememberNavController(), vm = CurrenciesScreenViewModel())
}