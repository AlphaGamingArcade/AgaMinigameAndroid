package com.alphagamingarcade.core.datastore.data

import androidx.datastore.core.DataStore
import com.alphagamingarcade.core.datastore.model.TokenData
import com.alphagamingarcade.core.datastore.utils.CryptoManager
import com.alphagamingarcade.core.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class TokenDataSourceImpl @Inject constructor(
    private val datastore: DataStore<TokenData>,
    private val cryptoManager: CryptoManager,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : TokenDataSource {

    /**
     * Encrypts and saves both tokens to DataStore.
     */
    override suspend fun saveTokens(accessToken: String, refreshToken: String) {
        withContext(ioDispatcher) {
            val encryptedAccess = cryptoManager.encrypt(accessToken)
            val encryptedRefresh = cryptoManager.encrypt(refreshToken)
            datastore.updateData { tokenData ->
                tokenData.copy(
                    accessToken = encryptedAccess,
                    refreshToken = encryptedRefresh
                )
            }
        }
    }

    /**
     * Retrieves and decrypts the access token.
     */
    override suspend fun getAccessToken(): String? = withContext(ioDispatcher) {
        val encryptedToken = datastore.data.first().accessToken
        if (encryptedToken.isNullOrEmpty()) null else cryptoManager.decrypt(encryptedToken)
    }

    /**
     * Retrieves and decrypts the refresh token.
     */
    override suspend fun getRefreshToken(): String? = withContext(ioDispatcher) {
        val encryptedToken = datastore.data.first().refreshToken
        if (encryptedToken.isNullOrEmpty()) null else cryptoManager.decrypt(encryptedToken)
    }

    override suspend fun clearTokens() {
        withContext(ioDispatcher) {
            datastore.updateData { TokenData() }
        }
    }
}