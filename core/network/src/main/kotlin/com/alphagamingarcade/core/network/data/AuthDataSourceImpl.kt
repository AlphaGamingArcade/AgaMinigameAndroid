package com.alphagamingarcade.core.network.data

import android.content.ContentValues.TAG
import android.net.http.HttpException
import android.util.Log
import com.alphagamingarcade.core.di.IoDispatcher
import com.alphagamingarcade.core.network.api.AuthRestApi
import com.alphagamingarcade.core.network.model.LoginRequest
import com.alphagamingarcade.core.network.model.NetworkAuthResponse
import com.alphagamingarcade.core.network.model.RegisterRequest
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
            storeTokens(response.data.accessToken, response.data.refreshToken)
            Result.success(response)
        } catch (e: HttpException) {
            Result.failure(e) // 4xx/5xx errors (wrong password, not found, etc.)
        } catch (e: IOException) {
            Result.failure(e) // No internet, timeout
        }
    }

    override suspend fun registerWithEmailAndPassword(
        email: String,
        password: String
    ): Result<NetworkAuthResponse> = withContext(ioDispatcher) {
        try {

            val request = RegisterRequest(
                email = email,
                password = password
            )

            val response = authRestApi.register(request)
            // Extract and store tokens securely
            storeTokens(response.data.accessToken, response.data.refreshToken)
            Result.success(response)
        } catch (e: HttpException) {
            Result.failure(e) // 4xx/5xx errors (wrong password, not found, etc.)
        } catch (e: IOException) {
            Result.failure(e) // No internet, timeout
        }
    }

    override suspend fun signOut() = withContext(ioDispatcher) {
//        // Call sign out endpoint if available
//        authRestApi.signOut()
        // Clear stored tokens
        clearTokens()
    }

    private fun storeTokens(accessToken: String, refreshToken: String) {
        // TODO: Store tokens securely in EncryptedSharedPreferences or similar
        // Example:
        // val sharedPref = context.getSharedPreferences("auth", Context.MODE_PRIVATE)
        // sharedPref.edit {
        //     putString("access_token", accessToken)
        //     putString("refresh_token", refreshToken)
        // }
    }

    private fun clearTokens() {
        // TODO: Clear tokens from secure storage
        // Example:
        // val sharedPref = context.getSharedPreferences("auth", Context.MODE_PRIVATE)
        // sharedPref.edit {
        //     remove("access_token")
        //     remove("refresh_token")
        // }
    }
}