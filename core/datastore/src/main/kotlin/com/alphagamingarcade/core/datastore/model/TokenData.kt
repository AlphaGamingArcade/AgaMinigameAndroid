package com.alphagamingarcade.core.datastore.model

import kotlinx.serialization.Serializable

@Serializable
data class TokenData(
    val accessToken: String? = null,
    val refreshToken: String? = null,
)
