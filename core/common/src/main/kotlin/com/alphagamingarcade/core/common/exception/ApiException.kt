package com.alphagamingarcade.core.common.exception

import com.alphagamingarcade.core.common.result.FieldError

class ApiException(
    override val message: String,
    val statusCode: Int? = null,
    val errors: List<FieldError>? = null
) : Exception(message) {

    /**
     * Returns the first field error message (useful for quick UI display)
     */
    fun firstErrorMessage(): String? {
        return errors?.firstOrNull()?.message
    }

    /**
     * Returns error message for a specific field
     */
    fun getErrorForField(field: String): String? {
        return errors?.firstOrNull { it.field == field }?.message
    }

    /**
     * Check if this is a network-related error (no status code)
     */
    fun isNetworkError(): Boolean {
        return statusCode == null
    }

    override fun toString(): String {
        return "ApiException(statusCode=$statusCode, message=$message, errors=$errors)"
    }
}