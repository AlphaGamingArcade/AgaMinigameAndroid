package com.alphagamingarcade.core.network.model

import com.alphagamingarcade.model.data.Banner
import kotlinx.serialization.Serializable

@Serializable
data class NetworkBanner(
    val id: Int,
    val title: String,
    val description: String,
    val image: String,
    val url: String,
    val order: Int,
    val datetime: String
)

fun NetworkBanner.toExternalModel() = Banner(
    id = id,
    title = title,
    imageUrl = image,
    description = description,
    isNew = true
)