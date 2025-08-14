package com.dev.timeflow

import android.appwidget.AppWidgetManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.dev.timeflow.Data.Repo.WidgetRepo
import com.dev.timeflow.View.Widget.EventWidget.EventSelectionScreen
import com.example.compose.TimeFlowTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class EventActivity : ComponentActivity(){


    @Inject
    lateinit var widgetRepo: WidgetRepo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val widgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, -1)
        if (widgetId == -1){
            finish()
            return
        }

        setContent {
            TimeFlowTheme{
                EventSelectionScreen(
                    widgetId = widgetId,
                    widgetRepo = widgetRepo
                )
            }
        }

    }



}