package org.qstuff.currencyconverter.di

import org.koin.dsl.module
import org.qstuff.currencyconverter.data.CurrencyExchangeRatesApi
import org.qstuff.currencyconverter.data.CurrencyExchangeRepositoryImpl
import org.qstuff.currencyconverter.domain.CurrencyExchangeRepository

/** Koin Modules
 *
 * For now, we put all in here
 * Later we might add separate modules for domain and data
 */
val appModule = module {

    // Data Layer
    single { CurrencyExchangeRatesApi() }
    single<CurrencyExchangeRepository> { CurrencyExchangeRepositoryImpl(get()) }
}
