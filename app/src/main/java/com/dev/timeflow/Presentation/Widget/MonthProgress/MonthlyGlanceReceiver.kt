package com.dev.timeflow.Presentation.Widget.MonthProgress

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.icu.util.Calendar
import android.os.Build
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import com.dev.timeflow.Presentation.Widget.MonthProgress.MonthlyGlanceReceiver.Companion.ACTION_UPDATE_WIDGET
import java.time.Instant
import java.time.Instant.now

class MonthlyGlanceReceiver : GlanceAppWidgetReceiver(){

    companion object{
        const val ACTION_UPDATE_WIDGET = "com.dev.timeflow.UPDATE_MONTH_PROGRESS_WIDGET"
    }
    override val glanceAppWidget: GlanceAppWidget = MonthProgress()

    override fun onEnabled(context: Context?) {
        super.onEnabled(context)
        //calling
    }

    override fun onDisabled(context: Context?) {
        super.onDisabled(context)
        //disabled
    }



}




