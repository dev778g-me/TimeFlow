package com.dev.timeflow.Di

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class Application : Application(){
   companion object{
       const  val TIMEFLOW_NOTIFICATION_ID = "timeflow_notification_id"
       const val TIMEFLOW_NOTIFICATION_NAME = "timeflow_notification_name"
   }
    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }


    private fun createNotificationChannel(){
        val channel = NotificationChannel(
            TIMEFLOW_NOTIFICATION_ID,
            "TimeFlow Notification",
            NotificationManager.IMPORTANCE_HIGH
        )

        channel.description = "TimeFlow Notification Channel"

        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        notificationManager.createNotificationChannel(channel)
    }





}