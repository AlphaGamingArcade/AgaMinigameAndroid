package com.alphagamingarcade.core.network.data

import com.alphagamingarcade.core.network.model.NetworkBanner

/**
 * Data source interface for Jetpack.
 */
interface BannerDataSource {
    suspend fun getBanners(): List<NetworkBanner>
}
