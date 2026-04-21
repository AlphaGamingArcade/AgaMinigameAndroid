package com.alphagamingarcade.core.network.model

import kotlinx.serialization.Serializable

@Serializable
data class NetworkChangePasswordRequest(
    val currentPassword: String,
    val newPassword: String,
    val confirmPassword: String
)

@Serializable
data object NetworkChangePasswordResponse