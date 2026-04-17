package com.alphagamingarcade.core.network.interceptor

import com.alphagamingarcade.core.datastore.data.TokenDataSource
import com.alphagamingarcade.core.datastore.data.UserPreferencesDataSource
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val tokenDataSource: TokenDataSource
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        // runBlocking is acceptable here since Interceptor.intercept() is not a suspend function
        // and OkHttp already runs this on a background thread
        val accessToken = runBlocking { tokenDataSource.getAccessToken() }

        val request = chain.request().newBuilder().apply {
            if (accessToken != null) {
                addHeader("Authorization", "Bearer $accessToken")
            }
        }.build()

        return chain.proceed(request)
    }
}