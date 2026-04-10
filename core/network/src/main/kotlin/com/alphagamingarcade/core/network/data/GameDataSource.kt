package com.alphagamingarcade.core.network.data

import com.alphagamingarcade.core.network.model.NetworkGame

interface GameDataSource {
    suspend fun getGames(): List<NetworkGame>
    suspend fun getTrendingGames(): List<NetworkGame>
    suspend fun getLatestGames(): List<NetworkGame>
    suspend fun getTopGames(): List<NetworkGame>
    suspend fun getComingSoonGames(): List<NetworkGame>
}