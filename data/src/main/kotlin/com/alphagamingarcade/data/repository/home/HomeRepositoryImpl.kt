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

package com.alphagamingarcade.data.repository.home

import com.alphagamingarcade.core.network.data.AuthDataSource
import com.alphagamingarcade.core.preferences.data.UserPreferencesDataSource
import com.alphagamingarcade.data.utils.SyncProgress
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import javax.inject.Inject

/**
 * Implementation of the ProfileRepository interface.
 *
 * @property userPreferencesDataSource Data source for user preferences.
 */
internal class HomeRepositoryImpl @Inject constructor(
    private val userPreferencesDataSource: UserPreferencesDataSource
) : HomeRepository {
    override suspend fun sync() = flow {
        Timber.d("DummyHomeRepository: Starting sync...")

        val total = 100
        for (i in 0..total step 10) {
                delay(1000) // Simulate network call
                Timber.d("DummyHomeRepository: Synced $i/$total items")
                emit(SyncProgress(current = i, total = total))
        }

        Timber.d("DummyHomeRepository: Sync completed!")
    }
}
