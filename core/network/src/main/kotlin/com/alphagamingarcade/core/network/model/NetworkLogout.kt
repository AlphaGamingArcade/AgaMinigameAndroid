package com.alphagamingarcade.core.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class NetworkLogoutRequest(
    @SerialName("refreshToken")
    val refreshToken: String
)

@Serializable
class NetworkLogoutResponse