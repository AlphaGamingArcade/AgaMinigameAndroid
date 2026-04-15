package com.alphagamingarcade.core.data.repository

import android.util.Log
import com.alphagamingarcade.core.data.model.DarkThemeConfig
import com.alphagamingarcade.core.data.model.Settings
import com.alphagamingarcade.core.data.model.asSettings
import com.alphagamingarcade.core.data.model.toDarkThemeConfigPreferences
import com.alphagamingarcade.core.datastore.data.UserPreferencesDataSource
import com.alphagamingarcade.core.network.data.AuthDataSource
import com.alphagamingarcade.core.utils.suspendRunCatching
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject


/**
 * Implementation of [SettingsRepository].
 *
 * @property authDataSource Data source for authentication.
 * @property userPreferencesDataSource Data source for user preferences.
 */
internal class SettingsRepositoryImpl @Inject constructor(
    private val authDataSource: AuthDataSource,
    private val userPreferencesDataSource: UserPreferencesDataSource,
) : SettingsRepository {

    /**
     * Retrieves the user's settings.
     *
     * @return A Flow emitting the user's settings.
     */
    override fun getSettings(): Flow<Settings> =
        userPreferencesDataSource.getUserDataPreferences().map { it.asSettings() }

    /**
     * Sets the dark theme configuration.
     *
     * @param darkThemeConfig The dark theme configuration.
     * @return A Result indicating the success or failure of the operation.
     */
    override suspend fun setDarkThemeConfig(darkThemeConfig: DarkThemeConfig): Result<Unit> {
        return suspendRunCatching {
            userPreferencesDataSource.setDarkThemeConfig(
                darkThemeConfig.toDarkThemeConfigPreferences(),
            )
        }
    }

    /**
     * Sets the dynamic color preference.
     *
     * @param useDynamicColor Whether to use dynamic colors.
     * @return A Result indicating the success or failure of the operation.
     */
    override suspend fun setDynamicColorPreference(useDynamicColor: Boolean): Result<Unit> {
        return suspendRunCatching {
            userPreferencesDataSource.setDynamicColorPreference(useDynamicColor)
        }
    }

    /**
     * Signs out the current user.
     *
     * @return A Result indicating the success or failure of the operation.
     */
    override suspend fun signOut(): Result<Unit> {
        return suspendRunCatching {
//            authDataSource.signOut()
            userPreferencesDataSource.resetUserPreferences()
        }
    }
}
