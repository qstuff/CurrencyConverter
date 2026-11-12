# CurrencyConverter

A small app that converts currencies using the current daily exchange rates
This app uses a public API to retrieve the exchange rates: See https://github.com/fawazahmed0/exchange-api

## Running the app

Open the project in Android Studio and run the app configuration on a device or emulator with internet access.
Min SDK is 24

## Tech Stack

- KTOR client with kotlinx.serialization
- Jetpack Compose (Material 3)
- Koin for dependency injection

## Architecture

- MVVM with a single StateFlow<UiState> in the ViewModel. Ui only sends events.
- Lightweight layering by package (data, domain, ui). No separate use cases, given the size of the app.

## Decisions
- Cross rates via EUR, one request instead of one per currency
- BigDecimal for amounts and rates
- suspend + Result in the repository instead of Flow
- Dynamic JSON key parsed via JsonObject

## Open issues and possible next steps

### Testing (not done within the time limit)
- Converter unit tests: EUR → USD, USD → EUR, cross rate, same currency, comma as decimal separator, invalid input, unknown currency, precision of the division
- Parsing of the API responses, including missing keys
- Repository tests with Ktor’s MockEngine (requires injecting the HttpClient via Koin instead of creating it in the API class)

### Functionality and UI

- Filter out currencies without a name or without a rate
- Show error messages on UI, plus a retry button
- Searchable currency selection (the list has 300+ entries)
- Caching the last known rates (with date) for offline use

## Misc

Note that this README was written after the 3h time limit.
