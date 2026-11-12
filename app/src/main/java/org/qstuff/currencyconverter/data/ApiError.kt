package org.qstuff.currencyconverter.data

sealed class ApiError(
    message: String,
    cause: Throwable? = null
) : Exception(message, cause) {
    // Timeouts, etc.
    class Network(cause: Throwable) : ApiError("Network error", cause)
    // Http status 4xx/5xx are thrown as Exceptions by HttpClient
    class Http(status: Int) : ApiError("HTTP $status")
    // Exceptions while parsing the response JSON
    class Parsing(cause: Throwable) : ApiError("Unexpected response", cause)
    // Any other Exception will be mapped here
    class Unknown(cause: Throwable) : ApiError("Unknown error", cause)
}