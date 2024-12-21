package com.cashflowtracker.miranda.utils

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.cashflowtracker.miranda.R

object NotificationHelper {
    private val channelId = "miranda-notification-channel"
    private val channelName = "Miranda Notification"
//    private val notificationId = 1001

    @SuppressLint("MissingPermission")
    fun sendNotification(
        context: Context,
        notificationId: Int,
        notificationType: String,
        recurrenceType: String,
        source: String,
        destination: String,
        amount: Double,
        currency: Currencies,
        comment: String,
        icon: Int,
        pendingIntent: PendingIntent
//        color: Int
    ) {
        createNotificationChannel(context)

        val notification = NotificationCompat.Builder(context, channelId)
            .setContentTitle(
                "$comment ${context.getString(R.string.notification_renews)} ${
                    context.getString(
                        Notifications.valueOf(notificationType).shortDesc
                    )
                }"
            )
            .setContentText(
                "${
                    formatAmount(
                        amount,
                        currency,
                        recurrenceType
                    )
                } ${context.getString(R.string.notification_with)} $source ${context.getString(R.string.notification_to)} $destination"
            )
            .setSmallIcon(icon)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
//            .setColor(color)
            .build()

        NotificationManagerCompat.from(context).notify(notificationId, notification)
    }

    @SuppressLint("ObsoleteSdkInt")
    private fun createNotificationChannel(context: Context) {
        // Android versions below O do not require creating notification channels
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val notificationManager = context.getSystemService(NotificationManager::class.java)
            notificationManager?.createNotificationChannel(channel)
        }
    }
}