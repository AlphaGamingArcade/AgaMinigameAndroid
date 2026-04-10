package com.alphagamingarcade.core.data.repository

import com.alphagamingarcade.core.network.data.GameDataSource
import com.alphagamingarcade.core.network.model.toExternalModel
import com.alphagamingarcade.model.data.Game
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GamesRepositoryImpl @Inject constructor(private val gameDataSource: GameDataSource): GamesRepository {
    override fun getGames(): Flow<List<Game>> = flow {
        val games = gameDataSource.getGames();
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