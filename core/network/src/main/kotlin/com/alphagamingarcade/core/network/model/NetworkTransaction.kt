package com.alphagamingarcade.core.network.model

import com.alphagamingarcade.model.data.Game
import com.alphagamingarcade.model.data.Transaction
import com.alphagamingarcade.model.data.TransactionFreeDepositStatus
import com.alphagamingarcade.model.data.TransactionStatus
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.Int

@Serializable
data class NetworkTransaction(
    @SerialName("id")
    val id: Int,

    @SerialName("memberId")
    val memberId: Int,

    @SerialName("amount")
    val amount: Double,

    @SerialName("type")
    val type: String,

    @SerialName("currency")
    val currency: String,

    @SerialName("datetime")
    val datetime: String,
)

@Serializable
data class NetworkFreeDepositStatus(
    @SerialName("claimed")
    val claimed: Boolean,

    @SerialName("amount")
    val amount: Double
)

@Serializable
data class NetworkFreeDepositRequest(
    @SerialName("memberId")
    val memberId: Int
)


fun NetworkTransaction.toExternalModel() = Transaction(
    id = id,
    memberId = memberId,
    amount = amount,
    type = type,
    status = TransactionStatus.SUCCESS,
    method = "",
    currency = currency,
    datetime = datetime
)

fun NetworkFreeDepositStatus.toExternalModel() = TransactionFreeDepositStatus(
    claimed = claimed,
    amount = amount
)