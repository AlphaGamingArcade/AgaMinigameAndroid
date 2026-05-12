package com.alphagamingarcade.model.data

data class Banner(
    val id: Int,
    val title: LocalizedText,
    val imageUrl: String,
    val description: LocalizedText,
    val actionType: String,
    val actionValue: String,
    val order: Int,
    val isActive: Boolean,
    val startDatetime: String?,
    val endDatetime: String?,
    val createdAt: String?,
    val updatedAt:  String?
)