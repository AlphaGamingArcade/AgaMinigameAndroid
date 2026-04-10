package com.alphagamingarcade.core.network.model

import kotlinx.serialization.Serializable

/**
 * Wrapper for API response with pagination data.
 */
@Serializable
data class ApiResponse<T>(
    val success: Boolean,
    val message: String,
    val data: T,
    val statusCode: Int,
)