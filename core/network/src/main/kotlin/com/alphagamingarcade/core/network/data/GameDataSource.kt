package com.alphagamingarcade.core.network.data

import com.alphagamingarcade.core.network.model.ApiResponse
import com.alphagamingarcade.core.network.model.NetworkGame

interface GameDataSource {
    suspend fun getGame(
        gameId: Int
    ): ApiResponse<NetworkGame>

    suspend fun getGames(
        pageNumber: Int,
        pageSize: Int,
        category: String? = null,
        search: String? = null,
        sortBy: String? = null,
        orderBy: String? = null,
    ): List<NetworkGame>
    suspend fun getTrendingGames(): List<NetworkGame>
    suspend fun getLatestGames(): List<NetworkGame>
    suspend fun getTopGames(): List<NetworkGame>
    suspend fun getComingSoonGames(): List<NetworkGame>

}