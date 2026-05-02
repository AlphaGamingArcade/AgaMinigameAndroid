package com.alphagamingarcade.core.datastore.data

import com.alphagamingarcade.core.datastore.model.DarkThemeConfigPreferences
import com.alphagamingarcade.core.datastore.model.MemberPreferences
import com.alphagamingarcade.core.datastore.model.UserDataPreferences
import kotlinx.coroutines.flow.Flow

/**
 * Interface defining methods to interact with user preferences data source.
 */
interface UserPreferencesDataSource {

    /**
     * Retrieves the user profile from the user preferences.
     *
     * @return A [Flow] of [MemberPreferences].
     */
    fun getUserDataPreferences(): Flow<UserDataPreferences>

    /**
     * Retrieves the user ID or throws an exception if the user is not authenticated.
     *
     * @return The user ID as a [String].
     * @throws IllegalStateException if the user is not authenticated.
     */
    suspend fun getUserIdOrThrow(): String

    suspend fun getMemberIdOrThrow(): Int

    suspend fun setUserProfile(userDataPreferences: UserDataPreferences)
    suspend fun setUserMember(memberPreferences: MemberPreferences)

    suspend fun setUserMemberGameMoney(amount: Double)

    suspend fun setDarkThemeConfig(darkThemeConfigPreferences: DarkThemeConfigPreferences)

    /**
     * Sets the dynamic color preferences in the user preferences.
     *
     * @param useDynamicColor A boolean indicating whether dynamic colors should be used.
     */
    suspend fun setDynamicColorPreference(useDynamicColor: Boolean)

    /**
     * Resets the user preferences to their default values.
     */
    suspend fun resetUserPreferences()
}
