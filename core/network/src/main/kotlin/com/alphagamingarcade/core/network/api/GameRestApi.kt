package com.alphagamingarcade.core.network.api

import com.alphagamingarcade.core.network.model.ApiResponse
import com.alphagamingarcade.core.network.model.NetworkGame
import com.alphagamingarcade.core.network.model.PaginatedResponse
import retrofit2.http.GET

interface GameRestApi {
    @GET("/games/games")
    suspend fun getGames(): ApiResponse<PaginatedResponse<NetworkGame>>
    @GET("/games/trending")
    suspend fun getTrendingGames(): ApiResponse<PaginatedResponse<NetworkGame>>

    @GET("/games/latest")
    suspend fun getLatestGames(): ApiResponse<PaginatedResponse<NetworkGame>>

    @GET("/games/top")
    suspend fun getTopGames(): ApiResponse<PaginatedResponse<NetworkGame>>

    @GET("/games/coming-soon")
    suspend fun getComingSoonGames(): ApiResponse<PaginatedResponse<NetworkGame>>
}
