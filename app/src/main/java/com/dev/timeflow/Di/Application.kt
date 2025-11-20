package com.dev.timeflow.Di

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class Application : Application(){
   companion object{
       const  val TIMEFLOW_NOTIFICATION_ID = "timeflow_notification_id"
       const val TIMEFLOW_NOTIFICATION_EVENT_ID = "timeflow_notification_event_id"
   }
    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        createEventNotificationChannel()
    }


    private fun createNotificationChannel(){
        val channel = NotificationChannel(
            TIMEFLOW_NOTIFICATION_ID,
            "Task Notification",
            NotificationManager.IMPORTANCE_HIGH
        )

        channel.description = "Task Notification Channel"

        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        notificationManager.createNotificationChannel(channel)
    }

    private fun createEventNotificationChannel(){
        val channel = NotificationChannel(
            TIMEFLOW_NOTIFICATION_EVENT_ID,
            "Event Notifications",
            NotificationManager.IMPORTANCE_HIGH
        )

        channel.description = "Event Notification Channel"

        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        notificationManager.createNotificationChannel(channel)
    }





}