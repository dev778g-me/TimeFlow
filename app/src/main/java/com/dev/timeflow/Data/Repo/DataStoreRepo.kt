package com.dev.timeflow.Data.Repo

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.longPreferencesKey
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import okio.IOException
import javax.inject.Inject

class DataStoreRepo @Inject constructor(
    @ApplicationContext private val context: Context,
    private val dataStore: DataStore<Preferences>
){

    private fun eventIdKey(widgetId: Long) = longPreferencesKey("widget_event_id_$widgetId")


    suspend fun saveEventId(widgetId: Long,eventId : Long){
        dataStore.edit {
            it[eventIdKey(widgetId = widgetId)] = eventId
        }
    }

    fun readEventId(widgetId: Long) : Flow<Long?>{
        return dataStore.data
            .catch {
                if (it is IOException ) emit(emptyPreferences()) else throw it
            }.map {
                pref ->
                pref[eventIdKey(
                    widgetId = widgetId
                )]
            }
    }
}