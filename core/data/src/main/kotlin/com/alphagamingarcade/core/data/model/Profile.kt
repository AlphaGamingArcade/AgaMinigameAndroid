package com.alphagamingarcade.core.data.model

import com.alphagamingarcade.core.datastore.model.UserDataPreferences


data class Profile(
    val userId: String = String(),
    val userEmail: String = String(),
    val isEmailVerified: Boolean = false,
    val nickname: String = String()
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
        nickname =  member?.nickname ?: String()
    )
}
