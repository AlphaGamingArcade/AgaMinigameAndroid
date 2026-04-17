package com.alphagamingarcade.core.network.data

import com.alphagamingarcade.core.network.model.NetworkAuthResponse
import com.alphagamingarcade.core.network.model.NetworkEmailStatusResponse
import com.alphagamingarcade.core.network.model.NetworkResendVerifyEmailResponse

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
        password: String,
        confirmPassword: String
    ): Result<NetworkAuthResponse>

    // Resend verify email
    suspend fun resendVerifyEmail(
        email: String
    ): Result<NetworkResendVerifyEmailResponse>

    // Resend verify email
    suspend fun getEmailStatus(
        email: String
    ): Result<NetworkEmailStatusResponse>

    // Sign out
    suspend fun signOut()
}
