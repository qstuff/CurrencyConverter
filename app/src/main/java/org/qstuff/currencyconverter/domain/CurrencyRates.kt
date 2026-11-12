package org.qstuff.currencyconverter.domain

import java.math.BigDecimal

data class CurrencyRates(
    val timestamp: String,
    val baseCurrencyCode: String,
    val rates: Map<String, BigDecimal>
)