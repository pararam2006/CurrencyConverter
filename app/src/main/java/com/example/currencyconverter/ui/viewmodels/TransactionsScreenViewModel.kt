package com.example.currencyconverter.ui.viewmodels

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.currencyconverter.data.dataSource.room.transaction.dbo.TransactionDbo
import com.example.currencyconverter.data.repository.TransactionRepository
import kotlinx.coroutines.launch

class TransactionsScreenViewModel(
    private val repository: TransactionRepository
): ViewModel() {
    val transactionsList = mutableStateListOf<TransactionDbo>()

    init {
        viewModelScope.launch {
            loadTransactions()
        }
    }

    private suspend fun loadTransactions() {
        val loadedTransactions = repository.getAllTransactions()
        transactionsList.clear()
        transactionsList.addAll(loadedTransactions)
    }
}