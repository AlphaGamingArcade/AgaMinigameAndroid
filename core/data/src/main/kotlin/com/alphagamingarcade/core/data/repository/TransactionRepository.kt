package com.alphagamingarcade.core.data.repository

import com.alphagamingarcade.model.data.Transaction
import com.alphagamingarcade.model.data.TransactionFreeDeposit

interface TransactionRepository {
    suspend fun getTransactions(memberId: Int, pageNumber: Int, pageSize: Int): List<Transaction>

    suspend fun getTransactionFreeDeposit(memberId: Int): TransactionFreeDeposit

    suspend fun claimTransactionFreeDeposit(memberId: Int): TransactionFreeDeposit
}