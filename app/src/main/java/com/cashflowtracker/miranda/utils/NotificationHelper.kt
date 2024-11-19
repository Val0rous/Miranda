package com.cashflowtracker.miranda.utils

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.compose.ui.graphics.toArgb
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.cashflowtracker.miranda.R
import com.cashflowtracker.miranda.data.database.Notification
import com.cashflowtracker.miranda.ui.theme.LocalCustomColors
import java.time.Duration
import java.time.ZonedDateTime
import java.util.concurrent.TimeUnit

object NotificationHelper {
    private val channelId = "miranda-notification-channel"
    private val channelName = "Miranda Notification"
//    private val notificationId = 1001

    @SuppressLint("MissingPermission")
    fun sendNotification(
        context: Context,
        notificationId: Int,
        type: String,
        source: String,
        destination: String,
        amount: Double,
        currency: Currencies,
        comment: String,
        icon: Int,
//        color: Int
    ) {
        createNotificationChannel(context)

        val notification = NotificationCompat.Builder(context, channelId)
            .setContentTitle(comment)
            .setContentText("${formatAmount(amount, currency, type)} with $source to $destination")
            .setSmallIcon(icon)
            .setAutoCancel(true)
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