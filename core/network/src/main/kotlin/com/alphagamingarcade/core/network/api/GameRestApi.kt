package com.alphagamingarcade.core.network.api

import com.alphagamingarcade.core.network.model.ApiResponse
import com.alphagamingarcade.core.network.model.NetworkGame
import com.alphagamingarcade.core.network.model.PaginatedResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GameRestApi {
    @GET("/games/{gameId}")
    suspend fun getGame(
        @Path("gameId") gameId: Int,
    ): ApiResponse<NetworkGame>

    @GET("/games")
    suspend fun getGames(
        @Query("pageNumber") pageNumber: Int,
        @Query("pageSize") pageSize: Int,
        @Query("category") category: String? = null,
        @Query("search") search: String? = null,
        @Query("sortBy") sortBy: String? = null,
        @Query("orderBy") orderBy: String? = null,
    ): ApiResponse<PaginatedResponse<NetworkGame>>
    @GET("/games/trending")
    suspend fun getTrendingGames(): ApiResponse<PaginatedResponse<NetworkGame>>

    @GET("/games/latest")
    suspend fun getLatestGames(): ApiResponse<PaginatedResponse<NetworkGame>>

    @GET("/games/top")
    suspend fun getTopGames(): ApiResponse<PaginatedResponse<NetworkGame>>

    @GET("/games/coming-soon")
    suspend fun getComingSoonGames(): ApiResponse<PaginatedResponse<NetworkGame>>
}
