package com.alphagamingarcade.core.preferences.model

import kotlinx.serialization.Serializable

/**
 * Represents user data saved in Shared Preferences.
 *
 * This data class is used to store information about a user, including their ID, name, profile picture URI string,
 * preferred theme brand, dark theme configuration, and dynamic color preference.
 *
 * @property id The unique identifier for the user. Defaults to empty if not provided.
 * @property userName The name of the user. Defaults to "No Name" if not provided.
 * @property profilePictureUriString The URI string for the user's profile picture, if available. Defaults to `null` if not provided.
 * @property darkThemeConfigPreferences The user's preferred dark theme configuration. Defaults to [DarkThemeConfigPreferences.FOLLOW_SYSTEM].
 * @property useDynamicColor A boolean indicating whether the user prefers dynamic colors. Defaults to `true`.
 */
@Serializable
data class UserDataPreferences(
    val id: String = String(),
    val userName: String? = null,
    val profilePictureUriString: String? = null,
    val darkThemeConfigPreferences: DarkThemeConfigPreferences = DarkThemeConfigPreferences.FOLLOW_SYSTEM,
    val useDynamicColor: Boolean = true,
)
