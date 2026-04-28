package com.alphagamingarcade.core.network.model

import com.alphagamingarcade.model.data.Game
import kotlinx.serialization.Serializable
import kotlin.String

@Serializable
data class NetworkGame(
    val code: String,
    val description: String,
    val descriptionMultiLanguage: Map<String, String>,
    val image: String,
    val category: String,
    val top: String,
    val latest: String,
    val trending: String,
    val comingSoon: String,
    val datetime: String,
    val gamecode: GameCode,
    val isFavorite: Boolean? = null
)

@Serializable
data class GameCode(
    val id: Int,
    val code: String,
    val name: String,
    val nameMultiLanguage: Map<String, String>,
    val gameType: String,
)

fun NetworkGame.toExternalModel() = Game(
    id = gamecode.id,
    gameCode = gamecode.code,
    name = gamecode.name,
    imageUrl = image,
    isTop = top == "y",
    isLatest = latest == "y",
    isTrending = trending == "y",
    isComingSoon = comingSoon == "y",
    needsSync = false,
    category = category,
    rating = 0.0f,
    playerCount = 0,
    isFavorite = isFavorite
)