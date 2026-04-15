package com.alphagamingarcade.core.data.repository

import com.alphagamingarcade.core.data.model.DarkThemeConfig
import com.alphagamingarcade.core.data.model.Settings
import kotlinx.coroutines.flow.Flow


/**
 * Repository for managing user settings.
 *
 * This repository uses DataStore for local persistence of user preferences like theme
 * configuration and dynamic color preferences. All settings are stored locally only.
 *
 * @see SettingsRepositoryImpl Implementation class with DataStore integration
 */
interface SettingsRepository {
    /**
     * Retrieves the user settings as a Flow.
     *
     * @return A Flow emitting the user settings.
     */
    fun getSettings(): Flow<Settings>

    /**
     * Sets the dark theme configuration.
     *
     * @param darkThemeConfig The dark theme configuration to set.
     * @return A Result indicating the success or failure of the operation.
     */
    suspend fun setDarkThemeConfig(darkThemeConfig: DarkThemeConfig): Result<Unit>

    /**
     * Sets the dynamic color preference.
     *
     * @param useDynamicColor Whether to use dynamic colors.
     * @return A Result indicating the success or failure of the operation.
     */
    suspend fun setDynamicColorPreference(useDynamicColor: Boolean): Result<Unit>

    /**
     * Signs out the current user.
     *
     * @return A Result indicating the success or failure of the operation.
     */
    suspend fun signOut(): Result<Unit>
}
