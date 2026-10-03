package com.dev.timeflow.Data.Repo

import android.content.Context
import com.dev.timeflow.Data.Model.CountDown
import com.dev.timeflow.Data.Model.Events
import dagger.hilt.EntryPoint
import dagger.hilt.EntryPoints
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class WidgetRepo @Inject constructor(
    private val eventRepo: EventRepo
) {
    @EntryPoint
    @InstallIn(SingletonComponent::class)
    interface TimeFlowWidgetEntryPoint{
        fun timeFlowWidgetRepo(): WidgetRepo
    }


    companion object{
        fun get(applicationContext: Context): WidgetRepo {
            val widgetEntryPoint : TimeFlowWidgetEntryPoint = EntryPoints.get(
                applicationContext,
                TimeFlowWidgetEntryPoint::class.java
            )
            return widgetEntryPoint.timeFlowWidgetRepo()
        }
    }


    suspend fun getCountDown(id: Long) : CountDown?{
        return eventRepo.getCountDown(id = id)
    }

    suspend fun getEvent(id: Long): Events? {
        return eventRepo.getEvent(id = id)
    }

    suspend fun deleteCountDown(countDown: CountDown){
        eventRepo.deleteCountDown(countDown = countDown)
    }




}