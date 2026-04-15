package com.alphagamingarcade.core.network.api

import com.alphagamingarcade.core.network.model.LoginRequest
import com.alphagamingarcade.core.network.model.NetworkAuthResponse
import com.alphagamingarcade.core.network.model.RegisterRequest
import retrofit2.http.Body
import retrofit2.http.POST

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
}