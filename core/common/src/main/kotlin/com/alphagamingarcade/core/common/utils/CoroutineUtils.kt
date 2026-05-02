package com.alphagamingarcade.core.common.utils

import com.alphagamingarcade.core.common.exception.ApiException
import com.alphagamingarcade.core.common.result.AppResult
import com.alphagamingarcade.core.common.result.FieldError
import org.json.JSONObject
import kotlin.coroutines.cancellation.CancellationException


// Use this when you need errors from error fields
suspend inline fun <T> suspendRunAppResultCatching(
    crossinline block: suspend () -> T
): AppResult<T> {
    return try {
        AppResult.Success(block())
    } catch (cancellationException: CancellationException) {
        throw cancellationException
    } catch (e: ApiException) {
        AppResult.Error(
            message = e.message,
            errors = e.errors
        )
    } catch (exception: Exception) {
        AppResult.Error(
            message = "Unexpected error occur",
            errors = emptyList()
        )
    }
}

fun parseFieldErrors(errorBody: String?): List<FieldError> {
    if (errorBody == null) return listOf(
        FieldError(type = "error", message = "Bad Request", field = null)
    )

    return try {
        val json = JSONObject(errorBody)

        // Try to parse field-level errors array first e.g. "errors": [...]
        val errorsArray = json.optJSONArray("errors")
        if (errorsArray != null && errorsArray.length() > 0) {
            (0 until errorsArray.length()).map { i ->
                val obj = errorsArray.getJSONObject(i)
                FieldError(
                    type = obj.optString("type", "error"),
                    message = obj.optString("message", "Unknown error"),
                    field = obj.optString("field").ifEmpty { null },
                )
            }
        } else {
            // Fall back to top-level message e.g. "message": "Authentication failed"
            val message = json.optString("message", "Bad Request")
            listOf(FieldError(type = "error", message = message, field = null))
        }
    } catch (e: Exception) {
        listOf(FieldError(type = "error", message = "Bad Request", field = null))
    }
}