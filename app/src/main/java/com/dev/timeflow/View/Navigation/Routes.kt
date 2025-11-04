package com.dev.timeflow.View.Navigation

import kotlinx.serialization.Serializable

@Serializable
sealed interface Routes {
    val route : String
     @Serializable
    data object TimerScreen : Routes{
         override val route ="m_timer_screen"
    }
    @Serializable
    data object ShowFeaturesScreen : Routes{
        override val route ="feature_screen"
    }
    @Serializable
    data object WelcomeScreen : Routes{
        override val route = "welcome_screen"
    }
    @Serializable
    data object NotificationScreen : Routes{
        override val route = "notification_screen"
    }

    @Serializable
    data object CalendarScreen : Routes{
        override val route = "m_new_task_screen"
    }
}