package com.example.currencyconverter.ui.viewmodels

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.currencyconverter.R
import com.example.currencyconverter.data.dataSource.remote.RemoteRatesServiceImpl
import com.example.currencyconverter.data.dataSource.remote.dto.RateDto
import com.example.currencyconverter.data.dataSource.room.account.dbo.AccountDbo
import com.example.currencyconverter.data.repository.AccountRepository
import com.example.currencyconverter.domain.entity.Currency
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CurrenciesScreenViewModel(
    private val repository: AccountRepository
): ViewModel() {
    private val accountsList = mutableStateListOf<AccountDbo>()
    private val ratesService = RemoteRatesServiceImpl()
    private val _rates = MutableStateFlow<List<RateDto>>(emptyList())
    val rates: StateFlow<List<RateDto>> = _rates

    private var _selectedCurrency = Currency.RUB.name
    var selectedCurrency: String
        get() = _selectedCurrency
        set(value) {
            _selectedCurrency = value
        }

    private var _amount = 1.0
    var amount: Double
        get() = _amount
        set(value) {
            _amount = value
        }

    private var _selectedCurrencyToTrade = Currency.USD.name
    var selectedCurrencyToTrade: String
        get() = _selectedCurrencyToTrade
        set(value) {
            _selectedCurrencyToTrade = value
        }

    init {
        viewModelScope.launch {
            while (true) {
                val newRates = ratesService.getRates(_selectedCurrency, _amount)
                _rates.value = newRates
                delay(1000)
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
        when(currency) {
            "USD" -> return "US Dollar"
            "GBP" -> return "Great Britain Pound"
            "EUR" -> return "Euro"
            "AUD" -> return "Australian Dollar"
            "BGN" -> return "Bulgarian Lev"
            "BRL" -> return "Brazilian Real"
            "CAD" -> return "Canadian Dollar"
            "CHF" -> return "Swiss Franc"
            "CNY" -> return "Chinese Yuan"
            "CZK" -> return "Czech Koruna"
            "DKK" -> return "Danish Krone"
            "HKD" -> return "Hong Kong Dollar"
            "HRK" -> return "Croatian Kuna"
            "HUF" -> return "Hungarian Forint"
            "IDR" -> return "Indonesian Rupiah"
            "ILS" -> return "Israeli New Shekel"
            "INR" -> return "Indian Rupee"
            "ISK" -> return "Icelandic Krona"
            "JPY" -> return "Japanese Yen"
            "KRW" -> return "South Korean Won"
            "MXN" -> return "Mexican Peso"
            "MYR" -> return "Malaysian Ringgit"
            "NOK" -> return "Norwegian Krone"
            "NZD" -> return "New Zealand Dollar"
            "PHP" -> return "Philippine Peso"
            "PLN" -> return "Polish Zloty"
            "RON" -> return "Romanian Leu"
            "RUB" -> return "Russia Ruble"
            "SEK" -> return "Swedish Krona"
            "SGD" -> return "Singapore Dollar"
            "THB" -> return "Thai Baht"
            "TRY" -> return "Turkish Lira"
            "ZAR" -> return "South African Rand"
            else -> return "Unknown currency"
        }
    }

    fun getCountryIcon(currency: String): Int {
        when(currency) {
            "USD" -> return R.drawable.icon_united_states
            "GBP" -> return R.drawable.icon_united_kingdom
            "EUR" -> return R.drawable.icon_european_union
            "AUD" -> return R.drawable.icon_australia
            "BGN" -> return R.drawable.icon_bulgaria
            "BRL" -> return R.drawable.icon_brazil
            "CAD" -> return R.drawable.icon_canada
            "CHF" -> return R.drawable.icon_switzerland
            "CNY" -> return R.drawable.icon_china
            "CZK" -> return R.drawable.icon_czech_republic
            "DKK" -> return R.drawable.icon_denmark
            "HKD" -> return R.drawable.icon_hong_kong
            "HRK" -> return R.drawable.icon_horvatia
            "HUF" -> return R.drawable.icon_hungary
            "IDR" -> return R.drawable.icon_indonesia
            "ILS" -> return R.drawable.icon_israel
            "INR" -> return R.drawable.icon_india
            "ISK" -> return R.drawable.icon_iceland
            "JPY" -> return R.drawable.icon_japan
            "KRW" -> return R.drawable.icon_south_korea
            "MXN" -> return R.drawable.icon_mexico
            "MYR" -> return R.drawable.icon_malaysia
            "NOK" -> return R.drawable.icon_norway
            "NZD" -> return R.drawable.icon_new_zealand
            "PHP" -> return R.drawable.icon_philippines
            "PLN" -> return R.drawable.icon_poland
            "RON" -> return R.drawable.icon_romania
            "RUB" -> return R.drawable.icon_russia
            "SEK" -> return R.drawable.icon_sweden
            "SGD" -> return R.drawable.icon_singapore
            "THB" -> return R.drawable.icon_thailand
            "TRY" -> return R.drawable.icon_turkey
            "ZAR" -> return R.drawable.icon_south_africa
            else -> return R.drawable.icon_unknown
        }
    }

    fun getCurrencySymbol(currency: String): String {
        when(currency) {
            "USD" -> return "\$"
            "GBP" -> return "£"
            "EUR" -> return "€"
            "AUD" -> return "AU\$"
            "BGN" -> return ""
            "BRL" -> return "R\$"
            "CAD" -> return "\$"
            "CHF" -> return "Fr"
            "CNY" -> return "¥"
            "CZK" -> return "Kč"
            "DKK" -> return "Kr"
            "HKD" -> return "HK\$"
            "HRK" -> return "Kn"
            "HUF" -> return "Ft"
            "IDR" -> return "Rs"
            "ILS" -> return "₪"
            "INR" -> return "₹"
            "ISK" -> return "kr"
            "JPY" -> return "¥"
            "KRW" -> return "₩"
            "MXN" -> return "\$"
            "MYR" -> return "RM"
            "NOK" -> return "NKr"
            "NZD" -> return "\$"
            "PHP" -> return "₱"
            "PLN" -> return "zł"
            "RON" -> return "L"
            "RUB" -> return "₽"
            "SEK" -> return "kr"
            "SGD" -> return "S\$"
            "THB" -> return "฿"
            "TRY" -> return "₺"
            "ZAR" -> return "R"
            else -> return "Unknown"
        }
    }

    suspend fun loadAccounts() {
        val loadedAccounts = repository.getAllAccounts()
        accountsList.clear()
        accountsList.addAll(loadedAccounts)
    }
}