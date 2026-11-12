package org.qstuff.currencyconverter.domain

import java.math.BigDecimal
import java.math.MathContext
import kotlin.let

class CurrencyConverter {

    fun convert(
        sourceCurrencyCode: String,
        targetCurrencyCode: String,
        amount: String,
        euroRates: Map<String, BigDecimal>
    ) : BigDecimal? {

        amount.trim()
            .replace(',', '.')
            .toBigDecimalOrNull()?.let {
                val sourceRate = euroRates[sourceCurrencyCode] ?: return null
                val targetRate = euroRates[targetCurrencyCode] ?: return null
                return (it * targetRate).divide(sourceRate, MathContext.DECIMAL64)
        }
        return null
    }
}