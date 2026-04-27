package com.alphagamingarcade.core.network.api

import com.alphagamingarcade.core.network.model.ApiResponse
import com.alphagamingarcade.core.network.model.NetworkFreeDepositRequest
import com.alphagamingarcade.core.network.model.NetworkFreeDepositStatus
import com.alphagamingarcade.core.network.model.NetworkTransaction
import com.alphagamingarcade.core.network.model.PaginatedResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface TransactionRestApi {
    @GET("/transactions")
    suspend fun getTransactions(
        @Query ("memberId") memberId: Int,
        @Query ("pageNumber") pageNumber: Int,
        @Query ("pageSize") pageSize: Int
    ): ApiResponse<PaginatedResponse<NetworkTransaction>>

    @GET("/transactions/free-deposit")
    suspend fun getTransactionFreeDeposit(
        @Query("memberId") memberId: Int
    ): ApiResponse<NetworkFreeDepositStatus>

    @POST("/transactions/free-deposit")
    suspend fun claimTransactionFreeDepositStatus(
        @Body request: NetworkFreeDepositRequest
    ): ApiResponse<NetworkFreeDepositStatus>
}