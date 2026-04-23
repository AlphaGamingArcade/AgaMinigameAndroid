package com.alphagamingarcade.core.data.repository

import com.alphagamingarcade.model.data.Transaction
import com.alphagamingarcade.model.data.TransactionFreeDepositStatus

interface TransactionRepository {
    suspend fun getTransactions(memberId: Int, pageNumber: Int, pageSize: Int): List<Transaction>

    suspend fun getTransactionFreeDepositStatus(memberId: Int): TransactionFreeDepositStatus

    suspend fun claimTransactionFreeDeposit(memberId: Int): TransactionFreeDepositStatus
}