package org.qstuff.currencyconverter.data

import kotlinx.serialization.Serializable
import org.qstuff.currencyconverter.domain.CurrencyRates
import java.math.BigDecimal


@Serializable
data class CurrencyRatesResponseDto(
    val date: String,
    val baseCurrency: String,
    val rates: Map<String, @Serializable(with = BigDecimalSerializer::class) BigDecimal>
)

fun CurrencyRatesResponseDto.toDomain() =
    CurrencyRates(
        date,
        baseCurrency,
        rates
    )