package com.dev.timeflow.Data.Repo

import com.dev.timeflow.Data.Dao.EventDao
import com.dev.timeflow.Data.Model.Events
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class EventRepo @Inject constructor(
   private val eventDao: EventDao
){
  suspend  fun getAllEvents() : Flow<List<Events>>{
        return eventDao.getAllEvents()
    }

    // function to insert an event into the database
    suspend fun insertEvent(events: Events){
        eventDao.insertEvent(
            events = events
        )
    }

    // function to update the event
    suspend fun updateEvent (events: Events){
        eventDao.updateEvent(events = events)
    }
  // function to get all events for a particular date
  suspend fun getEventsForADate(start: Long, end : Long) : Flow <List<Events>> {
      return eventDao.getAllEventsForADate(
          start = start,
          end = end
      )
  }
    suspend fun getEventsForNotification (start: Long) : Flow<List<Events>>{
        return eventDao.getEventsForNotification(
            start = start
        )
    }

    // function to delete an event from the database
    suspend fun deleteEvent(events: Events){
        eventDao.deleteEvent(
            events = events
        )
    }

}