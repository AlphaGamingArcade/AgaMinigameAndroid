package com.alphagamingarcade.core.network.data

import com.alphagamingarcade.core.network.api.TransactionRestApi
import com.alphagamingarcade.core.network.model.ApiResponse
import com.alphagamingarcade.core.network.model.NetworkFreeDepositRequest
import com.alphagamingarcade.core.network.model.NetworkFreeDepositStatus
import com.alphagamingarcade.core.network.model.NetworkTransaction
import com.alphagamingarcade.core.network.model.PaginatedResponse
import javax.inject.Inject

internal class TransactionDataSourceImpl @Inject constructor(
    private val transactionRestApi: TransactionRestApi
) : TransactionDataSource {
    override suspend fun getTransactions(
        memberId: Int,
        pageNumber: Int,
        pageSize: Int
    ): ApiResponse<PaginatedResponse<NetworkTransaction>> {
        return transactionRestApi.getTransactions(memberId, pageNumber, pageSize)
    }

    override suspend fun getTransactionFreeDeposit(memberId: Int): ApiResponse<NetworkFreeDepositStatus> {
       return transactionRestApi.getTransactionFreeDeposit(memberId)
    }

    override suspend fun claimTransactionFreeDeposit(memberId: Int): ApiResponse<NetworkFreeDepositStatus> {
        val request  = NetworkFreeDepositRequest(
            memberId = memberId
        )
        return transactionRestApi.claimTransactionFreeDepositStatus(request)
    }
}