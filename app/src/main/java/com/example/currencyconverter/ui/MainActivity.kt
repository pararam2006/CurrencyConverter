package com.example.currencyconverter.ui

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.fragment.app.FragmentActivity
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.example.currencyconverter.data.dataSource.room.AppDatabase
import com.example.currencyconverter.data.repository.AccountRepository
import com.example.currencyconverter.data.repository.TransactionRepository
import com.example.currencyconverter.ui.viewmodels.CurrenciesScreenViewModel
import com.example.currencyconverter.ui.viewmodels.CurrenciesScreenViewModelFactory
import com.example.currencyconverter.ui.viewmodels.TradingScreenViewModel
import com.example.currencyconverter.ui.viewmodels.TransactionsScreenViewModel
import com.example.currencyconverter.ui.viewmodels.TransactionsViewModelFactory

class MainActivity : FragmentActivity() {
    private lateinit var transactionRepository: TransactionRepository
    private lateinit var accountRepository: AccountRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "app_database.db"
        )
            .fallbackToDestructiveMigration()
            .build()
        val transactionDao = db.transactionDao()
        transactionRepository = TransactionRepository(transactionDao)
        val accountDao = db.accountDao()
        accountRepository = AccountRepository(accountDao)

        val tradingScreenViewModel: TradingScreenViewModel by viewModels()

        val currenciesScreenViewModel: CurrenciesScreenViewModel by viewModels {
            CurrenciesScreenViewModelFactory(repository = accountRepository)
        }

        val transactionsScreenViewModel: TransactionsScreenViewModel by viewModels {
            TransactionsViewModelFactory(repository = transactionRepository)
        }

        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            MyApp(
                navController = navController,
                currenciesScreenViewModel = currenciesScreenViewModel,
                transactionsScreenViewModel = transactionsScreenViewModel,
                tradingScreenViewModel = tradingScreenViewModel
            )
        }
    }
}