package com.dev.timeflow.Managers.notification

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import com.dev.timeflow.Data.Model.NotificationAlarmManagerModel
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.Calendar


class TimeFlowAlarmManagerService(
    @ApplicationContext private val context: Context,
){
    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager


    fun scheduleNotification (notificationAlarmManagerModel: List<NotificationAlarmManagerModel>){
        Log.d("TIMEFLOW ALARM MANAGER", "the alarm manager schedule notification function has started {}")

          notificationAlarmManagerModel.forEach { notificationAlarmManagerModel ->
              Log.d("TIMEFLOW ALARM MANAGER", "the current task_event_name is ${notificationAlarmManagerModel.title}")
              val intent = Intent(context, NotificationAlarmManagerReceiver::class.java).apply {
                  putExtra("task_event_name", notificationAlarmManagerModel.title)
                  putExtra("task_event_id",notificationAlarmManagerModel.id.toInt() * 5 +notificationAlarmManagerModel.hour)
                  putExtra("task_event_hour",notificationAlarmManagerModel.hour)
                  putExtra("task_event_min",notificationAlarmManagerModel.minute)

              }

              val requestCode =
                    ( notificationAlarmManagerModel.id + notificationAlarmManagerModel.hour *100 +notificationAlarmManagerModel.minute).hashCode()

              val pendingIntent = PendingIntent.getBroadcast(
                  context,
                  requestCode,
                  intent,
                  PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
              )


              val calendar = Calendar.getInstance().apply {
                  set(Calendar.DAY_OF_MONTH, notificationAlarmManagerModel.localDate.dayOfMonth)
                  set(Calendar.MONTH, notificationAlarmManagerModel.localDate.month.value - 1)
                  set(Calendar.YEAR, notificationAlarmManagerModel.localDate.year)
                  set(Calendar.HOUR_OF_DAY, notificationAlarmManagerModel.hour)
                  set(Calendar.MINUTE, notificationAlarmManagerModel.minute)
                  set(Calendar.SECOND, 0)
                  set(Calendar.MILLISECOND, 0)
              }
              Log.d(
                  "TIMEFLOW ALARM MANAGER",
                  "the month is ${notificationAlarmManagerModel.localDate.month.value - 1}"
              )

              if (calendar.timeInMillis < System.currentTimeMillis()) {
                  Log.d("TIMEFLOW", "Skipping past alarm for ${notificationAlarmManagerModel.title}")
                  return@forEach
              }


              if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                  alarmManager.setExactAndAllowWhileIdle(
                      AlarmManager.RTC_WAKEUP,
                      calendar.timeInMillis,
                      pendingIntent
                  )

              } else {
                  alarmManager.setExact(
                      AlarmManager.RTC_WAKEUP,
                      calendar.timeInMillis,
                      pendingIntent
                  )
              }
          }



        Log.d("TIMEFLOW ALARM MANAGER", "the alarm manager schedule notification function has ended")
    }

//    fun scheduleNotification(){
//        alarmManager.
//    }

}