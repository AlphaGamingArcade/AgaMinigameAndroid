package com.alphagamingarcade.sync.extensions

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.work.ForegroundInfo
import com.alphagamingarcade.core.extensions.createNotificationChannel
import com.alphagamingarcade.core.extensions.createProgressNotification
import com.alphagamingarcade.sync.R

private const val SYNC_NOTIFICATION_ID = 0
private const val SYNC_NOTIFICATION_CHANNEL_ID = "SyncNotificationChannel"
private const val MAIN_DEFAULT_INTENT_REQUEST_CODE = 0

/**
 * Foreground information for sync on lower API levels when sync workers are being
 * run with a foreground service
 *
 * @param total The total number of items.
 * @param current The current number of items.
 * @return The foreground information for the sync work.
 */
fun Context.syncForegroundInfo(total: Int, current: Int) = ForegroundInfo(
    SYNC_NOTIFICATION_ID,
    syncWorkNotification(total, current),
)

/**
 * Progress notification for sync work
 *
 * @param total The total number of items.
 * @param current The current number of items.
 * @return The notification for the sync work.
 */
private fun Context.syncWorkNotification(total: Int, current: Int): Notification {
    createNotificationChannel(
        channelId = SYNC_NOTIFICATION_CHANNEL_ID,
        channelName = R.string.sync_work_notification_channel_name,
        channelDescription = R.string.sync_work_notification_channel_description,
        importance = NotificationManager.IMPORTANCE_LOW,
    )

    return createProgressNotification(
        channelId = SYNC_NOTIFICATION_CHANNEL_ID,
        title = R.string.sync_work_notification_title,
        total = total,
        current = current,
        icon = R.drawable.ic_sync,
        pendingIntent = getDefaultIntent(this),
    )
}

/**
 * Returns the default MainActivity intent for the notification
 *
 * @param context The application context.
 * @return The default pending intent for the notification.
 */
private fun getDefaultIntent(context: Context) = PendingIntent.getActivity(
    /* context = */
    context,
    /* requestCode = */
    MAIN_DEFAULT_INTENT_REQUEST_CODE,
    /* intent = */
    Intent().apply {
        setClassName(
            context.packageName,
            "${context.packageName}.MainActivity",
        )
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    },
    /* flags = */
    PendingIntent.FLAG_IMMUTABLE,
)
