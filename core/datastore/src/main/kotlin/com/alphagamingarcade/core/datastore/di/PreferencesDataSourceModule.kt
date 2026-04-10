package com.alphagamingarcade.core.datastore.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import com.alphagamingarcade.core.datastore.data.UserPreferencesDataSource
import com.alphagamingarcade.core.datastore.data.UserPreferencesDataSourceImpl
import javax.inject.Singleton

/**
 * Preferences DataSource module
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class PreferencesDataSourceModule {

    /**
     * Bind preferences datasource
     *
     * @param userPreferencesDataSourceImpl PreferencesDatastoreImpl
     * @return [UserPreferencesDataSource]
     */
    @Binds
    @Singleton
    internal abstract fun bindUserPreferencesDataSource(
        userPreferencesDataSourceImpl: UserPreferencesDataSourceImpl,
    ): UserPreferencesDataSource
}
