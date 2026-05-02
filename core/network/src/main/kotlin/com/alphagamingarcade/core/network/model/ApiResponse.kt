package com.alphagamingarcade.core.network.model

import kotlinx.serialization.Serializable

@Serializable
data class ApiError(
    val type: String,
    val message: String,
    val field: String? = null,
)

/**
 * Wrapper for API response with pagination data.
 */
@Serializable
data class ApiResponse<T>(
    val success: Boolean,
    val message: String,
    val data: T,
    val statusCode: Int,
    val errors: List<ApiError>? = null,
)

@Serializable
data class ApiResponseNullable<T>(
    val success: Boolean,
    val message: String,
    val data: T? = null,  // ← nullable here only
    val statusCode: Int,
    val errors: List<ApiError>? = null,
)