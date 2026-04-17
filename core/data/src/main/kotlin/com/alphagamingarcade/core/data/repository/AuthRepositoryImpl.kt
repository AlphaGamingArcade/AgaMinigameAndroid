package com.alphagamingarcade.core.data.repository

import com.alphagamingarcade.core.datastore.data.TokenDataSource
import com.alphagamingarcade.core.datastore.data.UserPreferencesDataSource
import com.alphagamingarcade.core.datastore.model.UserDataPreferences
import com.alphagamingarcade.core.network.data.AuthDataSource
import com.alphagamingarcade.core.network.model.asMemberPreferences
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
    private val tokenDataSource: TokenDataSource
) : AuthRepository {
    override fun isSignedIn(): Flow<Boolean> {
        return userPreferencesDataSource.getUserDataPreferences()
            .map { userId -> userId.id.isNotEmpty() }
    }

    override fun isEmailVerified(): Flow<Boolean> {
        return userPreferencesDataSource.getUserDataPreferences()
            .map { user -> user.isEmailVerified }
    }

    override fun hasProfileSetup(): Flow<Boolean> {
        return userPreferencesDataSource.getUserDataPreferences()
            .map { user -> user.member != null }
    }

    override suspend fun signInWithEmailAndPassword(
        email: String,
        password: String,
    ): Result<Unit> {
        return suspendRunCatching {
            val result = authDataSource.signInWithEmailAndPassword(email, password)
                .getOrThrow()

            userPreferencesDataSource.setUserProfile(
                UserDataPreferences(
                    id = result.data.user.id.toString(),
                    email = result.data.user.email,
                    isEmailVerified = result.data.user.isEmailVerified,
                    member = result.data.member?.asMemberPreferences(),
                )
            )

            val accessToken = result.data.accessToken;
            val refreshToken = result.data.refreshToken;
            if (accessToken != null && refreshToken != null) {
                tokenDataSource.saveTokens(accessToken, refreshToken);
            }
        }
    }

    override suspend fun registerWithEmailAndPassword(
        email: String,
        password: String,
        confirmPassword: String
    ): Result<Unit> {
        return suspendRunCatching {
            val result = authDataSource.registerWithEmailAndPassword(email, password, confirmPassword).getOrThrow();

            userPreferencesDataSource.setUserProfile(
                UserDataPreferences(
                    id = result.data.user.id.toString(),
                    email = result.data.user.email,
                    isEmailVerified = result.data.user.isEmailVerified,
                    member = result.data.member?.asMemberPreferences(),
                )
            )

            val accessToken = result.data.accessToken;
            val refreshToken = result.data.refreshToken;
            if (accessToken != null && refreshToken != null) {
                tokenDataSource.saveTokens(accessToken, refreshToken);
            }
        }
    }

    override suspend fun resendVerifyEmail(email: String): Result<Unit> {
        return suspendRunCatching {
            authDataSource
                .resendVerifyEmail(email)
                .getOrThrow()
        }
    }

    override suspend fun getEmailStatus(email: String): Result<Boolean> {
        return suspendRunCatching {
            authDataSource.getEmailStatus(email)
                .getOrThrow()
                .data
                .isVerified
        }
    }
}