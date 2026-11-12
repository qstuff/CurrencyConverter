package org.qstuff.currencyconverter.domain


interface CurrencyExchangeRepository {

    suspend fun loadCurrencyCodes(): Result<CurrencyCodes>

    suspend fun loadCurrencyRates(currencyCode: String) : Result<CurrencyRates>
}