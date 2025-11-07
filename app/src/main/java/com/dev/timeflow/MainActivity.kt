package com.dev.timeflow

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import com.dev.timeflow.Data.Repo.DataStoreRepo
import com.dev.timeflow.Managers.WidgetAlarmService
import com.dev.timeflow.View.Navigation.NavGraph
import com.dev.timeflow.View.Navigation.Routes
import com.dev.timeflow.View.Screens.onBoarding.FeatureScreen
import com.dev.timeflow.View.Screens.onBoarding.WelcomeScreen
import com.dev.timeflow.Viewmodel.TaskAndEventViewModel


import com.example.compose.TimeFlowTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

   @Inject
   lateinit var dataStoreRepo: DataStoreRepo
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var keepSplashScreen = true
        installSplashScreen().setKeepOnScreenCondition {
            keepSplashScreen
        }
        WidgetAlarmService(this).scheduleAlarmForUpdatingWidgets()
        enableEdgeToEdge()
        setContent {
            TimeFlowTheme {
               var startDest by remember { mutableStateOf<String?>(null) }

                LaunchedEffect(Unit) {
                    val isCompleted = dataStoreRepo.readOnBoarding()
                        .collect {
                            if (it){
                                startDest = Routes.TimerScreen.route
                            }else {
                                startDest = Routes.WelcomeScreen.route
                            }
                            keepSplashScreen = false
                        }
                }


               startDest?.let {
                   NavGraph(
                       startDest = it
                   )
               }
            }
        }
    }



}

