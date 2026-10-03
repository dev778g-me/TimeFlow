package com.dev.timeflow.Data.Repo

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.dev.timeflow.Data.Model.BodyFont
import com.dev.timeflow.Data.Model.ColorSpecVersion
import com.dev.timeflow.Data.Model.ContrastLevel
import com.dev.timeflow.Data.Model.DEFAULT_PALETTE_STYLE
import com.dev.timeflow.Data.Model.ThemePreferences
import com.dev.timeflow.Data.Model.ThemeType
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

        val themeType = stringPreferencesKey("theme_type")
        val isDynamicTheme = booleanPreferencesKey("is_dynamic_theme")

       val isAmoled = booleanPreferencesKey("is_amoled_mode")
        val seedColor = longPreferencesKey("seed_color")
        val paletteStyle = stringPreferencesKey("palette_style")
        val colorSpecVersion = intPreferencesKey("color_spec_version")
        val contrastLevel = stringPreferencesKey("contrast_level")
        val bodyFont = stringPreferencesKey("body_font")
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
            if (it is IOException) emit(emptyPreferences()) else throw it
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

    fun readThemePreferences(): Flow<ThemePreferences> {
        return dataStore.data
            .catch {
                if (it is IOException) emit(emptyPreferences()) else throw it
            }.map { pref ->
                ThemePreferences(
                    themeType = enumOrDefault(pref[PrefKey.themeType], ThemeType.System),
                    isDynamicTheme = pref[PrefKey.isDynamicTheme] ?: true,
                    isAmoled = pref[PrefKey.isAmoled] ?:false,
                    seedColor = pref[PrefKey.seedColor],
                    paletteStyle = pref[PrefKey.paletteStyle] ?: DEFAULT_PALETTE_STYLE,
                    colorSpecVersion = ColorSpecVersion.fromCode(
                        pref[PrefKey.colorSpecVersion] ?: ColorSpecVersion.Default.code
                    ),
                    contrastLevel = enumOrDefault(pref[PrefKey.contrastLevel], ContrastLevel.Standard),
                    bodyFont = enumOrDefault(pref[PrefKey.bodyFont], BodyFont.SpaceGrotesk),
                )
            }
    }

    suspend fun saveThemeType(value: ThemeType) {
        dataStore.edit { it[PrefKey.themeType] = value.name }
    }

    suspend fun saveDynamicTheme(value: Boolean) {
        dataStore.edit { it[PrefKey.isDynamicTheme] = value }
    }

    suspend fun saveAmoled(value: Boolean) {
        dataStore.edit { it[PrefKey.isAmoled] = value }
    }
    suspend fun saveSeedColor(value: Long?) {
        dataStore.edit {
            if (value == null) it.remove(PrefKey.seedColor)
            else it[PrefKey.seedColor] = value
        }
    }

    suspend fun savePaletteStyle(value: String) {
        dataStore.edit { it[PrefKey.paletteStyle] = value }
    }

    suspend fun saveColorSpecVersion(value: ColorSpecVersion) {
        dataStore.edit { it[PrefKey.colorSpecVersion] = value.code }
    }

    suspend fun saveContrastLevel(value: ContrastLevel) {
        dataStore.edit { it[PrefKey.contrastLevel] = value.name }
    }

    suspend fun saveBodyFont(value: BodyFont) {
        dataStore.edit { it[PrefKey.bodyFont] = value.name }
    }

    private inline fun <reified T : Enum<T>> enumOrDefault(value: String?, default: T): T =
        enumValues<T>().firstOrNull { it.name == value } ?: default
}