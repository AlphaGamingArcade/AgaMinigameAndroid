package com.alphagamingarcade.core.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import javax.inject.Qualifier
import javax.inject.Singleton

/**
 * Annotation used to mark the application scope.
 */
@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class ApplicationScope

/**
 * Dagger module that provides a CoroutineScope tied to the application lifecycle.
 */
@Module
@InstallIn(SingletonComponent::class)
object CoroutineModule {
    /**
     * Provides a CoroutineScope that is tied to the application lifecycle.
     *
     * @param dispatcher The CoroutineDispatcher to be used by the CoroutineScope.
     * @return A CoroutineScope with a SupervisorJob and the provided dispatcher.
     */
    @Provides
    @Singleton
    @ApplicationScope
    fun providesCoroutineScope(
        @DefaultDispatcher dispatcher: CoroutineDispatcher,
    ): CoroutineScope = CoroutineScope(SupervisorJob() + dispatcher)
}
