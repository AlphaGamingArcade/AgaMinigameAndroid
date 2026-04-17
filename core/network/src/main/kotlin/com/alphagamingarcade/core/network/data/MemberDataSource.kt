package com.alphagamingarcade.core.network.data

import com.alphagamingarcade.core.network.model.NetworkAuthResponse
import com.alphagamingarcade.core.network.model.NetworkCreateMemberResponse

/**
 * Data source interface for Jetpack.
 */
interface MemberDataSource {
    suspend fun createMember(
        account: String,
        nickname: String,
        dateOfBirth: String
    ): Result<NetworkCreateMemberResponse>
}