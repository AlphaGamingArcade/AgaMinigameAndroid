package com.alphagamingarcade.core.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data object NetworkRemoveFavoriteResponse

@Serializable
data class NetworkAddFavoriteResponse(
    @SerialName("id")
    val id: Int,
    val memberId: Int,
    val gameId: Int,
    val createdAt: String
)

@Serializable
data class NetworkAddFavoriteRequest(
    val gameId: Int
)