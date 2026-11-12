package org.qstuff.currencyconverter.data

import kotlinx.serialization.Serializable


@Serializable
data class CurrencyCodeResponseDto(
    val currencyCodes: Map<String, String>
)