package com.alphagamingarcade.model.data

data class Transaction(
    val id: Int,
    val memberId: Int,
    val amount: Double,
    val type: String,
    val method: String,
    val status: TransactionStatus,
    val currency: String,
    val datetime: String,
)

data class TransactionFreeDepositStatus(
    val claimed: Boolean,
    val amount: Double
)

/**
 * Recharge transaction status.
 */
enum class TransactionStatus {
    SUCCESS,
    PENDING,
    FAILED,
}