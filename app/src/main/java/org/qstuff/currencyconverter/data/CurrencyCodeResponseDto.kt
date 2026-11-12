package org.qstuff.currencyconverter.data

import kotlinx.serialization.Serializable
import org.qstuff.currencyconverter.domain.CurrencyCodes


@Serializable
data class CurrencyCodeResponseDto(
    val currencyCodes: Map<String, String>
)

fun CurrencyCodeResponseDto.toDomain() =
    CurrencyCodes(currencyCodes)