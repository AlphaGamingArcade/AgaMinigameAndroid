package com.alphagamingarcade.core.network.data

/**
 * Data source interface for authentication operations.
 */
interface AuthDataSource {

    /**
     * Signs in a user with email and password.
     *
     * @param email The user's email address.
     * @param password The user's password.
     * @return A Result containing the authentication token or an error.
     */
    suspend fun signIn(email: String, password: String): Result<String>

    /**
     * Signs up a new user with email and password.
     *
     * @param email The user's email address.
     * @param password The user's password.
     * @return A Result containing the authentication token or an error.
     */
    suspend fun signUp(email: String, password: String): Result<String>

    /**
     * Signs out the current user.
     */
    suspend fun signOut()

    /**
     * Gets the currently authenticated user.
     *
     * @return The current user or null if not authenticated.
     */
    suspend fun getCurrentUser(): AuthUser?

    /**
     * Checks if a user is currently authenticated.
     *
     * @return True if authenticated, false otherwise.
     */
    fun isAuthenticated(): Boolean
}

/**
 * Data class representing an authenticated user.
 *
 * @property uid The user's unique identifier.
 * @property email The user's email address.
 * @property displayName The user's display name.
 */
data class AuthUser(
    val uid: String,
    val email: String?,
    val displayName: String?
)