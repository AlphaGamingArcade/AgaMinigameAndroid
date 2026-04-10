package com.alphagamingarcade.sync.manager

import android.content.Context
import androidx.work.ExistingWorkPolicy
import androidx.work.WorkManager
import com.alphagamingarcade.sync.worker.SyncWorker
import timber.log.Timber

object Sync {
    /**
     * Initializes the sync process that keeps the app's data current.
     * This method should be called only once from the app module's Application.onCreate().
     *
     * @param context The application context.
     */
    fun initialize(context: Context) {
        WorkManager.getInstance(context).apply {
            // Run sync on app startup and ensure only one sync worker runs at any time
            Timber.d("Sync: initialize")
            enqueueUniqueWork(
                SYNC_WORK_NAME,
                ExistingWorkPolicy.KEEP,
                SyncWorker.startUpSyncWork(),
            )
        }
    }
}

/**
 * The name of the sync worker.
 * This name is used to uniquely identify the sync worker.
 * It is used to enqueue the sync worker and to check if the sync worker is already running.
 */
internal const val SYNC_WORK_NAME = "com.alphagamingarcade.jetpack.sync.worker"
