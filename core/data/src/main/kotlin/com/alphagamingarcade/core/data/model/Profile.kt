package com.alphagamingarcade.core.data.model

import com.alphagamingarcade.core.datastore.model.MemberPreferences
import com.alphagamingarcade.core.datastore.model.UserDataPreferences


data class Profile(
    val userId: String = String(),
    val userEmail: String = String(),
    val isEmailVerified: Boolean = false,
)

/**
 * Extension function to convert UserDataPreferences to Profile.
 *
 * @return A Profile object with data from UserDataPreferences.
 */
fun UserDataPreferences.toProfile(): Profile {
    return Profile(
        userId = id,
        userEmail = email,
        isEmailVerified = isEmailVerified,
    )
}
