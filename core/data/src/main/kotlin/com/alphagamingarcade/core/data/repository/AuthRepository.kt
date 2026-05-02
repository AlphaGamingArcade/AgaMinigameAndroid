package com.alphagamingarcade.core.data.repository

import com.alphagamingarcade.core.common.result.AppResult
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun isSignedIn(): Flow<Boolean>
    fun isEmailVerified(): Flow<Boolean>
    fun hasProfileSetup(): Flow<Boolean>

    suspend fun signInWithEmailAndPassword(
        email: String,
        password: String
    ): AppResult<Unit>

    suspend fun registerWithEmailAndPassword(
        email: String,
        password: String,
        confirmPassword: String,
    ): AppResult<Unit>

    suspend fun resendVerifyEmail(email: String) : Result<Unit>

    suspend fun getEmailStatus(email: String) : Result<Boolean>

    suspend fun sendResetLink(email: String) : Result<Unit>

    suspend fun changePassword(currentPassword: String, newPassword: String, confirmPassword: String) : Result<Unit>
}