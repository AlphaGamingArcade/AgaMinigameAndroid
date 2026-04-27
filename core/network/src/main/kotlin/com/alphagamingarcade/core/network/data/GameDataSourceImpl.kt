package com.alphagamingarcade.core.network.data

import com.alphagamingarcade.core.di.IoDispatcher
import com.alphagamingarcade.core.network.api.GameRestApi
import com.alphagamingarcade.core.network.model.ApiResponse
import com.alphagamingarcade.core.network.model.NetworkGame
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

internal class GameDataSourceImpl @Inject constructor(
    private val gameRestApi: GameRestApi
) : GameDataSource {

    override suspend fun getGame(
        gameId: Int
    ): ApiResponse<NetworkGame> {
        return gameRestApi.getGame(
            gameId = gameId
        )
    }


    override suspend fun getGames(
        pageNumber: Int,
        pageSize: Int,
        category: String?,
        search: String?,
        sortBy: String?,
        orderBy: String?,
    ): List<NetworkGame> {
        val response = gameRestApi.getGames(
            pageNumber = pageNumber,
            pageSize = pageSize,
            category = category,
            search = search,
            sortBy = sortBy,
            orderBy = orderBy
        )
        return response.data.items
    }

    override suspend fun getTrendingGames(): List<NetworkGame> {
        val response = gameRestApi.getTrendingGames()
        return response.data.items
    }

    override suspend fun getLatestGames(): List<NetworkGame> {
        val response = gameRestApi.getLatestGames()
        return response.data.items
    }

    override suspend fun getTopGames(): List<NetworkGame> {
        val response = gameRestApi.getTopGames()
        return response.data.items
    }

    override suspend fun getComingSoonGames(): List<NetworkGame> {
        val response = gameRestApi.getComingSoonGames()
        return response.data.items
    }
}