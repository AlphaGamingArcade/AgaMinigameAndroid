package com.alphagamingarcade.core.network.data

import com.alphagamingarcade.core.network.api.BannerRestApi
import com.alphagamingarcade.core.network.model.ApiResponse
import com.alphagamingarcade.core.network.model.NetworkBanner
import com.alphagamingarcade.core.network.model.PaginatedResponse
import javax.inject.Inject


internal class BannerDataSourceImpl @Inject constructor(
    private val bannerRestApi: BannerRestApi
) : BannerDataSource {

    override suspend fun getBanners(): ApiResponse<PaginatedResponse<NetworkBanner>> {
        return bannerRestApi.getBanners()
    }
}
