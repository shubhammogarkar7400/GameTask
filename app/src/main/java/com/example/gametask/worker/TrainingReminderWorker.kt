package com.example.gametask.worker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.gametask.R

class TrainingReminderWorker(
    private val context: Context,
    private val workerParameters: WorkerParameters)  : CoroutineWorker(context, workerParameters){

    override suspend fun doWork(): Result {
        val notificationManager: NotificationManager = context.getSystemService(Service.NOTIFICATION_SERVICE) as NotificationManager

        // Extract data from WorkerParameters
        val inputData = inputData
        val title = inputData.getString("title") ?: "Math Games"
        val description = inputData.getString("description") ?: "Start your math training"
        Log.d("Training Reminder", "title : $title description $description")

        // Create a notification channel for Android Oreo and above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(NOTIFICATION_CHANNEL_ID, NOTIFICATION_NAME, NotificationManager.IMPORTANCE_DEFAULT)
            // Configure the notification channel.
            notificationChannel.description = "Sample Channel description"
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED
            notificationChannel.enableVibration(false)
            notificationManager.createNotificationChannel(notificationChannel)
        }

        // Build the notification
        val notification = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentTitle(title)
            .setContentText(description)
            .setPriority(NotificationCompat.VISIBILITY_PUBLIC)
            .setColor(Color.RED)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .build()

        // Notify the user with the created notification
        notificationManager.notify(System.currentTimeMillis().toInt(), notification)

        return Result.success()
    }


    companion object{
        private const val NOTIFICATION_CHANNEL_ID = "Reminder"
        private const val NOTIFICATION_NAME = "Training Reminder Notifications"

    }
}