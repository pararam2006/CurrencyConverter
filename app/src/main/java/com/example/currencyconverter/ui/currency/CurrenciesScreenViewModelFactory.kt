package com.example.currencyconverter.ui.currency

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.currencyconverter.data.repository.AccountRepository

class CurrenciesScreenViewModelFactory(
    private val repository: AccountRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CurrenciesScreenViewModel(repository) as T
    }
}