package com.dev.timeflow.Data.Dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Upsert
import com.dev.timeflow.Data.Model.Events
import kotlinx.coroutines.flow.Flow
import java.sql.Date

@Dao
interface EventDao {
    // insert an event into the database
    @Insert
    suspend fun insertEvent(events: Events)

    // update the event in the database
    @Upsert
    suspend fun updateEvent(events: Events)

    @Delete
    suspend fun deleteEvent(events: Events)

    // function to get all the events
    @Query("SELECT * FROM events")
    fun getAllEvents(): Flow<List<Events>>

    // function to get event for the date
    @Query("SELECT * FROM events WHERE createdAt BETWEEN :start AND :end")
    fun getAllEventsForADate(start: Long, end: Long): Flow<List<Events>>

    // function to get all events which have notification
    @Query("SELECT * FROM events WHERE notification =1 AND createdAt > :start")
    fun getEventsForNotification(start: Long) : Flow<List<Events>>

    // function to get an event by the id
    @Query("SELECT * FROM events WHERE id = :id")
    fun getEventById(id: Long): Flow<Events>
}