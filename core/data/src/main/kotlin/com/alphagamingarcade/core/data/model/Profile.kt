package com.alphagamingarcade.core.data.model

import com.alphagamingarcade.core.preferences.model.PreferencesUserProfile
import com.alphagamingarcade.core.preferences.model.UserDataPreferences

/**
 * Data class representing a user profile.
 *
 * @property userName The name of the user.
 * @property profilePictureUri The URI of the user's profile picture.
 */
data class Profile(
    val userName: String = String(),
    val userEmail: String = String(),
    val userBalance: Double = 0.0,
    val profilePictureUri: String? = null,
)

/**
 * Extension function to convert UserDataPreferences to Profile.
 *
 * @return A Profile object with data from UserDataPreferences.
 */
fun UserDataPreferences.toProfile(): Profile {
    return Profile(
        userName = userName ?: "Anonymous",
        userEmail =  "example@email.com",
        userBalance = 10000.0,
        profilePictureUri = profilePictureUriString,
    )
}

/**
 * Extension function to convert Profile to PreferencesUserProfile.
 *
 * @return A PreferencesUserProfile object with data from Profile.
 */
fun Profile.toPreferencesUserProfile(): PreferencesUserProfile {
    return PreferencesUserProfile(
        userName = userName,
        userEmail = userEmail,
        userBalance = userBalance,
        profilePictureUriString = profilePictureUri,
    )
}
