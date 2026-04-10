package com.alphagamingarcade.core.network.model

import kotlinx.serialization.Serializable

/**
 * Pagination wrapper for banner list.
 */
@Serializable
data class PaginatedResponse<T>(
    val items: List<T>,
    val pageNumber: Int,
    val pageSize: Int,
    val totalRecords: Int,
    val totalPages: Int,
    val hasNext: Boolean,
    val hasPrevious: Boolean,
)

