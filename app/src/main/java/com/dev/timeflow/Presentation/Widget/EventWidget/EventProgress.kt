package com.dev.timeflow.Presentation.Widget.EventWidget

import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.action.ActionParameters
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.LinearProgressIndicator
import androidx.glance.appwidget.action.ActionCallback
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.appwidget.cornerRadius
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import com.dev.timeflow.Data.Repo.WidgetRepo
import com.dev.timeflow.EventActivity
import com.dev.timeflow.MainActivity
import dagger.hilt.android.EntryPointAccessors

object EventProgress : GlanceAppWidget() {
    override suspend fun provideGlance(
        context: Context,
        id: GlanceId
    ) {

    val appContext = context.applicationContext
        val entryPoint = EntryPointAccessors.fromApplication(
            context = appContext,
            WidgetRepo.TimeFlowEntryPoint::class.java
        )
        val eventRepo = entryPoint.eventWidgetRepo()

    val widgetId = id.hashCode().toLong()


      provideContent {
       val eventIdState = eventRepo.readEventId(
           id = widgetId
       ).collectAsState(initial = null)
       val eventId = eventIdState.value
          GlanceTheme {
              if (eventId == null){
                  Box (
                      modifier = GlanceModifier.fillMaxSize()
                          .clickable(
                              onClick = actionRunCallback(LaunchActivityForSelectionOfEvent::class.java)
                          )
                          .background(GlanceTheme.colors.widgetBackground)
                          .cornerRadius(16.dp),
                      contentAlignment = Alignment.Center
                  ){
                      Text(
                          text = "Select a event"
                      )
                  }
              } else {
                 val eventState =  eventRepo.loadEvents(
                      id = eventId
                  ).collectAsState(initial = null)
                 val event = eventState.value
                  if (event == null){
                      Box(
                          modifier = GlanceModifier.fillMaxSize()
                              .background(GlanceTheme.colors.widgetBackground)
                              .cornerRadius(16.dp),
                          contentAlignment = Alignment.Center
                      ) {
                          Text(
                              text = "Loading...."
                          )
                      }

                  } else {
                      Box(
                          modifier = GlanceModifier.fillMaxSize()
                              .clickable(
                                  onClick = actionRunCallback(onEventAdded::class.java)
                              )
                              .padding(
                                  12.dp
                              )
                              .background(GlanceTheme.colors.widgetBackground)
                              .cornerRadius(16.dp),
                          contentAlignment = Alignment.Center
                      ) {
                          Column(
                              modifier = GlanceModifier.fillMaxSize(),
                              horizontalAlignment = Alignment.CenterHorizontally
                          ) {
                              Text(
                                  modifier = GlanceModifier.padding(
                                      vertical = 4.dp
                                  ),
                                  text = event.title,
                                  style = TextStyle(
                                      fontWeight = FontWeight.Bold,
                                      fontSize = 16.sp,
                                      color = GlanceTheme.colors.onPrimaryContainer
                                  ),
                                  maxLines = 1
                              )
                              val startDate = event.startTime
                              val endDate = event.endTime
                              val currentDay = System.currentTimeMillis()
                              val difference = endDate - startDate
                              val elapsed = currentDay - startDate
                              val progress = if (difference >0){
                                  (elapsed.toFloat() / difference.toFloat()) * 100f
                              } else {
                                  0f
                              }
                              Text(
                                  modifier = GlanceModifier.padding(
                                      vertical = 4.dp
                                  ),
                                  text = "$progress %",
                                  style = TextStyle(
                                      fontWeight = FontWeight.Bold,
                                      fontSize = 12.sp,
                                      color = GlanceTheme.colors.onPrimaryContainer
                                  ),
                                  maxLines = 1
                              )
                              Spacer(modifier = GlanceModifier.defaultWeight())


                              Box {
                                  LinearProgressIndicator(
                                      progress = progress /100f,
                                      modifier = GlanceModifier.height(10.dp).fillMaxWidth(),
                                              color = GlanceTheme.colors.primary,
                                      backgroundColor = GlanceTheme.colors.primaryContainer
                                  )
                              }
                          }
                      }
                  }

              }
          }
      }
    }
}


object onEventAdded : ActionCallback{
    override suspend fun onAction(
        context: Context,
        glanceId: GlanceId,
        parameters: ActionParameters
    ) {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or
                    Intent.FLAG_ACTIVITY_CLEAR_TOP or
                    Intent.FLAG_ACTIVITY_SINGLE_TOP
        }
        context.startActivity(intent)
    }
}

object LaunchActivityForSelectionOfEvent : ActionCallback{
    override suspend fun onAction(
        context: Context,
        glanceId: GlanceId,
        parameters: ActionParameters
    ) {
        val intent = Intent(context, EventActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_MULTIPLE_TASK or Intent.FLAG_ACTIVITY_NO_HISTORY
            putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, glanceId.hashCode())
        }

         context.startActivity(intent)
    }
}