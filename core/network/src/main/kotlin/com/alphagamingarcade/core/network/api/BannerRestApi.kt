package com.alphagamingarcade.core.network.api

import com.alphagamingarcade.core.network.model.ApiResponse
import com.alphagamingarcade.core.network.model.NetworkBanner
import com.alphagamingarcade.core.network.model.PaginatedResponse
import retrofit2.http.GET

interface BannerRestApi {
    @GET("/banners")
    suspend fun getBanners(): ApiResponse<PaginatedResponse<NetworkBanner>>
}