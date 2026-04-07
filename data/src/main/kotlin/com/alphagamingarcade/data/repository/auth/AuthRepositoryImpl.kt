/*
 * Copyright 2025 Atick Faisal
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.alphagamingarcade.data.repository.auth

import com.alphagamingarcade.core.network.data.AuthDataSource
import com.alphagamingarcade.core.preferences.data.UserPreferencesDataSource
import com.alphagamingarcade.core.utils.suspendRunCatching
import com.alphagamingarcade.data.model.profile.Profile
import com.alphagamingarcade.data.model.profile.toProfile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Implementation of the ProfileRepository interface.
 *
 * @property userPreferencesDataSource Data source for user preferences.
 */
internal class AuthRepositoryImpl @Inject constructor(
    private val userPreferencesDataSource: UserPreferencesDataSource,
) : AuthRepository {

    /**
     * Retrieves the user profile as a Flow.
     *
     * @return A Flow emitting the user profile.
     */
    override fun getProfile(): Flow<Profile> {
        return userPreferencesDataSource.getUserDataPreferences().map { it.toProfile() }
    }

    /**
     * Signs out the user and resets user preferences.
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
