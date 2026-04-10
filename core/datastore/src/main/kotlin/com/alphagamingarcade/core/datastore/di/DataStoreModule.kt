package com.alphagamingarcade.core.datastore.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import com.alphagamingarcade.core.di.IoDispatcher
import com.alphagamingarcade.core.datastore.model.UserDataPreferences
import com.alphagamingarcade.core.datastore.utils.UserDataSerializer
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton

/**
 * Datastore module
 */
@Module
@InstallIn(SingletonComponent::class)
object DatastoreModule {

    private const val DATA_STORE_FILE_NAME = "user_preferences.json"

    /**
     * Provides the [DataStore] for [UserDataPreferences].
     *
     * @param appContext The application [Context].
     * @param ioDispatcher The [CoroutineDispatcher] for performing I/O operations.
     * @return The [DataStore] for [UserDataPreferences].
     */
    @Singleton
    @Provides
    fun providePreferencesDataStore(
        @ApplicationContext appContext: Context,
        @IoDispatcher ioDispatcher: CoroutineDispatcher,
    ): DataStore<UserDataPreferences> {
        return DataStoreFactory.create(
            serializer = UserDataSerializer,
            produceFile = { appContext.dataStoreFile(DATA_STORE_FILE_NAME) },
            scope = CoroutineScope(ioDispatcher + SupervisorJob()),
        )
    }
}
