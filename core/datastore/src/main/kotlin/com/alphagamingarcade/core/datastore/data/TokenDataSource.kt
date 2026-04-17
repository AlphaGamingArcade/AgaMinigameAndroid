package com.alphagamingarcade.core.datastore.data

interface TokenDataSource {

    // New methods for secure token handling
    suspend fun saveTokens(accessToken: String, refreshToken: String)
    suspend fun getAccessToken(): String?
    suspend fun getRefreshToken(): String?
    suspend fun clearTokens()
}