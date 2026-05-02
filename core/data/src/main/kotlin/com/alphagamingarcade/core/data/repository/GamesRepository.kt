package com.alphagamingarcade.core.data.repository

import com.alphagamingarcade.model.data.Game
import kotlinx.coroutines.flow.Flow

interface GamesRepository {
    suspend fun getGame(gameId: Int): Result<Game>

    suspend fun getSimilarGames(
        gameId: Int,
        pageNumber: Int,
        pageSize: Int,
        search: String? = null,
        sortBy: String? = null,
        orderBy: String? = null,
    ): Result<List<Game>>


    fun getGames(
        pageNumber: Int,
        pageSize: Int,
        category: String? = null,
        search: String? = null,
        sortBy: String? = null,
        orderBy: String? = null,
    ):Flow<List<Game>>


    fun getTrendingGames(): Flow<List<Game>>

    fun getLatestGames(): Flow<List<Game>>

    fun getTopGames(): Flow<List<Game>>

    fun getComingSoonGames(): Flow<List<Game>>
}