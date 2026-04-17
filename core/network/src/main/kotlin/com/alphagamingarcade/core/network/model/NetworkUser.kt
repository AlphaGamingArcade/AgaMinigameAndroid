package com.alphagamingarcade.core.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NetworkUser(
    @SerialName("id")
    val id: Int,
    @SerialName("email")
    val email: String,
    @SerialName("isEmailVerified")
    val isEmailVerified: Boolean
)

