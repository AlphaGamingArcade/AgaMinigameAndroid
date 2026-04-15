package com.alphagamingarcade.core.data.model

import com.alphagamingarcade.core.datastore.model.PreferencesUserProfile
import com.alphagamingarcade.core.datastore.model.UserDataPreferences

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
    val isEmailVerified: Boolean = false,
    val profilePictureUri: String? = null,
)

/**
 * Extension function to convert UserDataPreferences to Profile.
 *
 * @return A Profile object with data from UserDataPreferences.
 */
fun UserDataPreferences.toProfile(): Profile {
    return Profile(
        userEmail =  "example@email.com",
        userBalance = 10000.0,
        profilePictureUri = "",
    )
}

/**
 * Extension function to convert Profile to PreferencesUserProfile.
 *
 * @return A PreferencesUserProfile object with data from Profile.
 */
fun Profile.toPreferencesUserProfile(): PreferencesUserProfile {
    return PreferencesUserProfile(
        userEmail = userEmail,
        isEmailVerified = isEmailVerified,
    )
}
