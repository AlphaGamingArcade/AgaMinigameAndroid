package com.alphagamingarcade.core.network.data

import com.alphagamingarcade.core.network.model.ApiResponse
import com.alphagamingarcade.core.network.model.NetworkFreeDepositStatus
import com.alphagamingarcade.core.network.model.NetworkTransaction
import com.alphagamingarcade.core.network.model.PaginatedResponse

interface TransactionDataSource {
    suspend fun getTransactions(
        memberId: Int,
        pageNumber: Int,
        pageSize: Int
    ): ApiResponse<PaginatedResponse<NetworkTransaction>>

    suspend fun getTransactionFreeDeposit(memberId: Int): ApiResponse<NetworkFreeDepositStatus>

    suspend fun claimTransactionFreeDeposit(memberId: Int): ApiResponse<NetworkFreeDepositStatus>
}