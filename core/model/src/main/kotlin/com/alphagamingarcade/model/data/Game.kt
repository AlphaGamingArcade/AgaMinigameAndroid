package com.alphagamingarcade.model.data

data class Game(
    val id: Int,
    val gameCode: String = String(),
    val name: String = String(),
    val imageUrl: String = String(),
    val isTop: Boolean = false,
    val isLatest: Boolean = false,
    val isTrending: Boolean = false,
    val isComingSoon: Boolean = false,
    val needsSync: Boolean = false,
    val category: String = String(),
    val rating: Float = 0.0f,
    val playerCount: Int = 0,
)