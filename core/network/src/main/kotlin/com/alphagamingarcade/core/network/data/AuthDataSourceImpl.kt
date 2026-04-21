package com.alphagamingarcade.core.network.data

import android.net.http.HttpException
import android.os.Build
import androidx.annotation.RequiresExtension
import com.alphagamingarcade.core.di.IoDispatcher
import com.alphagamingarcade.core.network.api.AuthRestApi
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
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import retrofit2.Call
import java.io.IOException
import javax.inject.Inject

internal class AuthDataSourceImpl @Inject constructor(
    private val authRestApi: AuthRestApi,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : AuthDataSource {

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    override suspend fun signInWithEmailAndPassword(
        email: String,
        password: String
    ): Result<ApiResponse<NetworkAuthData>> = withContext(ioDispatcher) {
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

    override suspend fun signOut(refreshToken: String
    ): Result<ApiResponseNullable<NetworkLogoutResponse?>> = withContext(ioDispatcher) {
        try {
            val request = NetworkLogoutRequest(
                refreshToken = refreshToken
            )
            val response = authRestApi.signOut(request)
            Result.success(response)
        } catch (e: HttpException) {
            Result.failure(e) // 4xx/5xx errors (wrong password, not found, etc.)
        } catch (e: IOException) {
            Result.failure(e) // No internet, timeout
        }
    }


    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    override suspend fun registerWithEmailAndPassword(
        email: String,
        password: String,
        confirmPassword: String
    ): Result<ApiResponse<NetworkAuthData>> = withContext(ioDispatcher) {
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
    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    override suspend fun resendVerifyEmail(
        email: String
    ): Result<ApiResponseNullable<NetworkResendVerifyEmail?>> = withContext(ioDispatcher) {
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
    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    override suspend fun getEmailStatus(
        email: String
    ): Result<ApiResponse<NetworkEmailStatus>> = withContext(ioDispatcher) {
        try {
            val response = authRestApi.getEmailStatus(email)
            Result.success(response)
        } catch (e: HttpException) {
            Result.failure(e)
        } catch (e: IOException) {
            Result.failure(e)
        }
    }



    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    override suspend fun sendForgotPasswordResetLink(
        email: String
    ): Result<ApiResponseNullable<NetworkForgotPassword?>> = withContext(ioDispatcher){
        try {
            val request = NetworkForgotPasswordRequest(
                email = email
            )
            val response = authRestApi.forgotPassword(request)
            Result.success(response)
        } catch (e: HttpException) {
            Result.failure(e)
        } catch (e: IOException) {
            Result.failure(e)
        }
    }

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    override suspend fun changePassword(
        currentPassword: String,
        newPassword: String,
        confirmPassword: String
    ): Result<ApiResponseNullable<NetworkChangePasswordResponse?>> = withContext(ioDispatcher){
        try {
            val request = NetworkChangePasswordRequest(
                currentPassword,
                newPassword,
                confirmPassword
            )
            val response = authRestApi.changePassword(request)

            Result.success(response)
        } catch (e: HttpException) {
            Result.failure(e)
        } catch (e: IOException) {
            Result.failure(e)
        }
    }

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    override fun refreshToken(refreshToken: String): Call<ApiResponse<NetworkRefreshToken>> {
        val request = NetworkRefreshTokenRequest(refreshToken = refreshToken)
        return authRestApi.refreshToken(request)
    }
}