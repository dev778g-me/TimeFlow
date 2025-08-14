package com.dev.timeflow.View.Widget.MonthProgress

import android.content.Context
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver

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




