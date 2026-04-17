package com.alphagamingarcade.core.network.api

import com.alphagamingarcade.core.network.model.LoginRequest
import com.alphagamingarcade.core.network.model.NetworkAuthResponse
import com.alphagamingarcade.core.network.model.NetworkEmailStatusResponse
import com.alphagamingarcade.core.network.model.NetworkResendVerifyEmailResponse
import com.alphagamingarcade.core.network.model.RegisterRequest
import com.alphagamingarcade.core.network.model.ResendVerifyEmailRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface AuthRestApi {
    @POST("auth/login")
    suspend fun signIn(
        @Body request: LoginRequest
    ): NetworkAuthResponse

    @POST("auth/register")
    suspend fun register(
        @Body request: RegisterRequest
    ): NetworkAuthResponse

    @POST("auth/signout")
    suspend fun signOut()

    @POST("auth/resend-verify-email")
    suspend fun resendVerifyEmail(
        @Body request: ResendVerifyEmailRequest
    ): NetworkResendVerifyEmailResponse

    @GET("auth/email-status")
    suspend fun getEmailStatus(
        @Query ("email") email: String
    ): NetworkEmailStatusResponse
}