package com.dev.timeflow.Managers.notification

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent

/**
 * Single source of truth for alarm identity.
 *
 * The request code is derived from the owning entity (type + id) ONLY. It must never
 * encode the reminder time, hour or minute: doing so makes an edited reminder
 * unidentifiable, so its old alarm can never be cancelled and the event fires twice.
 *
 * Scheduling is therefore always cancel-then-set, which is idempotent and cannot
 * orphan an alarm.
 */
object AlarmKeys {

    const val TYPE_EVENT = 0
    const val TYPE_TASK = 1

    const val EXTRA_ID = "task_event_id"
    const val EXTRA_TYPE = "task_event_type"
    const val EXTRA_TITLE = "task_event_name"
    const val EXTRA_START_TIME = "event_start_time"
    const val EXTRA_END_TIME = "event_end_time"
    const val EXTRA_HOUR = "task_event_hour"
    const val EXTRA_MINUTE = "task_event_min"
    const val EXTRA_NOTIFICATION_ID = "notification_id"

    fun requestCode(type: Int, id: Long): Int = "timeflow:$type:$id".hashCode()

    /**
     * Shade id for a reminder.
     *
     * Events and tasks live in separate Room databases, so their autoincrement ids
     * overlap: event 1 and task 1 both exist. The sign separates the two tables, which
     * is all that is needed to keep one notification from overwriting the other.
     *
     * This replaces `id * 5 + hour`, which was not injective - an event at 09:00 and a
     * task four hours later both resolved to 14, and the minute was discarded entirely.
     */
    fun reminderNotificationId(type: Int, id: Long): Int =
        if (type == TYPE_TASK) id.toInt() else -id.toInt()

    private fun pendingIntent(context: Context, requestCode: Int, create: Boolean): PendingIntent? =
        PendingIntent.getBroadcast(
            context,
            requestCode,
            Intent(context, NotificationAlarmManagerReceiver::class.java),
            PendingIntent.FLAG_IMMUTABLE or
                if (create) PendingIntent.FLAG_UPDATE_CURRENT else PendingIntent.FLAG_NO_CREATE
        )

    /**
     * Cancels any alarm previously scheduled for this entity. Safe to call when none
     * exists (FLAG_NO_CREATE returns null) and safe to call for an entity that has
     * never had a reminder.
     */
    fun cancel(context: Context, type: Int, id: Long) {
        val pendingIntent = pendingIntent(context, requestCode(type, id), create = false) ?: return
        (context.getSystemService(Context.ALARM_SERVICE) as AlarmManager).cancel(pendingIntent)
        pendingIntent.cancel()
    }
}
