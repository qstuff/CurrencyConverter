package org.qstuff.currencyconverter.ui.currency

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.qstuff.currencyconverter.domain.CurrencyExchangeRepository
import java.math.BigDecimal


/**
 * Manage the UiState for the CurrencyExchangeScreen
 * Handle user interaction and perform the currency calculation
 * Retrieve the required data from CurrencyExchangeRepository
 */
class CurrencyViewModel(
    private val currencyExchangeRepository: CurrencyExchangeRepository
) : ViewModel() {

    private companion object {
        const val DEFAULT_RATE_CODE = "eur"
    }

    data class Currency(
        val code: String,
        val name: String
    )

    data class CurrencyUiState(
        val currencies: List<Currency> = emptyList(),
        val selectedSource: Currency? = null,
        val selectedTarget: Currency? = null,
        val amountInput: String = "",
        val calculationResult: String? = null,
        val isLoading: Boolean = false,
        // for now, we just pass an error message in case. null for no error
        val errorMessage: String? = null
    )

    // We store rates at least for the lifetime of this ViewModel resp. the associated View
    // It's not used for the UiState but for calculation
    // NOTE: if it's required to show the excange rate to the user in a later version
    // we should add another field to the CurrencyUiState
    private var latestRatesMap: Map<String, BigDecimal> = emptyMap()

    private val _uiState = MutableStateFlow(CurrencyUiState())
    val uiState: StateFlow<CurrencyUiState> = _uiState.asStateFlow()


    init {
        loadData()
    }

    private fun loadData() {

        viewModelScope.launch {

            _uiState.update {
                it.copy(
                    isLoading = true,
                    errorMessage = null
                )
            }

            val codesDeferred = async { currencyExchangeRepository.loadCurrencyCodes() }
            val ratesDeferred = async { currencyExchangeRepository.loadCurrencyRates(DEFAULT_RATE_CODE) }

            val codesResult = codesDeferred.await()
            val ratesResult = ratesDeferred.await()

            codesResult.onFailure { Log.e("CurrencyVM", "Codes failed", it) }
            ratesResult.onFailure { Log.e("CurrencyVM", "Rates failed", it) }

            val codesError = codesResult.exceptionOrNull()
            val ratesError = ratesResult.exceptionOrNull()

            ratesResult.getOrNull()?.let {
                latestRatesMap = it.rates ?: emptyMap()
            }

            _uiState.update {
                it.copy(
                    isLoading = false,
                    currencies = codesResult.getOrNull()?.currencyCodes?.map { mapEntry ->
                        Currency(mapEntry.key, mapEntry.value)
                    }?.sortedBy { currency ->
                        currency.name
                    } ?: emptyList(),
                    errorMessage = codesError?.message ?: ratesError?.message
                )
            }
        }
    }

    // User Input

    fun onSourceSelected(
        selectedSourceCurrency: Currency
    ) {
        _uiState.update {
            it.copy(
                selectedSource = selectedSourceCurrency,
                calculationResult = null
            )
        }
    }

    fun onTargetSelected(
        selectedTargetCurrency: Currency
    ) {
        _uiState.update {
            it.copy(
                selectedTarget = selectedTargetCurrency,
                calculationResult = null
            )
        }
    }

    fun onAmountChanged(
        amount: String
    ) {
        _uiState.update {
            it.copy(
                amountInput = amount,
                calculationResult = null
            )
        }
    }

    fun onConvertButtonClicked() {
        // TODO: convert currency
    }
}