package com.dev.timeflow.Di.Module

import android.content.Context
import androidx.room.Room
import com.dev.timeflow.Data.Dao.EventDao
import com.dev.timeflow.Data.Dao.TaskDao
import com.dev.timeflow.Data.EventDatabase
import com.dev.timeflow.Data.Repo.EventRepo
import com.dev.timeflow.Data.TaskDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class TimeFlowModule (

){

    // provides the application context
    @Singleton
    @Provides
    fun provideApplicationContext(
        @ApplicationContext context: Context
    ) : Context{
        return context
    }

    // provide the event database
    @Singleton
    @Provides
    fun provideEventDatabase(
        @ApplicationContext context: Context
    ): EventDatabase {
        return Room.databaseBuilder(
            context,
            EventDatabase::class.java,
            "event_db"
        ).build()
    }

    // provide the task database
    @Singleton
    @Provides
    fun provideTaskDatabase(
        @ApplicationContext context: Context
    ) : TaskDatabase{
        return Room.databaseBuilder(
            context,
            TaskDatabase::class.java,
            "task_db"
        ).build()
    }

    // provide the task dao
    @Singleton
    @Provides
    fun provideTaskDao(
        taskDatabase: TaskDatabase
    ) : TaskDao{
        return taskDatabase.taskDao()
    }

    // provide the event dao
    @Singleton
    @Provides
    fun provideEventDao(
        eventDatabase: EventDatabase
    ): EventDao {
        return eventDatabase.eventDao()
    }

    // provides the event repository
    @Singleton
    @Provides
    fun provideEventRepository(
        eventDao: EventDao
    ): EventRepo{
        return EventRepo(
            eventDao = eventDao
        )
    }

}