package com.example.currencyconverter.ui

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.fragment.app.FragmentActivity
import androidx.navigation.compose.rememberNavController
import com.example.currencyconverter.ui.viewmodels.CurrenciesScreenViewModel

class MainActivity : FragmentActivity() {
    private val _currenciesScreenViewModel: CurrenciesScreenViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            MyApp(navController, vm = _currenciesScreenViewModel)
        }
    }
}

