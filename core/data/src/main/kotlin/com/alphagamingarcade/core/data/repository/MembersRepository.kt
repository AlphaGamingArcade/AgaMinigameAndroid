package com.alphagamingarcade.core.data.repository

import com.alphagamingarcade.model.data.Game

interface MembersRepository {
    suspend fun createMember(
        account: String,
        nickname: String,
        dateOfBirth: String,
    ): Result<Unit>

    suspend fun updateMember(
        nickname: String
    ): Result<Unit>

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
}