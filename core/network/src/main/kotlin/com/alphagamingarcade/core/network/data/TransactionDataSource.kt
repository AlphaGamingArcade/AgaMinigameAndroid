package com.alphagamingarcade.core.network.data

import com.alphagamingarcade.core.network.model.NetworkFreeDepositStatus
import com.alphagamingarcade.core.network.model.NetworkTransaction

interface TransactionDataSource {
    suspend fun getTransactions(
        memberId: Int,
        pageNumber: Int,
        pageSize: Int
    ): List<NetworkTransaction>

    suspend fun getTransactionFreeDepositStatus(memberId: Int): NetworkFreeDepositStatus

    suspend fun claimTransactionFreeDeposit(memberId: Int): NetworkFreeDepositStatus
}