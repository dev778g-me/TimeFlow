package com.dev.timeflow.View.Widget.WeekProgress

import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver

class WeekGlanceReceiver : GlanceAppWidgetReceiver(){
    override val glanceAppWidget: GlanceAppWidget = WeekProgress()
}