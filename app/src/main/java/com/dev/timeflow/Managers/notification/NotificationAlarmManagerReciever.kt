package com.dev.timeflow.Managers.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class NotificationAlarmManagerReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val message = intent.getStringExtra(AlarmKeys.EXTRA_TITLE)
        val startTime = intent.getLongExtra(AlarmKeys.EXTRA_START_TIME, 0L)
        val endTime = intent.getLongExtra(AlarmKeys.EXTRA_END_TIME, 0L)
        val type = intent.getIntExtra(AlarmKeys.EXTRA_TYPE, AlarmKeys.TYPE_EVENT)
        val id = intent.getLongExtra(AlarmKeys.EXTRA_ID, 0L)

        if (message.isNullOrBlank()) {
            Log.w("NOTIFICATION BROADCAST RECEIVER", "dropping alarm with no title")
            return
        }

        val notificationId = intent.getIntExtra(
            AlarmKeys.EXTRA_NOTIFICATION_ID,
            AlarmKeys.reminderNotificationId(type, id)
        )

        if (type == AlarmKeys.TYPE_EVENT) {
            TimeFlowNotificationManager(context).showEventNotification(
                context = context,
                message = message,
                notificationId = notificationId,
                progress = progressPercent(startTime, endTime)
            )
        } else {
            TimeFlowNotificationManager(context).showNotification(
                context, message = message, notificationId = notificationId
            )
        }
    }

    private fun progressPercent(startTime: Long, endTime: Long): Int {
        val total = endTime - startTime
        if (total <= 0L) return 0
        val elapsed = System.currentTimeMillis() - startTime
        return ((elapsed.toDouble() / total.toDouble()) * 100).toInt().coerceIn(0, 100)
    }
}
