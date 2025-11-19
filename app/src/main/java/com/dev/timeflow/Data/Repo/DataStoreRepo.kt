package com.dev.timeflow.Data.Repo

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
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

   private object PrefKey {
       val onBoardingKey = booleanPreferencesKey("on_boarding_key")
       val selectedCalendarType = intPreferencesKey("selected_calender_type")

       val userName = stringPreferencesKey("user_name")
   }


    suspend fun saveOnBoarding(completed : Boolean){
        dataStore.edit {
           it[PrefKey.onBoardingKey] = completed
        }
    }

    fun readOnBoarding() : Flow<Boolean>{
        return dataStore.data
            .catch {
                if (it is IOException ) emit(emptyPreferences()) else throw it
            }.map {
                pref ->
                val onBoardingState = pref[PrefKey.onBoardingKey] ?: false
                onBoardingState
            }
    }


    suspend fun selectedCalendar(type : Int){
        dataStore.edit {
            it[PrefKey.selectedCalendarType] = type
        }
    }

    suspend fun saveName (name : String){
        dataStore.edit {
            it[PrefKey.userName] = name
        }
    }


    fun readName () : Flow<String>{
        return dataStore.data.catch {
            if (it is IOException) emit(emptyPreferences()) else it
        }.map {
            value ->
            val userName  = value[PrefKey.userName] ?:""
            userName
        }
    }

    fun readCalenderType () : Flow<Int>{
        return dataStore.data.catch {
            if (it is IOException) emit(emptyPreferences()) else throw  it
        }.map {
            type ->
            val calendarType = type[PrefKey.selectedCalendarType] ?:0
            calendarType
        }
    }
}