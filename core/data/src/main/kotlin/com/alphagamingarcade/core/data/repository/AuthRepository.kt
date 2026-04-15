package com.alphagamingarcade.core.data.repository

import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun isSignedIn(): Flow<Boolean>
    suspend fun signInWithEmailAndPassword(email: String, password: String): Result<Unit>
    suspend fun registerWithEmailAndPassword(
        email: String,
        password: String
    ): Result<Unit>
}