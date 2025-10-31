package com.dev.timeflow.Data.Dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Upsert
import com.dev.timeflow.Data.Model.Tasks
import kotlinx.coroutines.flow.Flow

@Dao
 interface TaskDao{
     // function to insert or add a task to the database
     @Insert
     suspend fun insertTask(tasks: Tasks)

    // function to update the task
    @Upsert
    suspend fun updateTask(tasks: Tasks)

    // function to delete the task
    @Delete
    suspend fun deleteTask(tasks: Tasks)

    @Query("SELECT * FROM TASKS WHERE createdAt BETWEEN :start AND :end")
    fun getTaskForDate(start: Long, end : Long) : Flow<List<Tasks>>

    // function to get all the tasks
    @Query("SELECT * FROM TASKS ORDER BY createdAt")
     fun getAllTasks() : Flow<List<Tasks>>
 }