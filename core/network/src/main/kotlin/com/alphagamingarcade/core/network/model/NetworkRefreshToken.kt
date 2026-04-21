package com.alphagamingarcade.core.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NetworkRefreshToken(
    @SerialName("sub")
    val sub: Int,
    @SerialName("refreshToken")
    val refreshToken: String,
    @SerialName("accessToken")
    val accessToken: String,
    @SerialName("user")
    val user: NetworkUser? = null,
    @SerialName("member")
    val member: NetworkMember? = null
)

@Serializable
data class NetworkRefreshTokenRequest(
    @SerialName("refreshToken")
    val refreshToken: String
)