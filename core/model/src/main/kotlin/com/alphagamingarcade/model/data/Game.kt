package com.alphagamingarcade.model.data

data class LocalizedText(
    val en: String = "",
    val ko: String = "",
    val zh: String = "",
    val ja: String = ""
)

fun LocalizedText.get(currentLang: String): String {
    return when (currentLang) {
        "ko" -> ko
        "zh" -> zh
        "ja" -> ja
        else -> en
    }
}

data class Game(
    val id: Int,
    val gamecode: String = String(),
    val name: LocalizedText = LocalizedText(),
    val description: LocalizedText = LocalizedText(),
    val imageUrl: String = String(),
    val isTop: Boolean = false,
    val isLatest: Boolean = false,
    val isTrending: Boolean = false,
    val isComingSoon: Boolean = false,
    val needsSync: Boolean = false,
    val category: String = String(),
    val rating: Float = 0.0f,
    val playerCount: Int = 0,
    val isFavorite: Boolean? = null
)