package org.qstuff.currencyconverter.data

import android.util.Log
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import io.ktor.client.plugins.logging.Logger
import kotlinx.serialization.json.JsonObject


class CurrencyExchangeRatesApi {

    private companion object {

        // For details of the API Service we use here
        // see: https://github.com/fawazahmed0/exchange-api
        //
        // The generic URL structure is
        // https://cdn.jsdelivr.net/npm/@fawazahmed0/currency-api@{date}/{apiVersion}/{endpoint}
        // To simplify things we use the following base URL with date and version already included
        const val BASE_URL = "https://cdn.jsdelivr.net/npm/@fawazahmed0/currency-api@latest/v1"
    }

    private val httpClient = HttpClient(CIO) {

        // Throw Exceptions for status codes 4xx and 5xx, so we can handle the accordingly
        // will be mapped to ApiError.kt
        expectSuccess = true

        defaultRequest {
            url("$BASE_URL/")
        }

        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
            })
        }
        install(HttpTimeout) {
            requestTimeoutMillis = 30_000
            connectTimeoutMillis = 15_000
        }
        install(Logging) {
            level = LogLevel.HEADERS
            logger = object : Logger {
                override fun log(message: String) {
                    Log.d("CurrencyVM: HttpClient", message)
                }
            }
        }
    }

    suspend fun getCurrencyCodes(): Map<String, String> =
        httpClient.get("currencies.json").body()

    suspend fun getCurrencyRates(currencyCode: String): JsonObject =
        httpClient.get("currencies/$currencyCode.json").body()

}