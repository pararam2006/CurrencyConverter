package com.example.currencyconverter.data.manager

import com.example.currencyconverter.data.dataSource.remote.RemoteRatesServiceImpl
import com.example.currencyconverter.data.dataSource.remote.dto.RateDto
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

object CurrencyRatesManager : CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO

    private val ratesService = RemoteRatesServiceImpl()
    private var baseCurrency = "RUB"
    private var amount = 1.0
    private val _rates = mutableListOf<RateDto>()
    val rates: List<RateDto>
        get() = _rates.toList()

    init {
        launch {
            while (true) {
                val newRates = ratesService.getRates(baseCurrency, amount)
                synchronized(_rates) {
                    _rates.clear()
                    _rates.addAll(newRates)
                }
                delay(1000) // Обновление каждую секунду
            }
        }
    }

    fun updateBaseCurrency(currency: String) {
        synchronized(_rates) {
            baseCurrency = currency
        }
    }

    fun updateAmount(amount: Double) {
        synchronized(_rates) {
            this.amount = amount
        }
    }

    fun getRate(from: String, to: String): Double? {
        synchronized(_rates) {
            val baseRate = _rates.find { it.currency == from }?.value ?: 1.0
            val targetRate = _rates.find { it.currency == to }?.value ?: 1.0
            return if (baseRate != 0.0) targetRate / baseRate else null
        }
    }
}