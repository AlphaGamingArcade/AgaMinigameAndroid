package com.alphagamingarcade.core.data.repository

import com.alphagamingarcade.core.data.model.Profile
import com.alphagamingarcade.core.data.model.toProfile
import com.alphagamingarcade.core.datastore.data.TokenDataSource
import com.alphagamingarcade.core.datastore.data.UserPreferencesDataSource
import com.alphagamingarcade.core.network.data.AuthDataSource
import com.alphagamingarcade.core.utils.suspendRunCatching
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Implementation of the ProfileRepository interface.
 *
 * @property userPreferencesDataSource Data source for user preferences.
 * @property authDataSource Data source for authentication.
 */
internal class ProfileRepositoryImpl @Inject constructor(
    private val userPreferencesDataSource: UserPreferencesDataSource,
    private val authDataSource: AuthDataSource,
    private val tokenDataSource: TokenDataSource,

) : ProfileRepository {

    /**
     * Retrieves the user profile as a Flow.
     *
     * @return A Flow emitting the user profile.
     */
    override fun getProfile(): Flow<Profile> {
        return userPreferencesDataSource.getUserDataPreferences().map { it.toProfile() }
    }

    /**
     * Create member profile
     */


    /**
     * Signs out the user and resets user preferences.
     *
     * @return A Result indicating the success or failure of the operation.
     */
    override suspend fun signOut(): Result<Unit> {
        return suspendRunCatching {
            authDataSource.signOut()
            userPreferencesDataSource.resetUserPreferences()
            tokenDataSource.clearTokens()
        }
    }
}
