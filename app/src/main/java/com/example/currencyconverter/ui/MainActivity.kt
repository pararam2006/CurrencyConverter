package com.example.currencyconverter.ui

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.example.currencyconverter.data.dataSource.room.AppDatabase
import com.example.currencyconverter.data.dataSource.room.account.dbo.AccountDbo
import com.example.currencyconverter.data.repository.AccountRepository
import com.example.currencyconverter.data.repository.TransactionRepository
import com.example.currencyconverter.ui.viewmodels.CurrenciesScreenViewModel
import com.example.currencyconverter.ui.viewmodels.CurrenciesScreenViewModelFactory
import com.example.currencyconverter.ui.viewmodels.TradingScreenViewModel
import com.example.currencyconverter.ui.viewmodels.TransactionsScreenViewModel
import com.example.currencyconverter.ui.viewmodels.TransactionsViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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

        // Инициализация начального баланса 75000 рублей при первом запуске
        CoroutineScope(Dispatchers.IO).launch {
            val initialAccounts = accountDao.getAll()
            if (initialAccounts.isEmpty()) {
                accountDao.insertAll(AccountDbo(code = "RUB", amount = 75000.0))
            }
        }

        val tradingScreenViewModel: TradingScreenViewModel by viewModels {
            object : androidx.lifecycle.ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    @Suppress("UNCHECKED_CAST")
                    return TradingScreenViewModel().apply {
                        initialize(accountRepository, transactionRepository)
                    } as T
                }
            }
        }

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