package com.alphagamingarcade.core.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import com.alphagamingarcade.core.utils.StringDecoder
import com.alphagamingarcade.core.utils.UriDecoder

/**
 * Dagger module providing bindings for StringDecoder implementations.
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class StringDecoderModule {
    /**
     * Binds the provided [UriDecoder] instance as the implementation for [StringDecoder].
     *
     * @param uriDecoder The instance of [UriDecoder] to be bound as [StringDecoder].
     */
    @Binds
    abstract fun bindStringDecoder(uriDecoder: UriDecoder): StringDecoder
}
