package com.alphagamingarcade.core.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class NetworkForgotPasswordRequest(
    @SerialName("email")
    val email: String
)

@Serializable
data object NetworkForgotPassword
