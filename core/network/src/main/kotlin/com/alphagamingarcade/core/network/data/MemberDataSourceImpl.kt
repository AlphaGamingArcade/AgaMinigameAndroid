package com.alphagamingarcade.core.network.data

import com.alphagamingarcade.core.network.api.MemberRestApi
import com.alphagamingarcade.core.network.model.NetworkCreateMemberRequest

import com.alphagamingarcade.core.network.model.ApiResponse
import com.alphagamingarcade.core.network.model.ApiResponseNullable
import com.alphagamingarcade.core.network.model.NetworkAddFavoriteRequest
import com.alphagamingarcade.core.network.model.NetworkAddFavoriteResponse
import com.alphagamingarcade.core.network.model.NetworkGame
import com.alphagamingarcade.core.network.model.NetworkMember
import com.alphagamingarcade.core.network.model.NetworkRemoveFavoriteResponse
import com.alphagamingarcade.core.network.model.NetworkUpdateMemberRequest
import com.alphagamingarcade.core.network.model.NetworkUpdateMemberResponse
import com.alphagamingarcade.core.network.model.PaginatedResponse
import javax.inject.Inject

internal class MemberDataSourceImpl @Inject constructor(
    private val memberRestApi: MemberRestApi
) : MemberDataSource {
    override suspend fun createMember(
        account: String,
        nickname: String,
        dateOfBirth: String
    ): ApiResponse<NetworkMember> {
        val request = NetworkCreateMemberRequest(
            account = account,
            nickname = nickname,
            dateOfBirth = dateOfBirth
        )
        return memberRestApi.createMember(request)
    }

    override suspend fun updateMember(
        memberId: Int,
        nickname: String
    ): ApiResponseNullable<NetworkUpdateMemberResponse?> {
        val request = NetworkUpdateMemberRequest(
            nickname = nickname
        )
        return memberRestApi.updateMember(memberId, request)
    }

    override suspend fun getMemberFavorites(
        memberId: Int,
        pageNumber: Int,
        pageSize: Int,
        category: String? ,
        search: String?,
        sortBy: String?,
        orderBy: String?,
    ): ApiResponse<PaginatedResponse<NetworkGame>> {
        return memberRestApi.getMemberFavorites(
            memberId = memberId,
            pageNumber = pageNumber,
            pageSize = pageSize,
            category = category,
            search = search,
            sortBy = sortBy,
            orderBy = orderBy
        )
    }

    override suspend fun getMemberRecentPlayed(
        memberId: Int,
        pageNumber: Int,
        pageSize: Int,
        category: String? ,
        search: String?,
        sortBy: String?,
        orderBy: String?,
    ): ApiResponse<PaginatedResponse<NetworkGame>> {
        return memberRestApi.getMemberPlays(
            memberId = memberId,
            pageNumber = pageNumber,
            pageSize = pageSize,
            category = category,
            search = search,
            sortBy = sortBy,
            orderBy = orderBy
        )
    }

    override suspend fun addMemberFavorite(
        memberId: Int,
        gameId: Int
    ): ApiResponse<NetworkAddFavoriteResponse> {
        return memberRestApi.addMemberFavorite(
            memberId,
            NetworkAddFavoriteRequest(gameId)
        )
    }

    override suspend fun removeMemberFavorite(
        memberId: Int,
        gameId: Int
    ): ApiResponseNullable<NetworkRemoveFavoriteResponse> {
        return memberRestApi.removeMemberFavorite(
            memberId,
            gameId
        )
    }
}