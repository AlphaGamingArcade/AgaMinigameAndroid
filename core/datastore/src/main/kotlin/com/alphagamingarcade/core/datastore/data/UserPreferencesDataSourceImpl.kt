package com.alphagamingarcade.core.datastore.data

import androidx.datastore.core.DataStore
import com.alphagamingarcade.core.di.IoDispatcher
import com.alphagamingarcade.core.datastore.model.DarkThemeConfigPreferences
import com.alphagamingarcade.core.datastore.model.MemberPreferences
import com.alphagamingarcade.core.datastore.model.UserDataPreferences
import com.alphagamingarcade.core.datastore.utils.CryptoManager
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Implementation of the [UserPreferencesDataSource] interface using DataStore to manage user preferences.
 *
 * @property datastore The DataStore instance to manage user preferences data.
 * @property ioDispatcher The CoroutineDispatcher for performing I/O operations.
 */
internal class UserPreferencesDataSourceImpl @Inject constructor(
    private val datastore: DataStore<UserDataPreferences>,
    private val cryptoManager: CryptoManager,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : UserPreferencesDataSource {

    /**
     * Retrieves the user profile from the user preferences.
     *
     * @return A [Flow] of [UserDataPreferences].
     */
    override fun getUserDataPreferences(): Flow<UserDataPreferences> =
        datastore.data.flowOn(ioDispatcher)

    /**
     * Retrieves the user ID or throws an exception if the user is not authenticated.
     *
     * @return The user ID as a [String].
     * @throws IllegalStateException if the user is not authenticated.
     */
    override suspend fun getUserIdOrThrow(): String {
        return withContext(ioDispatcher) {
            val userId = datastore.data.first().id
            if (userId.isEmpty()) throw IllegalStateException("User not authenticated")
            userId
        }
    }
    
    override suspend fun setUserProfile(userDataPreferences: UserDataPreferences) {
        withContext(ioDispatcher) {
            datastore.updateData { userData ->
                userData.copy(
                    id = userDataPreferences.id,
                    email = userDataPreferences.email,
                    isEmailVerified = userDataPreferences.isEmailVerified,
                    member = userDataPreferences.member,
                )
            }
        }
    }

    override suspend fun setUserMember(memberPreferences: MemberPreferences) {
        withContext(ioDispatcher) {
            datastore.updateData { userData ->
                userData.copy(
                    member = memberPreferences,
                )
            }
        }
    }

    /**
     * Sets the dark theme configuration in the user preferences.
     *
     * @param darkThemeConfigPreferences The dark theme configuration to be set.
     */
    override suspend fun setDarkThemeConfig(darkThemeConfigPreferences: DarkThemeConfigPreferences) {
        withContext(ioDispatcher) {
            datastore.updateData { userData ->
                userData.copy(darkThemeConfigPreferences = darkThemeConfigPreferences)
            }
        }
    }

    /**
     * Sets the dynamic color preferences in the user preferences.
     *
     * @param useDynamicColor A boolean indicating whether dynamic colors should be used.
     */
    override suspend fun setDynamicColorPreference(useDynamicColor: Boolean) {
        withContext(ioDispatcher) {
            datastore.updateData { userData ->
                userData.copy(useDynamicColor = useDynamicColor)
            }
        }
    }

    /**
     * Resets the user preferences to their default values.
     */
    override suspend fun resetUserPreferences() {
        withContext(ioDispatcher) {
            datastore.updateData { UserDataPreferences() }
        }
    }
}
