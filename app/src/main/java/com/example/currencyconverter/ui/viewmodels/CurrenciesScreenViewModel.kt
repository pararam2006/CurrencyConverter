package com.example.currencyconverter.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.currencyconverter.R
import com.example.currencyconverter.data.dataSource.remote.RemoteRatesServiceImpl
import com.example.currencyconverter.data.dataSource.remote.dto.RateDto
import com.example.currencyconverter.data.dataSource.room.account.dbo.AccountDbo
import com.example.currencyconverter.data.repository.AccountRepository
import com.example.currencyconverter.domain.entity.Currency
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class CurrenciesScreenViewModel(
    private val repository: AccountRepository
) : ViewModel() {
    private val _accounts = MutableStateFlow<List<AccountDbo>>(emptyList())
    val accounts: StateFlow<List<AccountDbo>> = _accounts

    private val _rates = MutableStateFlow<List<RateDto>>(emptyList())
    val rates: StateFlow<List<RateDto>> = _rates

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val ratesService = RemoteRatesServiceImpl()
    private var _selectedCurrency = Currency.EUR.name
    var selectedCurrency: String
        get() = _selectedCurrency
        set(value) {
            _selectedCurrency = value
            loadRates()
        }

    private var _amount = 1.0
    var amount: Double
        get() = _amount
        set(value) {
            _amount = value
            loadRates()
        }

    private var _selectedCurrencyToTrade = Currency.USD.name
    var selectedCurrencyToTrade: String
        get() = _selectedCurrencyToTrade
        set(value) {
            _selectedCurrencyToTrade = value
        }

    init {
        viewModelScope.launch {
            loadInitialData()
            startRatesUpdate()
            observeAccounts() // Начинаем наблюдение за изменениями в базе
        }
    }

    private suspend fun loadInitialData() {
        _isLoading.value = true
        try {
            val loadedAccounts = repository.getAllAccounts() // Начальная загрузка для быстрого старта
            _accounts.value = loadedAccounts
        } finally {
            _isLoading.value = false
        }
    }

    private fun observeAccounts() {
        viewModelScope.launch {
            repository.getAllAccountsAsFlow().collectLatest { accounts ->
                _accounts.value = accounts // Автоматическое обновление при изменении данных
            }
        }
    }

    private fun loadRates() {
        viewModelScope.launch {
            val newRates = ratesService.getRates(_selectedCurrency, _amount)
            _rates.value = newRates
        }
    }

    private fun startRatesUpdate() {
        viewModelScope.launch {
            while (true) {
                loadRates()
                kotlinx.coroutines.delay(1000)
            }
        }
    }

    fun selectCurrency(currency: String) {
        _selectedCurrency = currency
    }

    fun updateAmount(newAmount: Double) {
        _amount = newAmount
    }

    fun getCurrencyFullName(currency: String): String {
        return when (currency) {
            "USD" -> "US Dollar"
            "GBP" -> "Great Britain Pound"
            "EUR" -> "Euro"
            "AUD" -> "Australian Dollar"
            "BGN" -> "Bulgarian Lev"
            "BRL" -> "Brazilian Real"
            "CAD" -> "Canadian Dollar"
            "CHF" -> "Swiss Franc"
            "CNY" -> "Chinese Yuan"
            "CZK" -> "Czech Koruna"
            "DKK" -> "Danish Krone"
            "HKD" -> "Hong Kong Dollar"
            "HRK" -> "Croatian Kuna"
            "HUF" -> "Hungarian Forint"
            "IDR" -> "Indonesian Rupiah"
            "ILS" -> "Israeli New Shekel"
            "INR" -> "Indian Rupee"
            "ISK" -> "Icelandic Krona"
            "JPY" -> "Japanese Yen"
            "KRW" -> "South Korean Won"
            "MXN" -> "Mexican Peso"
            "MYR" -> "Malaysian Ringgit"
            "NOK" -> "Norwegian Krone"
            "NZD" -> "New Zealand Dollar"
            "PHP" -> "Philippine Peso"
            "PLN" -> "Polish Zloty"
            "RON" -> "Romanian Leu"
            "RUB" -> "Russia Ruble"
            "SEK" -> "Swedish Krona"
            "SGD" -> "Singapore Dollar"
            "THB" -> "Thai Baht"
            "TRY" -> "Turkish Lira"
            "ZAR" -> "South African Rand"
            else -> "Unknown currency"
        }
    }

    fun getCountryIcon(currency: String): Int {
        return when (currency) {
            "USD" -> R.drawable.icon_united_states
            "GBP" -> R.drawable.icon_united_kingdom
            "EUR" -> R.drawable.icon_european_union
            "AUD" -> R.drawable.icon_australia
            "BGN" -> R.drawable.icon_bulgaria
            "BRL" -> R.drawable.icon_brazil
            "CAD" -> R.drawable.icon_canada
            "CHF" -> R.drawable.icon_switzerland
            "CNY" -> R.drawable.icon_china
            "CZK" -> R.drawable.icon_czech_republic
            "DKK" -> R.drawable.icon_denmark
            "HKD" -> R.drawable.icon_hong_kong
            "HRK" -> R.drawable.icon_horvatia
            "HUF" -> R.drawable.icon_hungary
            "IDR" -> R.drawable.icon_indonesia
            "ILS" -> R.drawable.icon_israel
            "INR" -> R.drawable.icon_india
            "ISK" -> R.drawable.icon_iceland
            "JPY" -> R.drawable.icon_japan
            "KRW" -> R.drawable.icon_south_korea
            "MXN" -> R.drawable.icon_mexico
            "MYR" -> R.drawable.icon_malaysia
            "NOK" -> R.drawable.icon_norway
            "NZD" -> R.drawable.icon_new_zealand
            "PHP" -> R.drawable.icon_philippines
            "PLN" -> R.drawable.icon_poland
            "RON" -> R.drawable.icon_romania
            "RUB" -> R.drawable.icon_russia
            "SEK" -> R.drawable.icon_sweden
            "SGD" -> R.drawable.icon_singapore
            "THB" -> R.drawable.icon_thailand
            "TRY" -> R.drawable.icon_turkey
            "ZAR" -> R.drawable.icon_south_africa
            else -> R.drawable.icon_unknown
        }
    }

    fun getCurrencySymbol(currency: String): String {
        return when (currency) {
            "USD" -> "\$"
            "GBP" -> "£"
            "EUR" -> "€"
            "AUD" -> "AU\$"
            "BGN" -> ""
            "BRL" -> "R\$"
            "CAD" -> "\$"
            "CHF" -> "Fr"
            "CNY" -> "¥"
            "CZK" -> "Kč"
            "DKK" -> "Kr"
            "HKD" -> "HK\$"
            "HRK" -> "Kn"
            "HUF" -> "Ft"
            "IDR" -> "Rs"
            "ILS" -> "₪"
            "INR" -> "₹"
            "ISK" -> "kr"
            "JPY" -> "¥"
            "KRW" -> "₩"
            "MXN" -> "\$"
            "MYR" -> "RM"
            "NOK" -> "NKr"
            "NZD" -> "\$"
            "PHP" -> "₱"
            "PLN" -> "zł"
            "RON" -> "L"
            "RUB" -> "₽"
            "SEK" -> "kr"
            "SGD" -> "S\$"
            "THB" -> "฿"
            "TRY" -> "₺"
            "ZAR" -> "R"
            else -> "Unknown"
        }
    }
}