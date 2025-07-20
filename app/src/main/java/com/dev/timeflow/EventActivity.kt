package com.dev.timeflow

import android.appwidget.AppWidgetManager
import android.os.Bundle
import android.os.PersistableBundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.AnyRes
import com.dev.timeflow.Presentation.Widget.EventWidget.EventSelectionScreen
import com.example.compose.TimeFlowTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EventActivity : ComponentActivity(){


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val widgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, -1)
        if (widgetId == -1){
            finish()
            return
        }

        setContent {
            TimeFlowTheme{
                EventSelectionScreen()
            }
        }

    }



}