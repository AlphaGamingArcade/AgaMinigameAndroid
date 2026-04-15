package com.alphagamingarcade.core.network.data

import com.alphagamingarcade.core.network.model.NetworkAuthResponse
import com.alphagamingarcade.core.network.model.NetworkAuthUser

/**
 * Data source interface for Jetpack.
 */
interface AuthDataSource {

    // Email/password sign in
    suspend fun signInWithEmailAndPassword(
        email: String,
        password: String
    ): Result<NetworkAuthResponse>

    // Email/password register
    suspend fun registerWithEmailAndPassword(
        email: String,
        password: String
    ): Result<NetworkAuthResponse>

    // Sign out
    suspend fun signOut()
}
