package org.qstuff.currencyconverter

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.qstuff.currencyconverter.di.appModule


class CurrencyConverterApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        // For now, we only initialize koin here
        // Would also the place to initialize other stuff that needs Application context
        // e.g. a logger or a crash reporting library

        startKoin {
            androidContext(this@CurrencyConverterApplication)
            modules(
                appModule
            )
        }
    }
}