package com.alphagamingarcade.core.network.data

import com.alphagamingarcade.core.di.IoDispatcher
import com.alphagamingarcade.core.network.api.BannerRestApi
import com.alphagamingarcade.core.network.model.NetworkBanner
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject


internal class BannerDataSourceImpl @Inject constructor(
    private val bannerRestApi: BannerRestApi,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : BannerDataSource {

    override suspend fun getBanners(): List<NetworkBanner> {
        return try {
            bannerRestApi.getBanners()
                .data
                .items
        } catch (e: Exception) {
            emptyList()
        }
    }
}
