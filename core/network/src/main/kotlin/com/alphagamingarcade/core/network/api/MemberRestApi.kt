package com.alphagamingarcade.core.network.api

import com.alphagamingarcade.core.network.model.ApiResponse
import com.alphagamingarcade.core.network.model.ApiResponseNullable
import com.alphagamingarcade.core.network.model.NetworkAddFavoriteRequest
import com.alphagamingarcade.core.network.model.NetworkAddFavoriteResponse
import com.alphagamingarcade.core.network.model.NetworkCreateMemberRequest
import com.alphagamingarcade.core.network.model.NetworkGame
import com.alphagamingarcade.core.network.model.NetworkMember
import com.alphagamingarcade.core.network.model.NetworkPlay
import com.alphagamingarcade.core.network.model.NetworkPlayRequest
import com.alphagamingarcade.core.network.model.NetworkRemoveFavoriteResponse
import com.alphagamingarcade.core.network.model.NetworkUpdateMemberRequest
import com.alphagamingarcade.core.network.model.NetworkUpdateMemberResponse
import com.alphagamingarcade.core.network.model.PaginatedResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface MemberRestApi {
    @GET("members/{memberId}")
    suspend fun getMember(
        @Path(value = "memberId") memberId: Int,
    ): ApiResponse<NetworkMember>

    @POST("members")
    suspend fun createMember(
        @Body request: NetworkCreateMemberRequest
    ): ApiResponse<NetworkMember>

    @PATCH("members/{memberId}")
    suspend fun updateMember(
        @Path("memberId") memberId: Int,
        @Body request: NetworkUpdateMemberRequest
    ): ApiResponseNullable<NetworkUpdateMemberResponse?>

    @GET("members/{memberId}/favorites")
    suspend fun getMemberFavorites(
        @Path("memberId") memberId: Int,
        @Query("pageNumber") pageNumber: Int,
        @Query("pageSize") pageSize: Int,
        @Query("category") category: String? = null,
        @Query("search") search: String? = null,
        @Query("sortBy") sortBy: String? = null,
        @Query("orderBy") orderBy: String? = null,
    ): ApiResponse<PaginatedResponse<NetworkGame>>

    @POST("members/{memberId}/favorites")
    suspend fun addMemberFavorite(
        @Path("memberId") memberId: Int,
        @Body request: NetworkAddFavoriteRequest
    ): ApiResponse<NetworkAddFavoriteResponse>

    @DELETE("members/{memberId}/favorites/{gameId}")
    suspend fun removeMemberFavorite(
        @Path("memberId") memberId: Int,
        @Path("gameId") gameId: Int,
    ): ApiResponseNullable<NetworkRemoveFavoriteResponse>

    @POST("members/{memberId}/favorites")
    suspend fun removeMemberFavorite(
        @Path("memberId") memberId: Int,
        @Query("pageNumber") pageNumber: Int,
        @Query("pageSize") pageSize: Int,
        @Query("category") category: String? = null,
        @Query("search") search: String? = null,
        @Query("sortBy") sortBy: String? = null,
        @Query("orderBy") orderBy: String? = null,
    ): ApiResponse<PaginatedResponse<NetworkGame>>

    @GET("members/{memberId}/plays")
    suspend fun getMemberPlays(
        @Path("memberId") memberId: Int,
        @Query("pageNumber") pageNumber: Int,
        @Query("pageSize") pageSize: Int,
        @Query("category") category: String? = null,
        @Query("search") search: String? = null,
        @Query("sortBy") sortBy: String? = null,
        @Query("orderBy") orderBy: String? = null,
    ): ApiResponse<PaginatedResponse<NetworkGame>>

    @POST("members/{memberId}/plays")
    suspend fun play(
        @Path("memberId") memberId: Int,
        @Body request: NetworkPlayRequest
    ): ApiResponse<NetworkPlay>
}