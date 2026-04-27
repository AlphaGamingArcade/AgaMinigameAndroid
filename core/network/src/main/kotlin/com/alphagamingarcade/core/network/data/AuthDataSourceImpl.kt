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
    ): ApiResponse<NetworkAuthData> {
        val request = LoginRequest(
            email = email,
            password = password
        )
        return authRestApi.signIn(request)
    }

    override suspend fun signOut(refreshToken: String
    ): ApiResponseNullable<NetworkLogoutResponse?> {
        val request = NetworkLogoutRequest(
            refreshToken = refreshToken
        )
        return authRestApi.signOut(request)
    }


    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    override suspend fun registerWithEmailAndPassword(
        email: String,
        password: String,
        confirmPassword: String
    ): ApiResponse<NetworkAuthData> {
        val request = RegisterRequest(
            email = email,
            password = password,
            confirmPassword = confirmPassword
        )
        return authRestApi.register(request)
    }

    // Resend verify email
    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    override suspend fun resendVerifyEmail(
        email: String
    ): ApiResponseNullable<NetworkResendVerifyEmail?> {
        val request = ResendVerifyEmailRequest(
            email = email
        )
        return authRestApi.resendVerifyEmail(request)
    }

    // Network email status
    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    override suspend fun getEmailStatus(
        email: String
    ): ApiResponse<NetworkEmailStatus> {
        return authRestApi.getEmailStatus(email)
    }

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    override suspend fun sendForgotPasswordResetLink(
        email: String
    ): ApiResponseNullable<NetworkForgotPassword?> {
        val request = NetworkForgotPasswordRequest(
            email = email
        )
        return authRestApi.forgotPassword(request)
    }

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    override suspend fun changePassword(
        currentPassword: String,
        newPassword: String,
        confirmPassword: String
    ): ApiResponseNullable<NetworkChangePasswordResponse?> {
        val request = NetworkChangePasswordRequest(
            currentPassword,
            newPassword,
            confirmPassword
        )
        return authRestApi.changePassword(request)
    }

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    override fun refreshToken(refreshToken: String): Call<ApiResponse<NetworkRefreshToken>> {
        val request = NetworkRefreshTokenRequest(refreshToken = refreshToken)
        return authRestApi.refreshToken(request)
    }
}