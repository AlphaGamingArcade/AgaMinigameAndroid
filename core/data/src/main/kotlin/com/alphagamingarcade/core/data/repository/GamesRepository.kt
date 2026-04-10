package com.alphagamingarcade.core.data.repository

import com.alphagamingarcade.model.data.Game
import kotlinx.coroutines.flow.Flow

interface GamesRepository {
    fun getGames():Flow<List<Game>>

    fun getTrendingGames(): Flow<List<Game>>

    fun getLatestGames(): Flow<List<Game>>

    fun getTopGames(): Flow<List<Game>>

    fun getComingSoonGames(): Flow<List<Game>>
}