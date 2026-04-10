package com.alphagamingarcade.core.datastore.model

import com.alphagamingarcade.core.datastore.model.DarkThemeConfigPreferences.DARK
import com.alphagamingarcade.core.datastore.model.DarkThemeConfigPreferences.FOLLOW_SYSTEM
import com.alphagamingarcade.core.datastore.model.DarkThemeConfigPreferences.LIGHT
import kotlinx.serialization.Serializable

/**
 * Enum class representing configuration options for the dark theme.
 *
 * @property FOLLOW_SYSTEM The dark theme configuration follows the system-wide setting.
 * @property LIGHT The app's dark theme is disabled, using the light theme.
 * @property DARK The app's dark theme is enabled, using the dark theme.
 */
@Serializable
enum class DarkThemeConfigPreferences {
    FOLLOW_SYSTEM,
    LIGHT,
    DARK,
}
