package com.alphagamingarcade.core.data.repository

import com.alphagamingarcade.core.network.data.TransactionDataSource
import com.alphagamingarcade.core.network.model.toExternalModel
import com.alphagamingarcade.model.data.Transaction
import com.alphagamingarcade.model.data.TransactionFreeDeposit
import javax.inject.Inject

class TransactionRepositoryImpl @Inject constructor (
    private  val transactionDataSource: TransactionDataSource
) : TransactionRepository
{
    override suspend fun getTransactions(
        memberId: Int,
        pageNumber: Int,
        pageSize: Int
    ): List<Transaction> {
        return transactionDataSource
            .getTransactions(memberId, pageNumber, pageSize)
            .data
            .items
            .map { it.toExternalModel() }
    }

    override suspend fun getTransactionFreeDeposit(memberId: Int): TransactionFreeDeposit {
        return transactionDataSource.getTransactionFreeDeposit(memberId)
            .data
            .toExternalModel()
    }

    override suspend fun claimTransactionFreeDeposit(memberId: Int): TransactionFreeDeposit {
        return transactionDataSource.claimTransactionFreeDeposit(memberId)
            .data
            .toExternalModel()
    }
}