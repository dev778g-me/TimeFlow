package com.dev.timeflow.Data.Repo

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
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
}