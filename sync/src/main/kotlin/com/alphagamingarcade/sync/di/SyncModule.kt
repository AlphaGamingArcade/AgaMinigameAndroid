package com.alphagamingarcade.sync.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import com.alphagamingarcade.data.utils.SyncManager
import com.alphagamingarcade.sync.manager.SyncManagerImpl
import javax.inject.Singleton

/**
 * Dagger module for providing synchronization-related dependencies.
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class SyncModule {
    /**
     * Binds the implementation of SyncManager to SyncManagerImpl.
     *
     * @param syncManager The implementation of SyncManager.
     * @return The bound SyncManager instance.
     */
    @Binds
    @Singleton
    internal abstract fun bindSyncManager(syncManager: SyncManagerImpl): SyncManager
}
