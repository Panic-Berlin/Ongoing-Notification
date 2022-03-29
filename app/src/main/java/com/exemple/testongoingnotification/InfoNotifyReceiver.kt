package com.exemple.testongoingnotification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.Worker
import androidx.work.WorkerParameters

class InfoNotifyReceiver(context: Context, parameters: WorkerParameters) :
    Worker(context, parameters) {
    override fun doWork(): Result {
        Log.d(TAG, "doWork: success")
        showNotification()
        return Result.success()
    }

    private fun showNotification() {
        val cache = (1..100).random()
        var percent = 0
        percent = when {
            cache < 100 -> {
                (10..35).random()
            }
            cache < 200 -> {
                (36..50).random()
            }
            else -> {
                (51..89).random()
            }
        }

        val intent = Intent(applicationContext, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(applicationContext, 0, intent, 0)

        val contentView = RemoteViews(applicationContext.packageName, R.layout.info_notification)
        contentView.setTextViewText(R.id.tv_percent, "${percent}%")
        contentView.setProgressBar(R.id.pb_percent,100, percent, false)
        val builder = NotificationCompat.Builder(applicationContext, "info_id")
            .setContent(contentView)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setContentIntent(pendingIntent)
            .setAutoCancel(false)
            .setOngoing(true)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelName = "work_channel_name"
            val channel = NotificationChannel(
                "info_id",
                channelName,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "description"
            }
            val  notificationManager = applicationContext.getSystemService(
                Context.NOTIFICATION_SERVICE
            ) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
        with(NotificationManagerCompat.from(applicationContext)) {
            notify(201, builder.build())
            Log.d(TAG, "showNotification: success")
        }

    }
}
