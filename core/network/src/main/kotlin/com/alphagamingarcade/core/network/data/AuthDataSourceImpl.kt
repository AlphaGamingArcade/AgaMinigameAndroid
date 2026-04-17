package com.alphagamingarcade.core.network.data

import android.net.http.HttpException
import com.alphagamingarcade.core.di.IoDispatcher
import com.alphagamingarcade.core.network.api.AuthRestApi
import com.alphagamingarcade.core.network.model.LoginRequest
import com.alphagamingarcade.core.network.model.NetworkAuthResponse
import com.alphagamingarcade.core.network.model.NetworkEmailStatusResponse
import com.alphagamingarcade.core.network.model.NetworkResendVerifyEmailResponse
import com.alphagamingarcade.core.network.model.RegisterRequest
import com.alphagamingarcade.core.network.model.ResendVerifyEmailRequest
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject

internal class AuthDataSourceImpl @Inject constructor(
    private val authRestApi: AuthRestApi,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : AuthDataSource {

    override suspend fun signInWithEmailAndPassword(
        email: String,
        password: String
    ): Result<NetworkAuthResponse> = withContext(ioDispatcher) {
        try {
            val request = LoginRequest(
                email = email,
                password = password
            )
            val response = authRestApi.signIn(request)
            Result.success(response)
        } catch (e: HttpException) {
            Result.failure(e) // 4xx/5xx errors (wrong password, not found, etc.)
        } catch (e: IOException) {
            Result.failure(e) // No internet, timeout
        }
    }

    override suspend fun registerWithEmailAndPassword(
        email: String,
        password: String,
        confirmPassword: String
    ): Result<NetworkAuthResponse> = withContext(ioDispatcher) {
        try {

            val request = RegisterRequest(
                email = email,
                password = password,
                confirmPassword = confirmPassword
            )

            val response = authRestApi.register(request)

            Result.success(response)
        } catch (e: HttpException) {
            Result.failure(e) // 4xx/5xx errors (wrong password, not found, etc.)
        } catch (e: IOException) {
            Result.failure(e) // No internet, timeout
        }
    }

    // Resend verify email
    override suspend fun resendVerifyEmail(
        email: String
    ): Result<NetworkResendVerifyEmailResponse> = withContext(ioDispatcher) {
        try {
            val request = ResendVerifyEmailRequest(
                email = email
            )

            val response = authRestApi.resendVerifyEmail(request)

            Result.success(response)
        } catch (e: HttpException) {
            Result.failure(e)
        } catch (e: IOException) {
            Result.failure(e)
        }
    }

    // Network email status
    override suspend fun getEmailStatus(
        email: String
    ): Result<NetworkEmailStatusResponse> = withContext(ioDispatcher) {
        try {
            val response = authRestApi.getEmailStatus(email)
            Result.success(response)
        } catch (e: HttpException) {
            Result.failure(e)
        } catch (e: IOException) {
            Result.failure(e)
        }
    }


    override suspend fun signOut() = withContext(ioDispatcher) {
//        // Call sign out endpoint if available
//        authRestApi.signOut()
        // Clear stored tokens
//        clearTokens()
    }

}