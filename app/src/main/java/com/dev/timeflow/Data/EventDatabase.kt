package com.dev.timeflow.Data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.dev.timeflow.Data.Dao.CountdownDao
import com.dev.timeflow.Data.Dao.EventDao
import com.dev.timeflow.Data.Model.CountDown
import com.dev.timeflow.Data.Model.Events


@Database(entities = [Events::class, CountDown::class], version = 2)
abstract class EventDatabase : RoomDatabase(){
    abstract fun eventDao() : EventDao

    abstract fun countDownDao () : CountdownDao
}



val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE IF NOT EXISTS countdown_table (
                id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                name TEXT NOT NULL,
                startTime INTEGER NOT NULL,
                endTime INTEGER NOT NULL
            )
            """.trimIndent()
        )
    }
}