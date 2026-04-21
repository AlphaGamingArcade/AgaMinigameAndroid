package com.alphagamingarcade.core.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NetworkEmailStatus(
    @SerialName("isVerified")
    val isVerified: Boolean,
    @SerialName("datetime")
    val datetime: String?
)
