package com.alphagamingarcade.core.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class NetworkEmailStatusResponse(
    @SerialName("success")
    val success: Boolean,
    @SerialName("message")
    val message: String,
    @SerialName("data")
    val data: NetworkEmailStatus,
    @SerialName("statusCode")
    val statusCode: Int
)

@Serializable
data class NetworkEmailStatus(
    @SerialName("isVerified")
    val isVerified: Boolean,
    @SerialName("datetime")
    val datetime: String?
)
