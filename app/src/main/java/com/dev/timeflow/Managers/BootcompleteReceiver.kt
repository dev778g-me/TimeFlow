package com.dev.timeflow.Managers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.dev.timeflow.Data.Model.NotificationAlarmManagerModel
import com.dev.timeflow.Data.Repo.EventRepo
import com.dev.timeflow.Data.Repo.TaskRepo
import com.dev.timeflow.Managers.notification.TimeFlowAlarmManagerService
import com.dev.timeflow.View.utils.toHour
import com.dev.timeflow.View.utils.toLocalDate
import com.dev.timeflow.View.utils.toMinute
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.ZoneId
import javax.inject.Inject


@AndroidEntryPoint
class BootCompleteReceiver (
) : BroadcastReceiver(){
    @Inject lateinit var taskRepo: TaskRepo
    @Inject lateinit var eventRepo: EventRepo
    override fun onReceive(context: Context, intent: Intent) {


        val pendingResult = goAsync()
        val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)


        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            WidgetAlarmService(context).scheduleAlarmForUpdatingWidgets()

            scope.launch {

                try {
                    val startDay = LocalDate.now()
                        .atStartOfDay().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()


                    val task = taskRepo.getTaskForScheduling(
                        start = startDay
                    ).first().map {
                        NotificationAlarmManagerModel(
                            id = it.id,
                            title = it.name,
                            hour = it.taskTime!!.toHour(),
                            minute = it.taskTime.toMinute(),
                            localDate = it.taskTime.toLocalDate()
                        )
                    }

                    val event = eventRepo.getEventsForNotification(
                        start = startDay
                    ).first().map {
                        NotificationAlarmManagerModel(
                            id = it.id,
                            title = it.name,
                            hour = it.eventNotificationTime.toHour(),
                            minute = it.eventNotificationTime.toMinute(),
                            localDate = it.eventNotificationTime.toLocalDate()
                        )
                    }

                    val notificationAlarmManagerModel = task + event

                    TimeFlowAlarmManagerService(context = context).scheduleNotification(
                        notificationAlarmManagerModel = notificationAlarmManagerModel
                    )
                } catch (e: Exception) {
                    println(e.localizedMessage)
                } finally {

                    pendingResult.finish()
                }


            }

        }
    }
}