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
    val trending: String,
    val datetime: String,
    val gamecode: GameCode
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
    id = code,
    name = gamecode.name,
    imageUrl = image,
    isHot = top == "y",
    isNew = trending == "y",
    needsSync = false,
    category = category,
    rating = 0.0f,
    playerCount = 0
)