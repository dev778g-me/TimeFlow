package com.dev.timeflow.Data.Dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.dev.timeflow.Data.Model.Events
import kotlinx.coroutines.flow.Flow
import java.sql.Date

@Dao
interface EventDao {
    // insert an event into the database
    @Insert
    suspend fun insertEvent(events: Events)

    @Delete
    suspend fun deleteEvent(events: Events)

    // function to get all the events
    @Query("SELECT * FROM events")
    fun getAllEvents(): Flow<List<Events>>

    // function to get event for the date
    @Query("SELECT * FROM events WHERE eventTime= :date")
    fun getAllEventsForADate(date: Long): Flow<List<Events>>

    // function to get an event by the id
    @Query("SELECT * FROM events WHERE id = :id")
    fun getEventById(id: Long): Flow<Events>
}