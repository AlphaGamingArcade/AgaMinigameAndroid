package com.alphagamingarcade.model.data

data class Banner(
    val id: Int,
    val title: String,
    val imageUrl: String,
    val description: String,
    val actionType: String,
    val actionValue: String,
    val order: Int,
    val isActive: Boolean,
    val startDatetime: String?,
    val endDatetime: String?,
    val createdAt: String?,
    val updatedAt:  String?
)