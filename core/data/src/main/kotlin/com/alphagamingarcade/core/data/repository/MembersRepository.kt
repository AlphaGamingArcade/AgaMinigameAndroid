package com.alphagamingarcade.core.data.repository

import com.alphagamingarcade.core.common.result.AppResult
import com.alphagamingarcade.core.network.model.NetworkPlay
import com.alphagamingarcade.model.data.Game
import com.alphagamingarcade.model.data.Play

interface MembersRepository {
    suspend fun createMember(
        account: String,
        nickname: String,
        dateOfBirth: String,
    ): AppResult<Unit>

    suspend fun updateMember(
        nickname: String
    ): AppResult<Unit>

    suspend fun getMemberFavorites(
        memberId: Int,
        pageNumber: Int,
        pageSize: Int
    ): Result<List<Game>>

    suspend fun getMemberRecentPlayed(
        memberId: Int,
        pageNumber: Int,
        pageSize: Int
    ): Result<List<Game>>

    suspend fun removeFavorite(
        memberId: Int,
        gameId: Int
    ): Result<Unit>

    suspend fun addFavorite(
        memberId: Int,
        gameId: Int
    ): Result<Unit>

    suspend fun playGame(
        memberId: Int,
        gameId: Int
    ): Result<Play>

}