package com.alphagamingarcade.core.network.data

import com.alphagamingarcade.core.network.model.ApiResponse
import com.alphagamingarcade.core.network.model.ApiResponseNullable
import com.alphagamingarcade.core.network.model.NetworkMember
import com.alphagamingarcade.core.network.model.NetworkUpdateMemberResponse

/**
 * Data source interface for Jetpack.
 */
interface MemberDataSource {
    suspend fun createMember(
        account: String,
        nickname: String,
        dateOfBirth: String
    ): Result<ApiResponse<NetworkMember>>

    suspend fun updateMember(
        memberId: Int,
        nickname: String
    ): Result<ApiResponseNullable<NetworkUpdateMemberResponse?>>
}