package com.alphagamingarcade.core.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NetworkAuthResponse(
    @SerialName("success")
    val success: Boolean,
    @SerialName("message")
    val message: String,
    @SerialName("data")
    val data: AuthData,
    @SerialName("statusCode")
    val statusCode: Int
)

@Serializable
data class AuthData(
    @SerialName("sub")
    val sub: Int,
    @SerialName("refreshToken")
    val refreshToken: String?,
    @SerialName("accessToken")
    val accessToken: String?,
    @SerialName("user")
    val user: NetworkUser,
    @SerialName("member")
    val member: NetworkMember? = null
)



@Serializable
data class LoginRequest(
    @SerialName("email")
    val email: String,
    @SerialName("password")
    val password: String
)

@Serializable
data class RegisterRequest(
    @SerialName("email")
    val email: String,
    @SerialName("password")
    val password: String,
    @SerialName("confirmPassword")
    val confirmPassword: String
)



