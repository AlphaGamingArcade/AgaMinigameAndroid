package com.alphagamingarcade.core.network.model

import com.alphagamingarcade.model.data.Banner
import kotlinx.serialization.Serializable
import kotlin.String

@Serializable
data class NetworkBanner(
    val id: Int,
    val title: String,
    val description: String,
    val image: String,
    val actionType: String,
    val actionValue: String,
    val order: Int,
    val isActive: Boolean,
    val startDatetime: String?,
    val endDatetime: String?,
    val createdAt: String?,
    val updatedAt:  String?
)

fun NetworkBanner.toExternalModel() = Banner(
    id = id,
    title = title,
    imageUrl = image,
    description = description,
    actionType = actionType,
    actionValue = actionValue,
    order = order,
    isActive = isActive,
    startDatetime = startDatetime,
    endDatetime = endDatetime,
    createdAt = createdAt,
    updatedAt = updatedAt
)