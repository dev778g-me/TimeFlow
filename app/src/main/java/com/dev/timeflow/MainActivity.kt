package com.dev.timeflow

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.dev.timeflow.Managers.WidgetAlarmService
import com.dev.timeflow.View.Navigation.NavGraph


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

