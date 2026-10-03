package com.dev.timeflow.View.Widget.countDown

import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver


class CountDownGlanceReceiver() : GlanceAppWidgetReceiver(){
    override val glanceAppWidget: GlanceAppWidget
        get() = CountDownWidget()
}