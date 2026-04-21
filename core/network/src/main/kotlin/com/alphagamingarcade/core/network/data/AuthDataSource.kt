package com.alphagamingarcade.core.network.data

import com.alphagamingarcade.core.network.model.ApiResponse
import com.alphagamingarcade.core.network.model.ApiResponseNullable
import com.alphagamingarcade.core.network.model.NetworkAuthData
import com.alphagamingarcade.core.network.model.NetworkChangePasswordResponse
import com.alphagamingarcade.core.network.model.NetworkEmailStatus
import com.alphagamingarcade.core.network.model.NetworkForgotPassword
import com.alphagamingarcade.core.network.model.NetworkLogoutResponse
import com.alphagamingarcade.core.network.model.NetworkRefreshToken
import com.alphagamingarcade.core.network.model.NetworkResendVerifyEmail
import retrofit2.Call

/**
 * Data source interface for Jetpack.
 */
interface AuthDataSource {

    // Email/password sign in
    suspend fun signInWithEmailAndPassword(
        email: String,
        password: String
    ): Result<ApiResponse<NetworkAuthData>>

    // Email/password sign in
    suspend fun signOut(
        refreshToken: String
    ): Result<ApiResponseNullable<NetworkLogoutResponse?>>

    // Email/password register
    suspend fun registerWithEmailAndPassword(
        email: String,
        password: String,
        confirmPassword: String
    ): Result<ApiResponse<NetworkAuthData>>

    // Resend verify email
    suspend fun resendVerifyEmail(
        email: String
    ): Result<ApiResponseNullable<NetworkResendVerifyEmail?>>

    // Resend verify email
    suspend fun getEmailStatus(
        email: String
    ): Result<ApiResponse<NetworkEmailStatus>>


    // Resend verify email
    suspend fun sendForgotPasswordResetLink(
        email: String
    ): Result<ApiResponseNullable<NetworkForgotPassword?>>

    // Resend verify email
    fun refreshToken(
        refreshToken: String
    ): Call<ApiResponse<NetworkRefreshToken>>

    // Change password
    suspend fun changePassword(
        currentPassword: String,
        newPassword: String,
        confirmPassword: String
    ): Result<ApiResponseNullable<NetworkChangePasswordResponse?>>
}
