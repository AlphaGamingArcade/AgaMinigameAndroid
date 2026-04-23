package com.alphagamingarcade.core.network.data

import com.alphagamingarcade.core.network.api.TransactionRestApi
import com.alphagamingarcade.core.network.model.NetworkFreeDepositRequest
import com.alphagamingarcade.core.network.model.NetworkFreeDepositStatus
import com.alphagamingarcade.core.network.model.NetworkTransaction
import javax.inject.Inject

internal class TransactionDataSourceImpl @Inject constructor(
    private val transactionRestApi: TransactionRestApi
) : TransactionDataSource {
    override suspend fun getTransactions(
        memberId: Int,
        pageNumber: Int,
        pageSize: Int
    ): List<NetworkTransaction> {
        return try {
            val response = transactionRestApi.getTransactions(memberId, pageNumber, pageSize)
            return response.data.items
        } catch (e: Exception) {
            emptyList()
        }
    }

    override suspend fun getTransactionFreeDepositStatus(memberId: Int): NetworkFreeDepositStatus {
        return try {
            val response = transactionRestApi.getTransactionFreeDepositStatus(memberId)
            return response.data
        } catch (e: Exception) {
            NetworkFreeDepositStatus(false, 0.0)
        }
    }

    override suspend fun claimTransactionFreeDeposit(memberId: Int): NetworkFreeDepositStatus {
        return try {
            val request  = NetworkFreeDepositRequest(
                memberId = memberId
            )
            val response = transactionRestApi.claimTransactionFreeDepositStatus(request)
            return response.data
        } catch (e: Exception) {
            NetworkFreeDepositStatus(false, 0.0)
        }
    }
}