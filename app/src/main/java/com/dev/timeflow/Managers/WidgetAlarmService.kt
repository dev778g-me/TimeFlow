package com.dev.timeflow.Managers

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.icu.util.Calendar
import android.os.Build
import android.os.SystemClock
import android.util.Log

class WidgetAlarmService(
    private val context: Context
) {

    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    val pendingIntent = PendingIntent.getBroadcast(
        context,
        1,
        Intent(context, WidgetAlarmReceivers::class.java)
        , PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )

    fun scheduleAlarmForUpdatingWidgets(){

        val calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY,0)
            set(Calendar.MINUTE,0)
            set(Calendar.SECOND,0)
            set(Calendar.MILLISECOND,0)

            if (timeInMillis <= System.currentTimeMillis()) {
                add(Calendar.DAY_OF_YEAR, 1)
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                pendingIntent,
                )
        } else {
            alarmManager.setExact(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                pendingIntent
            )
        }


        Log.d("WidgetAlarm", "Midnight alarm scheduled for: ${calendar.time}")

    }

}