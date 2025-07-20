package com.dev.timeflow.Managers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.glance.appwidget.GlanceAppWidgetManager
import com.dev.timeflow.Presentation.Widget.EventWidget.EventProgress
import com.dev.timeflow.Presentation.Widget.MonthProgress.MonthProgress
import com.dev.timeflow.Presentation.Widget.WeekProgress.WeekProgress
import com.dev.timeflow.Presentation.Widget.YearProgress.YearProgress
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.jvm.java

class WidgetAlarmReceivers : BroadcastReceiver(){
    override fun onReceive(context: Context, intent: Intent) {

        val pendingResult = goAsync()
        Log.d("Receiver","The BroadCast Receiver has Started")
        CoroutineScope(Dispatchers.Default).launch {
            try {
                val glanceAppWidgetManager = GlanceAppWidgetManager(context)
                val monthGlanceId = glanceAppWidgetManager.getGlanceIds(MonthProgress::class.java)
                val weekGlanceId = glanceAppWidgetManager.getGlanceIds(WeekProgress::class.java)
                val yearGlanceId = glanceAppWidgetManager.getGlanceIds(YearProgress::class.java)
                val eventGlanceId = glanceAppWidgetManager.getGlanceIds(EventProgress::class.java)
                Log.d("Receiver","The Month Glance id :  ${monthGlanceId.size}")

                weekGlanceId.forEach {
                    Log.d("Receiver","Week Glance Block Started")
                    WeekProgress().update(context,it)
                }
                monthGlanceId.forEach {
                    Log.d("Receiver","Month Glance Block started")
                    MonthProgress().update(context,it)
                }
                yearGlanceId.forEach {
                    Log.d("Receiver","Yearly Glance Block Started")
                    YearProgress().update(context,it)
                }

                eventGlanceId.forEach {
                    Log.d("Receiver","Event Glance Block Started")
                    EventProgress.update(
                        context = context,
                        id = it
                    )
                }
                // schedule for next midnight
                WidgetAlarmService(context).scheduleAlarmForUpdatingWidgets()

            } catch (e: Exception) {
                Log.e("BroadCast Error","${e.localizedMessage}")
            } finally {
                pendingResult.finish()
                Log.d("Receiver","The Pending Result is Finished")
            }
        }

    }
}