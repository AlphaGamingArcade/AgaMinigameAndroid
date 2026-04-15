package com.alphagamingarcade.core.network.di

import com.alphagamingarcade.core.network.data.AuthDataSource
import com.alphagamingarcade.core.network.data.AuthDataSourceImpl
import com.alphagamingarcade.core.network.data.BannerDataSource
import com.alphagamingarcade.core.network.data.BannerDataSourceImpl
import com.alphagamingarcade.core.network.data.GameDataSource
import com.alphagamingarcade.core.network.data.GameDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Module for providing [BannerDataSource].
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {

    @Binds
    @Singleton
    internal abstract fun bindBannerDataSource(
        bannerDataSourceImpl: BannerDataSourceImpl,
    ): BannerDataSource

    @Binds
    @Singleton
    internal abstract fun bindGameDataSource(
        gameDataSourceImpl: GameDataSourceImpl,
    ): GameDataSource

    @Binds
    @Singleton
    internal abstract fun bindAuthDataSource(
        authDataSourceImpl: AuthDataSourceImpl,
    ): AuthDataSource
}
