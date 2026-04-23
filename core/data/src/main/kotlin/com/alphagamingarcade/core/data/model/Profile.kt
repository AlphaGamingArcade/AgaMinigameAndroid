package com.alphagamingarcade.core.data.model

import com.alphagamingarcade.core.datastore.model.UserDataPreferences


data class Profile(
    val userId: String = String(),
    val userEmail: String = String(),
    val isEmailVerified: Boolean = false,
    val nickname: String = String(),
    val balance: Double = 0.0,
    val currency: String = "USD",
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
        nickname =  member?.nickname ?: String(),
        balance = member?.gameMoney ?: 0.0,
        currency = member?.agent?.currency ?: "USD",
    )
}
