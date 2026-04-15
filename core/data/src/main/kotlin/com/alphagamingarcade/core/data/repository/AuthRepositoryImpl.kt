package com.alphagamingarcade.core.data.repository

import android.util.Log
import com.alphagamingarcade.core.datastore.data.UserPreferencesDataSource
import com.alphagamingarcade.core.network.data.AuthDataSource
import com.alphagamingarcade.core.network.model.asPreferencesUserProfile
import com.alphagamingarcade.core.utils.suspendRunCatching
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Implementation of the [AuthRepository] interface responsible for handling authentication operations.
 *
 * @param authDataSource The data source for authentication operations.
 * @param userPreferencesDataSource The data source for user preferences.
 */
internal class AuthRepositoryImpl @Inject constructor(
    private val authDataSource: AuthDataSource,
    private val userPreferencesDataSource: UserPreferencesDataSource,
) : AuthRepository {
    override fun isSignedIn(): Flow<Boolean> {
        return userPreferencesDataSource.getUserDataPreferences()
            .map { userId -> userId.id.isNotEmpty() }
    }

    override suspend fun signInWithEmailAndPassword(
        email: String,
        password: String,
    ): Result<Unit> {
        return suspendRunCatching {
            val result = authDataSource.signInWithEmailAndPassword(email, password)
                .getOrThrow()
            userPreferencesDataSource.setUserProfile(result.data.user.asPreferencesUserProfile())
        }
    }

    override suspend fun registerWithEmailAndPassword(
        email: String,
        password: String
    ): Result<Unit> {
        return suspendRunCatching {
            val result = authDataSource.registerWithEmailAndPassword(email, password)
                .getOrThrow()
            userPreferencesDataSource.setUserProfile(result.data.user.asPreferencesUserProfile())
        }
    }
}