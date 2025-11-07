package com.dev.timeflow.Di.Module

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.room.Room
import com.dev.timeflow.Data.Dao.EventDao
import com.dev.timeflow.Data.Dao.TaskDao
import com.dev.timeflow.Data.EventDatabase
import com.dev.timeflow.Data.Repo.DataStoreRepo
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
    private val Context.dataStore by preferencesDataStore(name = "user_prefs")
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

    // provide the datastore prefrences
    private val PREF_NAME = "onboarding_pref"
    @Provides
    @Singleton
    fun provideDataStore(
        @ApplicationContext context: Context
    ) : DataStore<Preferences>{
        return PreferenceDataStoreFactory.create(
            produceFile = {
                context.preferencesDataStoreFile(
                    PREF_NAME
                )
            }
        )
    }



    @Singleton
    @Provides
    fun provideDataStoreRepo(
        @ApplicationContext context: Context,
        dataStore: DataStore<Preferences>
    ) : DataStoreRepo{
        return DataStoreRepo(
            context = context,
            dataStore = dataStore
        )
    }


}