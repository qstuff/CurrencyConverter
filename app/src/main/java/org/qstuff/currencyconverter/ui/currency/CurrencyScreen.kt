package org.qstuff.currencyconverter.ui.currency

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun CurrencyScreen(
    modifier: Modifier = Modifier,
    viewModel: CurrencyViewModel = koinViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    CurrencyContent(
        state = state,
        onSourceSelected = viewModel::onSourceSelected,
        onTargetSelected = viewModel::onTargetSelected,
        onAmountChanged = viewModel::onAmountChanged,
        onConvertButtonClicked = viewModel::onConvertButtonClicked,
        modifier = modifier
    )
}


@Composable
fun CurrencyContent(
    state: CurrencyViewModel.CurrencyUiState,
    onSourceSelected: (CurrencyViewModel.Currency) -> Unit,
    onTargetSelected: (CurrencyViewModel.Currency) -> Unit,
    onAmountChanged: (String) -> Unit,
    onConvertButtonClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 64.dp)
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Welcome to Currency Exchange",
            fontWeight = FontWeight.Bold,
            fontSize = 25.sp
        )
        Text(
            text = "Select Source Currency",
            fontWeight = FontWeight.Medium,
            fontSize = 20.sp,
            modifier = Modifier.padding(top = 24.dp)
        )
        SimpleDropdownList(
            values = state.currencies,
            selectedValue = state.selectedSource,
            onValueSelected = onSourceSelected,
            modifier = Modifier.padding(top = 16.dp)
        )
        Text(
            text = "Select Target Currency",
            fontWeight = FontWeight.Medium,
            fontSize = 20.sp,
            modifier = Modifier.padding(top = 24.dp)
        )
        SimpleDropdownList(
            values = state.currencies,
            selectedValue = state.selectedTarget,
            onValueSelected = onTargetSelected,
            modifier = Modifier.padding(top = 16.dp)
        )
        Text(
            text = "Enter Amount To Convert",
            fontWeight = FontWeight.Medium,
            fontSize = 20.sp,
            modifier = Modifier.padding(top = 24.dp)
        )
        OutlinedTextField(
            value = state.amountInput,
            onValueChange = onAmountChanged,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            singleLine = true,
            modifier = Modifier.padding(top = 24.dp)
        )
        Button(
            onClick = onConvertButtonClicked,
            enabled = true,
            modifier = Modifier.padding(top = 24.dp)
        ) {
            Text(text = "Convert")
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SimpleDropdownList(
    values: List<CurrencyViewModel.Currency>,
    selectedValue: CurrencyViewModel.Currency?,
    onValueSelected: (CurrencyViewModel.Currency) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = modifier) {
        OutlinedButton(onClick = { expanded = true }) {
            Text(text = selectedValue?.name ?: "Nothing yet selected")
            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            values.forEach { value ->
                DropdownMenuItem(
                    text = { Text(text = value.name) },
                    onClick = {
                        onValueSelected(value)
                        expanded = false
                    }
                )
            }
        }
    }
}