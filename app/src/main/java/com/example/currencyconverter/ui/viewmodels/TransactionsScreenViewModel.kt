package com.example.currencyconverter.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.currencyconverter.data.dataSource.room.transaction.dbo.TransactionDbo
import com.example.currencyconverter.data.repository.TransactionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class TransactionsScreenViewModel(
    private val repository: TransactionRepository
) : ViewModel() {
    private val _transactionsList = MutableStateFlow<List<TransactionDbo>>(emptyList())
    val transactionsList: StateFlow<List<TransactionDbo>> = _transactionsList

    init {
        viewModelScope.launch {
            repository.getAllTransactionsAsFlow().collectLatest { transactions ->
                _transactionsList.value = transactions
            }
        }
    }
}