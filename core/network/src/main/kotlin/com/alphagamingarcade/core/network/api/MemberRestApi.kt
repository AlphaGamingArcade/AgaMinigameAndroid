package com.alphagamingarcade.core.network.api

import com.alphagamingarcade.core.network.model.NetworkCreateMemberRequest
import com.alphagamingarcade.core.network.model.NetworkCreateMemberResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface MemberRestApi {
    @POST("members")
    suspend fun createMember(
        @Body request: NetworkCreateMemberRequest
    ): NetworkCreateMemberResponse
}