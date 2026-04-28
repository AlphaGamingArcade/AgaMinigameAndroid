package com.alphagamingarcade.core.data.di

import com.alphagamingarcade.core.data.repository.AuthRepository
import com.alphagamingarcade.core.data.repository.AuthRepositoryImpl
import com.alphagamingarcade.core.data.repository.BannersRepositoryImpl
import com.alphagamingarcade.core.data.repository.BannersRepository
import com.alphagamingarcade.core.data.repository.GamesRepository
import com.alphagamingarcade.core.data.repository.GamesRepositoryImpl
import com.alphagamingarcade.core.data.repository.MembersRepository
import com.alphagamingarcade.core.data.repository.MembersRepositoryImpl
import com.alphagamingarcade.core.data.repository.ProfileRepository
import com.alphagamingarcade.core.data.repository.ProfileRepositoryImpl
import com.alphagamingarcade.core.data.repository.SettingsRepository
import com.alphagamingarcade.core.data.repository.SettingsRepositoryImpl
import com.alphagamingarcade.core.data.repository.TransactionRepository
import com.alphagamingarcade.core.data.repository.TransactionRepositoryImpl
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
    internal abstract fun bindsSettingsRepository(
        settingsRepositoryImpl: SettingsRepositoryImpl,
    ): SettingsRepository


    @Binds
    @Singleton
    internal abstract fun bindsAuthRepository(
        authRepositoryImpl: AuthRepositoryImpl,
    ): AuthRepository

    @Binds
    @Singleton
    internal abstract fun bindsProfileRepository(
        profileRepositoryImpl: ProfileRepositoryImpl
    ): ProfileRepository

    @Binds
    @Singleton
    internal abstract fun bindMemberRepository(
        membersRepositoryImpl: MembersRepositoryImpl
    ): MembersRepository

    @Binds
    @Singleton
    internal abstract fun bindTransactionRepository(
        transactionRepositoryImpl: TransactionRepositoryImpl
    ): TransactionRepository
}
