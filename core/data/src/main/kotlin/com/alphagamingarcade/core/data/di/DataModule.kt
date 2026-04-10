package com.alphagamingarcade.core.data.di

import com.alphagamingarcade.core.data.repository.BannersRepositoryImpl
import com.alphagamingarcade.core.data.repository.BannersRepository
import com.alphagamingarcade.core.data.repository.GamesRepository
import com.alphagamingarcade.core.data.repository.GamesRepositoryImpl
import com.alphagamingarcade.core.data.repository.UserDataRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Dagger module for providing repository implementations.
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    @Singleton
    internal abstract fun bindsBannerRepository(
        bannersRepositoryImpl: BannersRepositoryImpl,
    ): BannersRepository

    @Binds
    @Singleton
    internal abstract fun bindsGamesRepository(
        gamesRepositoryImpl: GamesRepositoryImpl,
    ): GamesRepository

    @Binds
    @Singleton
    internal abstract fun bindsUserDataRepository(
        userDataRepository: UserDataRepository,
    ): UserDataRepository
}
