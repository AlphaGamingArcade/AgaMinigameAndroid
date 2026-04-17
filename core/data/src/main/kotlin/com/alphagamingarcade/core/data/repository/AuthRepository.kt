package com.alphagamingarcade.core.data.repository

import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun isSignedIn(): Flow<Boolean>
    fun isEmailVerified(): Flow<Boolean>
    fun hasProfileSetup(): Flow<Boolean>

    suspend fun signInWithEmailAndPassword(email: String, password: String): Result<Unit>
    suspend fun registerWithEmailAndPassword(
        email: String,
        password: String,
        confirmPassword: String,
    ): Result<Unit>

    suspend fun resendVerifyEmail(email: String) : Result<Unit>

    suspend fun getEmailStatus(email: String) : Result<Boolean>
}