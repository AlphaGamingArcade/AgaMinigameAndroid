package com.alphagamingarcade.core.data.repository

import com.alphagamingarcade.core.data.model.Profile
import kotlinx.coroutines.flow.Flow


/**
 * Interface for managing profile-related operations.
 *
 * This repository observes Firebase Auth state to provide profile information and handle
 * sign-out operations. Profile data is cached locally for offline access.
 *
 * @see ProfileRepositoryImpl Implementation class with Firebase Auth integration
 */
interface ProfileRepository {
    /**
     * Retrieves the profile information.
     *
     * @return A Flow emitting the Profile object.
     */
    fun getProfile(): Flow<Profile>

    /**
     * Signs out the current user.
     *
     * @return A Result indicating the success or failure of the operation.
     */
    suspend fun signOut(): Result<Unit>

    /**
     * Change the password of a user.
     *
     * @return A Result indicating the success or failure of the operation.
     */
    suspend fun changePassword(
        currentPassword: String,
        newPassword: String,
        confirmPassword: String
    ): Result<Unit>
}
