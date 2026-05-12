package com.alphagamingarcade.core.network.model

import com.alphagamingarcade.model.data.Banner
import com.alphagamingarcade.model.data.LocalizedText
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.String

@Serializable
data class NetworkBanner(
    val id: Int,
    @SerialName("titleMultiLanguage")
    val title: NetworkLocalizedText,
    @SerialName("descriptionMultiLanguage")
    val description: NetworkLocalizedText,
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
    title = LocalizedText(
        en = title.get("en"),
        ko = title.get("ko"),
        zh = title.get("cn"),
        ja = title.get("ja")
    ),
    imageUrl = image,
    description = LocalizedText(
        en = description.get("en"),
        ko = description.get("ko"),
        zh = description.get("cn"),
        ja = description.get("ja")
    ),
    actionType = actionType,
    actionValue = actionValue,
    order = order,
    isActive = isActive,
    startDatetime = startDatetime,
    endDatetime = endDatetime,
    createdAt = createdAt,
    updatedAt = updatedAt
)