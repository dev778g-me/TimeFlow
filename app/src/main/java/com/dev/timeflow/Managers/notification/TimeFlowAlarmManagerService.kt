package com.dev.timeflow.Managers.notification

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import com.dev.timeflow.Data.Model.NotificationAlarmManagerModel
import com.dev.timeflow.View.utils.toMillis
import dagger.hilt.android.qualifiers.ApplicationContext
import java.time.LocalTime
import javax.inject.Inject

private const val TAG = "TIMEFLOW ALARM MANAGER"

class TimeFlowAlarmManagerService @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    /**
     * Schedules (or re-schedules) the single reminder alarm owned by this entity.
     *
     * Cancels first so this is idempotent: re-inserting, editing the reminder time or
     * re-running after boot all converge on exactly one pending alarm per entity. A
     * past-due reminder cancels without rescheduling, so a stale alarm can never
     * survive into a later boot.
     */
    fun schedule(model: NotificationAlarmManagerModel) {
        AlarmKeys.cancel(context, model.type, model.id)

        val triggerAt = model.localDate.toMillis(LocalTime.of(model.hour, model.minute, 0))

        if (triggerAt <= System.currentTimeMillis()) {
            Log.d(TAG, "Skipping past alarm for ${model.title}")
            return
        }

        val intent = Intent(context, NotificationAlarmManagerReceiver::class.java).apply {
            putExtra(AlarmKeys.EXTRA_ID, model.id)
            putExtra(AlarmKeys.EXTRA_TYPE, model.type)
            putExtra(AlarmKeys.EXTRA_TITLE, model.title)
            putExtra(AlarmKeys.EXTRA_START_TIME, model.startTime ?: 0L)
            putExtra(AlarmKeys.EXTRA_END_TIME, model.endTime ?: 0L)
            putExtra(AlarmKeys.EXTRA_HOUR, model.hour)
            putExtra(AlarmKeys.EXTRA_MINUTE, model.minute)
            putExtra(AlarmKeys.EXTRA_NOTIFICATION_ID, AlarmKeys.reminderNotificationId(model.type, model.id))
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            AlarmKeys.requestCode(model.type, model.id),
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                triggerAt,
                pendingIntent
            )
        } else {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerAt, pendingIntent)
        }
    }

    fun scheduleAll(models: List<NotificationAlarmManagerModel>) {
        models.forEach { schedule(it) }
    }

    /** Drops the pending reminder for an entity that no longer has one. */
    fun cancel(type: Int, id: Long) = AlarmKeys.cancel(context, type, id)
}
