package com.alphagamingarcade.core.common.result

import kotlin.code

data class FieldError(
    val type: String,
    val message: String,
    val code: String,
    val field: String? = null,
)

sealed class AppResult<out T> {
    data class Success<T>(val data: T) : AppResult<T>()

    data class Error(
        val message: String,
        val errors: List<FieldError>? = null
    ) : AppResult<Nothing>()
}
