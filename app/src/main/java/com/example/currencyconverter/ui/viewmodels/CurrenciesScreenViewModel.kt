package com.example.currencyconverter.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.currencyconverter.data.dataSource.remote.RemoteRatesServiceImpl
import com.example.currencyconverter.data.dataSource.remote.dto.RateDto
import com.example.currencyconverter.domain.entity.Currency
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CurrenciesScreenViewModel: ViewModel() {
    private val ratesService = RemoteRatesServiceImpl()
    private val _rates = MutableStateFlow<List<RateDto>>(emptyList())
    val rates: StateFlow<List<RateDto>> = _rates

    private var selectedCurrency = Currency.RUB.name
    private var amount = 1.0

    init {
        viewModelScope.launch {
            while (true) {
                val newRates = ratesService.getRates(selectedCurrency, amount)
                _rates.value = newRates
                delay(1000)
            }
        }
    }

    fun selectCurrency(currency: String) {
        selectedCurrency = currency
    }

    fun updateAmount(newAmount: Double) {
        amount = newAmount
    }
}