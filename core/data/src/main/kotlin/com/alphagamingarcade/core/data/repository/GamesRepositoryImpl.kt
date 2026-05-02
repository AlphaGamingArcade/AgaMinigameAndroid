package com.alphagamingarcade.core.data.repository

import com.alphagamingarcade.core.network.data.GameDataSource
import com.alphagamingarcade.core.network.model.toExternalModel
import com.alphagamingarcade.core.utils.suspendRunCatching
import com.alphagamingarcade.model.data.Game
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GamesRepositoryImpl @Inject constructor(private val gameDataSource: GameDataSource): GamesRepository {
    override suspend fun getGame(gameId: Int): Result<Game> {
        return suspendRunCatching {
            gameDataSource.getGame(gameId).data.toExternalModel()
        }
    }

    override suspend fun getSimilarGames(
        gameId: Int,
        pageNumber: Int,
        pageSize: Int,
        search: String?,
        sortBy: String?,
        orderBy: String?,
    ): Result<List<Game>> {
        return suspendRunCatching {
            gameDataSource.getSimilarGames(
                gameId,
                pageNumber,
                pageSize,
                search,
                sortBy,
                orderBy
            ).data.items.map { it.toExternalModel() }
        }
    }

    override fun getGames(
        pageNumber: Int,
        pageSize: Int,
        category: String?,
        search: String?,
        sortBy: String?,
        orderBy: String?,
    ): Flow<List<Game>> = flow {
        val games = gameDataSource.getGames(
            pageNumber = pageNumber,
            pageSize = pageSize,
            category = category,
            search = search,
            sortBy = sortBy,
            orderBy = orderBy,
        );
        emit(games.map { it.toExternalModel() })
    }

    override fun getTrendingGames(): Flow<List<Game>> = flow {
        val games = gameDataSource.getTrendingGames()
        emit(games.map { it.toExternalModel() })
    }

    override fun getLatestGames(): Flow<List<Game>> = flow {
        val games = gameDataSource.getLatestGames();
        emit(games.map { it.toExternalModel() })
    }

    override fun getTopGames(): Flow<List<Game>> = flow {
        val games = gameDataSource.getTopGames();
        emit(games.map { it.toExternalModel() })
    }

    override fun getComingSoonGames(): Flow<List<Game>> = flow {
        val games = gameDataSource.getComingSoonGames();
        emit(games.map { it.toExternalModel() })
    }
}