package com.dev.timeflow.Data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.dev.timeflow.Data.Dao.TaskDao
import com.dev.timeflow.Data.Model.Tasks

@Database(
    entities = [Tasks::class],
    version = 1
)
abstract class TaskDatabase : RoomDatabase(){
    abstract fun taskDao() : TaskDao
}