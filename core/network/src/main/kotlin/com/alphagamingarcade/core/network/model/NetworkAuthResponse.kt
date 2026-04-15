package com.alphagamingarcade.core.network.model

import com.alphagamingarcade.core.datastore.model.PreferencesUserProfile
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
    val refreshToken: String,
    @SerialName("accessToken")
    val accessToken: String,
    @SerialName("user")
    val user: NetworkAuthUser,
    @SerialName("member")
    val member: String? = null
)

/**
 * Wrapper for API response with pagination data.
 */
@Serializable
data class NetworkAuthUser(
    @SerialName("id")
    val id: Int,
    @SerialName("email")
    val email: String,
    @SerialName("isEmailVerified")
    val isEmailVerified: Boolean
)

fun NetworkAuthUser.asPreferencesUserProfile() = PreferencesUserProfile(
    id = id.toString(),
    userEmail = email,
    isEmailVerified = isEmailVerified
)
