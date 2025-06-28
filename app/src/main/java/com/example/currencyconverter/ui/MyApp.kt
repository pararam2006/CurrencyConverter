package com.example.currencyconverter.ui

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
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import com.example.currencyconverter.ui.screens.CurrenciesScreen
import com.example.currencyconverter.ui.screens.TradingScreen
import com.example.currencyconverter.ui.screens.TransactionsScreen
import com.example.currencyconverter.ui.viewmodels.CurrenciesScreenViewModel
import com.example.currencyconverter.ui.viewmodels.TradingScreenViewModel
import com.example.currencyconverter.ui.viewmodels.TransactionsScreenViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyApp(
        navController: NavHostController,
        currenciesScreenViewModel: CurrenciesScreenViewModel,
        transactionsScreenViewModel: TransactionsScreenViewModel,
        tradingScreenViewModel: TradingScreenViewModel
    ) {
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
                            else -> "Неизвестный экран"
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
                        vm = currenciesScreenViewModel,
                        navController = navController
                    )
                }

                composable("trading") {
                    TradingScreen(
                        modifier = Modifier.padding(innerPadding),
                        vm = tradingScreenViewModel
                    )
                }

                composable(
//                    "trading/{currencyFrom}/{currencyTo}/{currencyFromAmount}/{currencyToAmount}",
                    "trading/{currencyFrom}/{currencyTo}/{currencyFromAmount}",
                    arguments = listOf(
                        navArgument("currencyFrom") { type = NavType.StringType },
                        navArgument("currencyTo") { type = NavType.StringType },
                        navArgument("currencyFromAmount") { type = NavType.FloatType },
//                        navArgument("currencyToAmount") { type = NavType.FloatType }
                    )
                ) { backStackEntry ->
                    val currencyFrom = backStackEntry.arguments?.getString("currencyFrom") ?: ""
                    val currencyTo = backStackEntry.arguments?.getString("currencyTo") ?: ""
                    val currencyFromAmount = backStackEntry.arguments?.getFloat("currencyFromAmount")?.toDouble() ?: 1.0
//                    val currencyToAmount = backStackEntry.arguments?.getFloat("currencyToAmount")?.toDouble() ?: 1.0

                    TradingScreen(
                        modifier = Modifier.padding(innerPadding),
                        vm = tradingScreenViewModel,
                        currencyFrom = currencyFrom,
                        currencyTo = currencyTo,
                        currencyFromAmount = currencyFromAmount,
//                        currencyToAmount = currencyToAmount
                    )
                }

                composable("transactions") {
                    TransactionsScreen(
                        modifier = Modifier.padding(innerPadding),
                        vm = transactionsScreenViewModel,
                        navController = navController
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