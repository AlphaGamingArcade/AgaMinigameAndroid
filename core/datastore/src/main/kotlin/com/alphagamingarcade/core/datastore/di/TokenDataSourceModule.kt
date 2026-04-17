package com.alphagamingarcade.core.datastore.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import com.alphagamingarcade.core.datastore.data.TokenDataSource
import com.alphagamingarcade.core.datastore.data.TokenDataSourceImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class TokenDataSourceModule {

    @Binds
    @Singleton
    internal abstract fun bindTokenDataSource(
        tokenDataSourceImpl: TokenDataSourceImpl,
    ): TokenDataSource
}