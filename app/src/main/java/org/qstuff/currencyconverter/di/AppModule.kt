package org.qstuff.currencyconverter.di

import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import org.qstuff.currencyconverter.data.CurrencyExchangeRatesApi
import org.qstuff.currencyconverter.data.CurrencyExchangeRepositoryImpl
import org.qstuff.currencyconverter.domain.CurrencyExchangeRepository
import org.qstuff.currencyconverter.ui.currency.CurrencyViewModel

/** Koin Modules
 *
 * For now, we put all in here
 * Later we might add separate modules for domain and data
 */
val appModule = module {

    // App LAyer
    viewModel { CurrencyViewModel(get()) }

    // Data Layer
    single { CurrencyExchangeRatesApi() }
    single<CurrencyExchangeRepository> { CurrencyExchangeRepositoryImpl(get()) }
}
