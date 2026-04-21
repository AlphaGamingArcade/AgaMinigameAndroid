package com.alphagamingarcade.core.network.data

import com.alphagamingarcade.core.network.api.MemberRestApi
import com.alphagamingarcade.core.network.model.NetworkCreateMemberRequest

import android.net.http.HttpException
import com.alphagamingarcade.core.di.IoDispatcher
import com.alphagamingarcade.core.network.model.ApiResponse
import com.alphagamingarcade.core.network.model.ApiResponseNullable
import com.alphagamingarcade.core.network.model.NetworkMember
import com.alphagamingarcade.core.network.model.NetworkUpdateMemberRequest
import com.alphagamingarcade.core.network.model.NetworkUpdateMemberResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject

internal class MemberDataSourceImpl @Inject constructor(
    private val memberRestApi: MemberRestApi,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : MemberDataSource {
    override suspend fun createMember(
        account: String,
        nickname: String,
        dateOfBirth: String
    ): Result<ApiResponse<NetworkMember>> = withContext(ioDispatcher) {
        try {
            val request = NetworkCreateMemberRequest(
                account = account,
                nickname = nickname,
                dateOfBirth = dateOfBirth
            )
            val response = memberRestApi.createMember(request)

            Result.success(response)
        } catch (e: HttpException) {
            Result.failure(e) // 4xx/5xx errors (wrong password, not found, etc.)
        } catch (e: IOException) {
            Result.failure(e) // No internet, timeout
        }
    }

    override suspend fun updateMember(
        memberId: Int,
        nickname: String
    ): Result<ApiResponseNullable<NetworkUpdateMemberResponse?>> = withContext(ioDispatcher) {
        try {
            val request = NetworkUpdateMemberRequest(
                nickname = nickname
            )
            val response = memberRestApi.updateMember(memberId, request)
            Result.success(response)

        } catch (e: HttpException) {
            Result.failure(e) // 4xx/5xx errors (wrong password, not found, etc.)
        } catch (e: IOException) {
            Result.failure(e) // No internet, timeout
        }
    }
}