package com.alphagamingarcade.core.network.data

import com.alphagamingarcade.core.network.model.ApiResponse
import com.alphagamingarcade.core.network.model.ApiResponseNullable
import com.alphagamingarcade.core.network.model.NetworkAddFavoriteResponse
import com.alphagamingarcade.core.network.model.NetworkGame
import com.alphagamingarcade.core.network.model.NetworkMember
import com.alphagamingarcade.core.network.model.NetworkPlay
import com.alphagamingarcade.core.network.model.NetworkRemoveFavoriteResponse
import com.alphagamingarcade.core.network.model.NetworkUpdateMemberResponse
import com.alphagamingarcade.core.network.model.PaginatedResponse

/**
 * Data source interface for Jetpack.
 */
interface MemberDataSource {
    suspend fun createMember(
        account: String,
        nickname: String,
        dateOfBirth: String
    ): ApiResponse<NetworkMember>

    suspend fun updateMember(
        memberId: Int,
        nickname: String
    ): ApiResponseNullable<NetworkUpdateMemberResponse?>

    suspend fun getMemberFavorites(
        memberId: Int,
        pageNumber: Int,
        pageSize: Int,
        category: String? = null,
        search: String? = null,
        sortBy: String? = null,
        orderBy: String? = null,
    ): ApiResponse<PaginatedResponse<NetworkGame>>

    suspend fun getMemberRecentPlayed(
        memberId: Int,
        pageNumber: Int,
        pageSize: Int,
        category: String? = null,
        search: String? = null,
        sortBy: String? = null,
        orderBy: String? = null,
    ): ApiResponse<PaginatedResponse<NetworkGame>>

    suspend fun addMemberFavorite(
        memberId: Int,
        gameId: Int
    ): ApiResponse<NetworkAddFavoriteResponse>

    suspend fun removeMemberFavorite(
        memberId: Int,
        gameId: Int
    ): ApiResponseNullable<NetworkRemoveFavoriteResponse>

    suspend fun playGame(
        memberId: Int,
        gameId: Int
    ): ApiResponse<NetworkPlay>
}