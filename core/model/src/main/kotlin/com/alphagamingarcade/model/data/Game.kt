package com.alphagamingarcade.model.data

data class Game(
    val id: String,
    val name: String = String(),
    val imageUrl: String = String(),
    val isHot: Boolean = false,
    val isNew: Boolean = false,
    val needsSync: Boolean = false,
    val category: String = String(),
    val rating: Float = 0.0f,
    val playerCount: Int = 0,
)