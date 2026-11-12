package org.qstuff.currencyconverter.data

import io.ktor.client.plugins.ResponseException
import io.ktor.serialization.ContentConvertException
import okio.IOException
import org.qstuff.currencyconverter.domain.CurrencyCodes
import org.qstuff.currencyconverter.domain.CurrencyExchangeRepository
import org.qstuff.currencyconverter.domain.CurrencyRates
import java.util.concurrent.CancellationException

import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

class CurrencyExchangeRepositoryImpl(
    private val currencyExchangeRatesApi: CurrencyExchangeRatesApi
) : CurrencyExchangeRepository {

    override suspend fun loadCurrencyCodes(): Result<CurrencyCodes> {
        return safeCall {
            CurrencyCodes(
                currencyExchangeRatesApi.getCurrencyCodes()
            )
        }
    }

    override suspend fun loadCurrencyRates(currencyCode: String): Result<CurrencyRates> {
        return safeCall {
            val json = currencyExchangeRatesApi.getCurrencyRates(currencyCode)
            val date = json["date"]?.jsonPrimitive?.content
            val ratesJson = json[currencyCode]?.jsonObject
            val rates = ratesJson?.mapValues { (_, value) -> value.jsonPrimitive.content.toBigDecimal() }

            CurrencyRates(
                date =  date,
                baseCurrencyCode = currencyCode,
                rates = rates
            )
        }
    }

    private suspend fun <T> safeCall(callBlock: suspend () -> T): Result<T> =
        try {
            Result.success(callBlock())
        } catch (e: CancellationException) {
            throw e
        } catch (e: ResponseException) {
            Result.failure(ApiError.Http(e.response.status.value))
        } catch (e: ContentConvertException) {
            Result.failure(ApiError.Parsing(e))
        } catch (e: IOException) {
            Result.failure(ApiError.Network(e))
        } catch (e: Exception) {
            Result.failure(ApiError.Unknown(e))
        }
}