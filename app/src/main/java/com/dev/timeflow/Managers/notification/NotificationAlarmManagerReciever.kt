package com.dev.timeflow.Managers.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class NotificationAlarmManagerReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        Log.d("NOTIFICATION BROADCAST RECEIVER", "broadcast receiver started received : ${intent}")
        val message = intent.getStringExtra("task_event_name")
        val notificationId = intent.getIntExtra("task_event_id", 0)
        TimeFlowNotificationManager(context).showNotification(
            context, message = message!!, notificationId = notificationId
        )
        Log.d("NOTIFICATION BROADCAST RECEIVER", "notification function has ran")
    }
}