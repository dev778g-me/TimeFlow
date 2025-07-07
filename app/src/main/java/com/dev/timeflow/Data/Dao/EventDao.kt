package com.dev.timeflow.Data.Dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.dev.timeflow.Data.Model.Events
import kotlinx.coroutines.flow.Flow

@Dao
interface EventDao{
    // insert an event into the database
    @Insert
   suspend fun insertEvent(events: Events)

    @Delete
   suspend fun deleteEvent(events: Events)

    // function to get all the events
    @Query("SELECT * FROM events")
    fun getAllEvents() : Flow<List<Events>>
}