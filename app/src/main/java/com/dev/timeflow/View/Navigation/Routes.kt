package com.dev.timeflow.View.Navigation

import kotlinx.serialization.Serializable

@Serializable
sealed interface Routes {
    val route : String
     @Serializable
    data object TimerScreen : Routes{
         override val route ="timer_screen"
    }
    @Serializable
    data object EventScreen : Routes{
        override val route ="event_screen"
    }
    @Serializable
    data object TaskScreen : Routes{
        override val route = "task_screen"
    }
    @Serializable
    data object AddEventScreen : Routes{
        override val route = "add_event_screen"
    }

    @Serializable
    data object NewTaskScreen : Routes{
        override val route = "new_task_screen"
    }
}