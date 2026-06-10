package com.smartsplit.app.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import timber.log.Timber

object NotificationHelper {
    private const val CHANNEL_ID   = "smartsplit_channel"
    private const val CHANNEL_NAME = "SmartSplit"
    private var notifId            = 0

    fun createChannel(context: Context) {
        val channel = NotificationChannel(
            CHANNEL_ID,
            CHANNEL_NAME,
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = "SmartSplit bill notifications"
        }
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(channel)
        Timber.d("Notification channel created")
    }

    fun showBillReminderNotification(context: Context, pendingBillsCount: Int) {
        val prefs = com.smartsplit.app.data.preferences.AppPreferences(context)
        if (!prefs.notificationsEnabled) return

        if (!NotificationManagerCompat.from(context).areNotificationsEnabled()) return

        try {
            val notification = NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_dialog_alert)
                .setContentTitle("Ada Tagihan Belum Lunas!")
                .setContentText("Kamu memiliki $pendingBillsCount tagihan yang belum diselesaikan. Yuk, ingatkan teman-temanmu!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)
                .build()

            NotificationManagerCompat.from(context).notify(999, notification)
            Timber.d("Push reminder notification shown for $pendingBillsCount bills")
        } catch (e: SecurityException) {
            Timber.w("Notification permission not granted")
        }
    }

    fun showBillSavedNotification(context: Context, restaurantName: String) {
        if (!NotificationManagerCompat.from(context).areNotificationsEnabled()) return
        try {
            val notification = NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle("Tagihan Disimpan")
                .setContentText("Tagihan $restaurantName berhasil disimpan.")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)
                .build()

            NotificationManagerCompat.from(context).notify(notifId++, notification)
            Timber.d("Notification shown for: $restaurantName")
        } catch (e: SecurityException) {
            Timber.w("Notification permission not granted")
        }
    }
}