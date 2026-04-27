package com.alphagamingarcade.core.network.data

import com.alphagamingarcade.core.network.model.ApiResponse
import com.alphagamingarcade.core.network.model.NetworkBanner
import com.alphagamingarcade.core.network.model.PaginatedResponse

/**
 * Data source interface for Jetpack.
 */
interface BannerDataSource {
    suspend fun getBanners(): ApiResponse<PaginatedResponse<NetworkBanner>>
}
