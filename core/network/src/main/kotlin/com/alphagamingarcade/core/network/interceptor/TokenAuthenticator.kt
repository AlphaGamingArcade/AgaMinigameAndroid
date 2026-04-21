package com.alphagamingarcade.core.network.interceptor

import com.alphagamingarcade.core.datastore.data.TokenDataSource
import com.alphagamingarcade.core.network.api.AuthRestApi
import com.alphagamingarcade.core.network.model.NetworkRefreshTokenRequest
import dagger.Lazy
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject

class TokenAuthenticator @Inject constructor(
    private val tokenDataSource: TokenDataSource,
    private val authRestApi: Lazy<AuthRestApi>
) : Authenticator {

    override fun authenticate(route: Route?, response: Response): Request? {
        if (responseCount(response) >= 2) return null

        if (response.request.url.encodedPath.contains("/auth/refresh")) return null

        val refreshToken = runBlocking {
            tokenDataSource.getRefreshToken()
        } ?: return null

        val refreshResponse = try {
            authRestApi.get().refreshToken( // ← .get() to access the instance
                NetworkRefreshTokenRequest(refreshToken = refreshToken)
            ).execute()
        } catch (e: Exception) {
            return null
        }

        if (!refreshResponse.isSuccessful) return null

        val body = refreshResponse.body() ?: return null
        val data = body.data

        val newAccessToken = data.accessToken
        val newRefreshToken = data.refreshToken

        if (newAccessToken.isBlank() || newRefreshToken.isBlank()) return null

        runBlocking {
            tokenDataSource.saveTokens(
                accessToken = newAccessToken,
                refreshToken = newRefreshToken,
            )
        }

        return response.request.newBuilder()
            .header("Authorization", "Bearer $newAccessToken")
            .build()
    }

    private fun responseCount(response: Response): Int {
        var count = 1
        var prior = response.priorResponse
        while (prior != null) {
            count++
            prior = prior.priorResponse
        }
        return count
    }
}