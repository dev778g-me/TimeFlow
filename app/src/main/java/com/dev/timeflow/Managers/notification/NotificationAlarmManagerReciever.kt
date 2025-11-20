package com.dev.timeflow.Managers.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class NotificationAlarmManagerReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        Log.d("NOTIFICATION BROADCAST RECEIVER", "broadcast receiver started received : $intent")
        Log.d("NOTIFICATION BROADCAST RECEIVER", "All extras: ${intent.extras?.keySet()?.joinToString()}")
        val message = intent.getStringExtra("task_event_name")
        val notificationId = intent.getIntExtra("task_event_id", 0)
        val startTime = intent.getLongExtra("event_start_time",0)
        val endTime = intent.getLongExtra("event_end_time",0)
        val type = intent.getIntExtra("task_event_type",0)

        val currentTime = System.currentTimeMillis()
        Log.d("NOTIFICATION BROADCAST RECEIVER", "broadcast receiver started received : $type")
        val elapsed = currentTime -startTime
        val total = endTime - startTime


        val percent = ((elapsed.toFloat() / total.toFloat()) * 100f).coerceIn(0f, 100f)

        if (type == 0) {
            TimeFlowNotificationManager(context).showEventNotification(
                context,
                message = message!!,
                notificationId = notificationId,
                progress = percent.toInt()
            )
        } else {
            TimeFlowNotificationManager(context).showNotification(
                context, message = message!!, notificationId = notificationId
            )
            Log.d("NOTIFICATION BROADCAST RECEIVER", "notification function has ran")
        }




    }
}