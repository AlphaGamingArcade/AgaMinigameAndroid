package com.alphagamingarcade.core.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResendVerifyEmailRequest(
    @SerialName("email")
    val email: String,
)

@Serializable
data object NetworkResendVerifyEmail