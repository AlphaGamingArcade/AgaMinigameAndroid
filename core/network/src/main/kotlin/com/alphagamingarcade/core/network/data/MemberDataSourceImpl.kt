package com.alphagamingarcade.core.network.data

import com.alphagamingarcade.core.common.exception.ApiException
import com.alphagamingarcade.core.common.utils.parseFieldErrors
import com.alphagamingarcade.core.network.api.MemberRestApi
import com.alphagamingarcade.core.network.model.NetworkCreateMemberRequest

import com.alphagamingarcade.core.network.model.ApiResponse
import com.alphagamingarcade.core.network.model.ApiResponseNullable
import com.alphagamingarcade.core.network.model.NetworkAddFavoriteRequest
import com.alphagamingarcade.core.network.model.NetworkAddFavoriteResponse
import com.alphagamingarcade.core.network.model.NetworkGame
import com.alphagamingarcade.core.network.model.NetworkMember
import com.alphagamingarcade.core.network.model.NetworkPlay
import com.alphagamingarcade.core.network.model.NetworkPlayRequest
import com.alphagamingarcade.core.network.model.NetworkRemoveFavoriteResponse
import com.alphagamingarcade.core.network.model.NetworkUpdateMemberRequest
import com.alphagamingarcade.core.network.model.NetworkUpdateMemberResponse
import com.alphagamingarcade.core.network.model.PaginatedResponse
import retrofit2.HttpException
import javax.inject.Inject

internal class MemberDataSourceImpl @Inject constructor(
    private val memberRestApi: MemberRestApi
) : MemberDataSource {
    override suspend fun getMember(
        memberId: Int
    ): ApiResponse<NetworkMember> {
        return memberRestApi.getMember(
            memberId = memberId
        )
    }


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
        return try {
            memberRestApi.createMember(request)
        }  catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            throw ApiException(
                message = e.message() ?: "Unexpected error occur.",
                statusCode = e.code(),
                errors = parseFieldErrors(errorBody),
            )
        }
    }

    override suspend fun updateMember(
        memberId: Int,
        nickname: String
    ): ApiResponseNullable<NetworkUpdateMemberResponse?> {
       return try {
           val request = NetworkUpdateMemberRequest(
               nickname = nickname
           )
           memberRestApi.updateMember(memberId, request)
       } catch (e: HttpException) {
           val errorBody = e.response()?.errorBody()?.string()
           throw ApiException(
               message = e.message() ?: "Unexpected error occur.",
               statusCode = e.code(),
               errors = parseFieldErrors(errorBody),
           )
       }
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

    override suspend fun playGame(memberId: Int, gameId: Int): ApiResponse<NetworkPlay> {
       return memberRestApi.play(
           memberId,
           NetworkPlayRequest(gameId)
       )
    }
}