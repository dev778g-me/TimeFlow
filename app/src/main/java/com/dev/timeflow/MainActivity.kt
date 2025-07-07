package com.dev.timeflow

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresPermission
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.glance.LocalContext
import app.rive.runtime.kotlin.RiveInitializer
import com.dev.timeflow.Managers.WidgetAlarmService
import com.dev.timeflow.Presentation.Navigation.NavGraph


import com.dev.timeflow.Presentation.Screens.HomeScreen
import com.dev.timeflow.Presentation.Widget.MonthProgress.MonthlyGlanceReceiver
import com.example.compose.TimeFlowTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    //@RequiresPermission(Manifest.permission.SCHEDULE_EXACT_ALARM)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WidgetAlarmService(this).scheduleAlarmForUpdatingWidgets()
        enableEdgeToEdge()
        setContent {
            TimeFlowTheme {
                NavGraph()
            }
        }
    }
}

