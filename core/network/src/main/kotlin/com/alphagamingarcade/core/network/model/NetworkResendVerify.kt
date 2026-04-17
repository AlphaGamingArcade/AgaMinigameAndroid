package com.alphagamingarcade.core.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResendVerifyEmailRequest(
    @SerialName("email")
    val email: String,
)

@Serializable
data class NetworkResendVerifyEmailResponse(
    @SerialName("success")
    val success: Boolean,
    @SerialName("message")
    val message: String,
    @SerialName("data")
    val data: AuthData? = null,
    @SerialName("statusCode")
    val statusCode: Int
)
