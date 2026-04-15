package com.alphagamingarcade.core.datastore.model

import kotlinx.serialization.Serializable

@Serializable
data class PreferencesUserProfile(
    val id: String = String(),
    val userEmail: String = String(),
    val isEmailVerified: Boolean = false,
)
