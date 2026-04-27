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
    ): ApiResponse<NetworkAuthData>

    // Email/password sign in
    suspend fun signOut(
        refreshToken: String
    ): ApiResponseNullable<NetworkLogoutResponse?>

    // Email/password register
    suspend fun registerWithEmailAndPassword(
        email: String,
        password: String,
        confirmPassword: String
    ): ApiResponse<NetworkAuthData>

    // Resend verify email
    suspend fun resendVerifyEmail(
        email: String
    ): ApiResponseNullable<NetworkResendVerifyEmail?>

    // Resend verify email
    suspend fun getEmailStatus(
        email: String
    ): ApiResponse<NetworkEmailStatus>


    // Resend verify email
    suspend fun sendForgotPasswordResetLink(
        email: String
    ): ApiResponseNullable<NetworkForgotPassword?>

    // Change password
    suspend fun changePassword(
        currentPassword: String,
        newPassword: String,
        confirmPassword: String
    ): ApiResponseNullable<NetworkChangePasswordResponse?>

    // Resend verify email
    fun refreshToken(
        refreshToken: String
    ): Call<ApiResponse<NetworkRefreshToken>>

}
