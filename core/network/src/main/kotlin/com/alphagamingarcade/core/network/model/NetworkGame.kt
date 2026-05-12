package com.alphagamingarcade.core.network.model

import com.alphagamingarcade.model.data.Game
import com.alphagamingarcade.model.data.LocalizedText
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.String


@Serializable
data class NetworkLocalizedText(
    val en: String = "",
    val ko: String = "",
    val cn: String = "",
    val ja: String = ""
)

fun NetworkLocalizedText.get(currentLang: String): String {
    return when (currentLang) {
        "ko" -> ko
        "cn" -> cn
        "ja" -> ja
        else -> en
    }
}


@Serializable
data class NetworkGame(
    val code: String,
    @SerialName("descriptionMultiLanguage")
    val description: NetworkLocalizedText,
    val image: String,
    val category: String,
    val top: String,
    val latest: String,
    val trending: String,
    val comingSoon: String,
    val datetime: String,
    val totalPlayers: Int,
    val gamecode: Gamecode,
    val isFavorite: Boolean? = null
)

@Serializable
data class Gamecode(
    val id: Int,
    val code: String,
    @SerialName("nameMultiLanguage")
    val name: NetworkLocalizedText,
    val gameType: String,
)

fun NetworkGame.toExternalModel() = Game(
    id = gamecode.id,
    gamecode = gamecode.code,
    name = LocalizedText(
        en = gamecode.name.get("en"),
        ko = gamecode.name.get("ko"),
        zh = gamecode.name.get("cn"),
        ja = gamecode.name.get("ja")
    ),
    description = LocalizedText(
        en = description.get("en"),
        ko = description.get("ko"),
        zh = description.get("cn"),
        ja = description.get("ja")
    ),
    imageUrl = image,
    isTop = top == "y",
    isLatest = latest == "y",
    isTrending = trending == "y",
    isComingSoon = comingSoon == "y",
    needsSync = false,
    category = category,
    rating = 0.0f,
    playerCount = totalPlayers,
    isFavorite = isFavorite
)