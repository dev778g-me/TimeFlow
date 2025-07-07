package com.dev.timeflow.Data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.dev.timeflow.Data.Dao.EventDao
import com.dev.timeflow.Data.Model.Events


@Database(entities = [Events::class], version = 1)
abstract class EventDatabase : RoomDatabase(){
    abstract fun eventDao() : EventDao
}