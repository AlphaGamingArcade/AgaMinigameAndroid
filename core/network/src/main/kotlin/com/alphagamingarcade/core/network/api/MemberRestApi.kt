package com.alphagamingarcade.core.network.api

import com.alphagamingarcade.core.network.model.ApiResponse
import com.alphagamingarcade.core.network.model.ApiResponseNullable
import com.alphagamingarcade.core.network.model.NetworkCreateMemberRequest
import com.alphagamingarcade.core.network.model.NetworkMember
import com.alphagamingarcade.core.network.model.NetworkUpdateMemberRequest
import com.alphagamingarcade.core.network.model.NetworkUpdateMemberResponse
import retrofit2.http.Body
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface MemberRestApi {
    @POST("members")
    suspend fun createMember(
        @Body request: NetworkCreateMemberRequest
    ): ApiResponse<NetworkMember>

    @PATCH("members/{memberId}")
    suspend fun updateMember(
        @Path("memberId") memberId: Int,
        @Body request: NetworkUpdateMemberRequest
    ): ApiResponseNullable<NetworkUpdateMemberResponse?>
}