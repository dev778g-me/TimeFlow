package com.dev.timeflow.Data.Repo

import android.content.Context
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.updateAll
import com.dev.timeflow.Data.Dao.EventDao
import com.dev.timeflow.Data.Model.Events
import com.dev.timeflow.Presentation.Widget.EventWidget.EventProgress
import dagger.hilt.EntryPoint
import dagger.hilt.EntryPoints
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class WidgetRepo @Inject constructor(
    @ApplicationContext private  val context: Context,
    private val eventDao: EventDao,
    private val dataStoreRepo: DataStoreRepo
){

    @EntryPoint
    @InstallIn(SingletonComponent::class)
    interface TimeFlowEntryPoint {
        fun eventWidgetRepo () : WidgetRepo
    }

    companion object{
        fun get(applicationContext: Context) : WidgetRepo {
            val widgetEntryPoint : TimeFlowEntryPoint = EntryPoints.get(
                applicationContext,
                TimeFlowEntryPoint::class.java
            )
            return widgetEntryPoint.eventWidgetRepo()
        }
    }

    // function to read the event id
    fun readEventId(id : Long) : Flow<Long?>{
           return dataStoreRepo.readEventId(
               widgetId = id
           )
    }

    suspend fun updateWidget(){
        val appWidgetManager = GlanceAppWidgetManager(context)
        val glanceId = appWidgetManager.getGlanceIds(EventProgress::class.java)
        glanceId.forEach {
            EventProgress.update(
                context = context,
                id = it
            )
        }
        EventProgress.updateAll(context)
    }

   suspend fun saveEventId(
        widgetId : Long,
        eventId : Long
    ){
        dataStoreRepo.saveEventId(
            widgetId = widgetId,
            eventId = eventId
        )
    }


   fun loadEvents(id : Long) : Flow<Events>{
       return eventDao.getEventById(
            id = id
        )
    }

}