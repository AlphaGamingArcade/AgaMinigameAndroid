package com.alphagamingarcade.core.network.di

import com.alphagamingarcade.core.network.api.BannerRestApi
import com.alphagamingarcade.core.network.api.GameRestApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import com.alphagamingarcade.core.network.di.retrofit.RetrofitModule
import retrofit2.Retrofit
import javax.inject.Singleton


@Module(
    includes = [
        RetrofitModule::class,
    ],
)
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun providesBannerRestApi(retrofit: Retrofit): BannerRestApi {
        return retrofit.create(BannerRestApi::class.java)
    }

    @Provides
    @Singleton
    fun providesGameRestApi(retrofit: Retrofit): GameRestApi {
        return retrofit.create(GameRestApi::class.java)
    }

}
