package com.alphagamingarcade.compose

import android.app.Application
import coil.ImageLoader
import coil.ImageLoaderFactory
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import javax.inject.Inject

/**
 * The main application class that extends [Application] and is annotated with [HiltAndroidApp].
 */
@HiltAndroidApp
class App : Application(), ImageLoaderFactory {

    @Inject
    lateinit var imageLoader: dagger.Lazy<ImageLoader>

    /**
     * Called when the application is first created.
     * Performs initialization tasks, such as setting up Timber logging in debug mode.
     */
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())
    }

    /**
     * Creates a new instance of [ImageLoader] using the injected [imageLoader].
     *
     * @return A new instance of [ImageLoader].
     */
    override fun newImageLoader(): ImageLoader = imageLoader.get()
}
