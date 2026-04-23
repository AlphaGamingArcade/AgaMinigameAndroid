package com.alphagamingarcade.core.network.data

import com.alphagamingarcade.core.di.IoDispatcher
import com.alphagamingarcade.core.network.api.GameRestApi
import com.alphagamingarcade.core.network.model.NetworkGame
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

internal class GameDataSourceImpl @Inject constructor(
    private val gameRestApi: GameRestApi,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : GameDataSource {

    override suspend fun getGames(): List<NetworkGame> {
        return try {
            val response = gameRestApi.getGames()
            return response.data.items
        } catch (e: Exception) {
            emptyList()
        }
    }

    override suspend fun getTrendingGames(): List<NetworkGame> {
        return try {
            val response = gameRestApi.getTrendingGames()
            return response.data.items
        } catch (e: Exception) {
            emptyList()
        }
    }

    override suspend fun getLatestGames(): List<NetworkGame> {
        return try {
            val response = gameRestApi.getLatestGames()
            return response.data.items
        } catch (e: Exception) {
            emptyList()
        }
    }

    override suspend fun getTopGames(): List<NetworkGame> {
        return try {
            val response = gameRestApi.getTopGames()
            return response.data.items
        } catch (e: Exception) {
            emptyList()
        }
    }

    override suspend fun getComingSoonGames(): List<NetworkGame> {
        return try {
            val response = gameRestApi.getComingSoonGames()
            return response.data.items
        } catch (e: Exception) {
            emptyList()
        }
    }
}