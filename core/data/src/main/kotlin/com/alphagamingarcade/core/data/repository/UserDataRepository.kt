package com.alphagamingarcade.core.data.repository

import com.alphagamingarcade.core.data.model.DarkThemeConfig
import com.alphagamingarcade.model.data.UserData
import kotlinx.coroutines.flow.Flow

interface UserDataRepository {
    val userData: Flow<UserData>
//    /**
//     * Sets the desired dark theme config.
//     */
//    suspend fun setDarkThemeConfig(darkThemeConfig: DarkThemeConfig)
//
//    /**
//     * Sets the preferred dynamic color config.
//     */
//    suspend fun setDynamicColorPreference(useDynamicColor: Boolean)
//
//    /**
//     * Sets whether the user has completed the onboarding process.
//     */
//    suspend fun setShouldHideOnboarding(shouldHideOnboarding: Boolean)
}