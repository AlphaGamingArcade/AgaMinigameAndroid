package com.alphagamingarcade.core.data.model

import com.alphagamingarcade.core.preferences.model.DarkThemeConfigPreferences
import com.alphagamingarcade.core.preferences.model.UserDataPreferences

/**
 * Data class representing editable user settings related to themes and appearance.
 *
 * @property useDynamicColor Indicates whether dynamic colors are enabled.
 * @property darkThemeConfig Configuration for the dark theme.
 * @property language The language of the app.
 * @constructor Creates a [Settings] instance with optional parameters.
 */
data class Settings(
    val userName: String? = null,
    val useDynamicColor: Boolean = true,
    val darkThemeConfig: DarkThemeConfig = DarkThemeConfig.FOLLOW_SYSTEM,
    val language: Language = Language.ENGLISH,
)

/**
 * Enum class representing configuration options for the dark theme.
 *
 * @property FOLLOW_SYSTEM The dark theme configuration follows the system-wide setting.
 * @property LIGHT The app's dark theme is disabled, using the light theme.
 * @property DARK The app's dark theme is enabled, using the dark theme.
 */
enum class DarkThemeConfig {
    FOLLOW_SYSTEM,
    LIGHT,
    DARK,
}

/**
 * Enum class representing the supported languages.
 *
 * @property code The language code.
 */
enum class Language(val code: String) {
    ENGLISH("en"),
    ARABIC("ar"),
}

/**
 * Extension function to map [UserDataPreferences] to [Settings].
 *
 * @return The mapped [Settings].
 */
fun UserDataPreferences.asSettings(): Settings {
    return Settings(
        userName = userName,
        useDynamicColor = useDynamicColor,
        darkThemeConfig = darkThemeConfigPreferences.toDarkThemeConfig(),
    )
}

/**
 * Extension function to map [DarkThemeConfigPreferences] to [DarkThemeConfig].
 *
 * @return The mapped [DarkThemeConfig].
 */
fun DarkThemeConfigPreferences.toDarkThemeConfig(): DarkThemeConfig {
    return when (this) {
        DarkThemeConfigPreferences.FOLLOW_SYSTEM -> DarkThemeConfig.FOLLOW_SYSTEM
        DarkThemeConfigPreferences.LIGHT -> DarkThemeConfig.LIGHT
        DarkThemeConfigPreferences.DARK -> DarkThemeConfig.DARK
    }
}

/**
 * Extension function to map [DarkThemeConfig] to [DarkThemeConfigPreferences].
 *
 * @return The mapped [DarkThemeConfigPreferences].
 */
fun DarkThemeConfig.toDarkThemeConfigPreferences(): DarkThemeConfigPreferences {
    return when (this) {
        DarkThemeConfig.FOLLOW_SYSTEM -> DarkThemeConfigPreferences.FOLLOW_SYSTEM
        DarkThemeConfig.LIGHT -> DarkThemeConfigPreferences.LIGHT
        DarkThemeConfig.DARK -> DarkThemeConfigPreferences.DARK
    }
}
