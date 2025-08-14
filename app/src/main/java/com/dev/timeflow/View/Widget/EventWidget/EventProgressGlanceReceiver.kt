package com.dev.timeflow.View.Widget.EventWidget

import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver

class EventProgressGlanceReceiver : GlanceAppWidgetReceiver(){
    override val glanceAppWidget: GlanceAppWidget
        get() = EventProgress
}