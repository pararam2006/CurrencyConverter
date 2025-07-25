package com.example.currencyconverter.ui.trading

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.currencyconverter.R
import com.example.currencyconverter.data.dataSource.room.account.dbo.AccountDbo
import com.example.currencyconverter.data.dataSource.room.transaction.dbo.TransactionDbo
import com.example.currencyconverter.data.repository.AccountRepository
import com.example.currencyconverter.data.repository.TransactionRepository
import com.example.currencyconverter.data.manager.CurrencyRatesManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.LocalDateTime

class TradingScreenViewModel : ViewModel() {
    private lateinit var accountRepository: AccountRepository
    private lateinit var transactionRepository: TransactionRepository
    private var _currencyFrom = ""
    var currencyFrom: String
        get() = _currencyFrom
        set(value) {
            _currencyFrom = value
        }

    private var _amountFrom = 1.0
    var amountFrom: Double
        get() = _amountFrom
        set(value) {
            _amountFrom = value
        }

    private var _currencyTo = ""
    var currencyTo: String
        get() = _currencyTo
        set(value) {
            _currencyTo = value
        }

    private var _amountTo = 1.0
    var amountTo: Double
        get() = _amountTo
        set(value) {
            _amountTo = value
        }

    private val _balances = MutableStateFlow<Map<String, Double>>(emptyMap())
    val balances: StateFlow<Map<String, Double>> = _balances

    fun initialize(accountRepository: AccountRepository, transactionRepository: TransactionRepository) {
        this.accountRepository = accountRepository
        this.transactionRepository = transactionRepository
        loadBalances()
    }

    private fun loadBalances() {
        viewModelScope.launch {
            val accounts = accountRepository.getAllAccounts()
            _balances.value = accounts.associate { it.code to it.amount }
        }
    }

    fun getRate(from: String, to: String): Double? {
        return CurrencyRatesManager.getRate(from, to)
    }

    fun getBalance(currency: String): Double {
        return balances.value[currency] ?: 0.0 // Безопасный доступ к балансу
    }

    fun trade() {
        viewModelScope.launch {
            val fromAccount = accountRepository.getAllAccounts().find { it.code == currencyFrom }
            val toAccount = accountRepository.getAllAccounts().find { it.code == currencyTo }

            if (fromAccount != null && fromAccount.amount >= amountFrom) {
                val newFromAmount = fromAccount.amount - amountFrom
                val newToAmount = (toAccount?.amount ?: 0.0) + amountTo

                accountRepository.insertAllAccounts(
                    AccountDbo(code = currencyFrom, amount = BigDecimal(newFromAmount).setScale(5, RoundingMode.HALF_EVEN).toDouble()),
                    AccountDbo(code = currencyTo, amount = BigDecimal(newToAmount).setScale(5, RoundingMode.HALF_EVEN).toDouble())
                )

                val rate = CurrencyRatesManager.getRate(currencyFrom, currencyTo) ?: 1.0
                val transaction = TransactionDbo(
                    id = 0,
                    from = currencyFrom,
                    to = currencyTo,
                    fromAmount = amountFrom,
                    toAmount = amountTo,
                    dateTime = LocalDateTime.now()
                )
                transactionRepository.insertAllTransactions(transaction)
                loadBalances() // Обновляем балансы после обмена
            }
        }
    }

    fun getCountryIcon(currency: String): Int {
        when (currency) {
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

    fun getCurrencyFullName(currency: String): String {
        when (currency) {
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

    fun getCurrencySymbol(currency: String): String {
        when (currency) {
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
}