package com.alphagamingarcade.core.network.api

import com.alphagamingarcade.core.network.model.ApiResponse
import com.alphagamingarcade.core.network.model.ApiResponseNullable
import com.alphagamingarcade.core.network.model.LoginRequest
import com.alphagamingarcade.core.network.model.NetworkAuthData
import com.alphagamingarcade.core.network.model.NetworkChangePasswordRequest
import com.alphagamingarcade.core.network.model.NetworkChangePasswordResponse
import com.alphagamingarcade.core.network.model.NetworkEmailStatus
import com.alphagamingarcade.core.network.model.NetworkForgotPassword
import com.alphagamingarcade.core.network.model.NetworkForgotPasswordRequest
import com.alphagamingarcade.core.network.model.NetworkLogoutRequest
import com.alphagamingarcade.core.network.model.NetworkLogoutResponse
import com.alphagamingarcade.core.network.model.NetworkRefreshToken
import com.alphagamingarcade.core.network.model.NetworkRefreshTokenRequest
import com.alphagamingarcade.core.network.model.NetworkResendVerifyEmail
import com.alphagamingarcade.core.network.model.RegisterRequest
import com.alphagamingarcade.core.network.model.ResendVerifyEmailRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

interface AuthRestApi {
    @POST("auth/login")
    suspend fun signIn(
        @Body request: LoginRequest
    ): ApiResponse<NetworkAuthData>

    @POST("auth/register")
    suspend fun register(
        @Body request: RegisterRequest
    ): ApiResponse<NetworkAuthData>

    @POST("auth/logout")
    suspend fun signOut(
        @Body request: NetworkLogoutRequest
    ) : ApiResponseNullable<NetworkLogoutResponse?>

    @POST("auth/resend-verify-email")
    suspend fun resendVerifyEmail(
        @Body request: ResendVerifyEmailRequest
    ): ApiResponseNullable<NetworkResendVerifyEmail?>

    @GET("auth/email-status")
    suspend fun getEmailStatus(
        @Query ("email") email: String
    ): ApiResponse<NetworkEmailStatus>

    @POST("auth/forgot-password")
    suspend fun forgotPassword(
        @Body request: NetworkForgotPasswordRequest
    ): ApiResponseNullable<NetworkForgotPassword?>

    // Call<> only for the refresh token used in Authenticator
    @POST("auth/refresh")
    fun refreshToken(@Body request: NetworkRefreshTokenRequest): Call<ApiResponse<NetworkRefreshToken>>

    @PUT("auth/change-password")
    suspend fun changePassword(
        @Body request: NetworkChangePasswordRequest
    ): ApiResponseNullable<NetworkChangePasswordResponse?>
}