package com.dev.timeflow.View.Widget.countDown

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.glance.GlanceId
import androidx.glance.appwidget.state.getAppWidgetState
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.glance.state.PreferencesGlanceStateDefinition


object CountDownWidgetState {


    const val EXTRA_COUNTDOWN_ID = "countdown_id"

    private val CountdownIdKey = longPreferencesKey("countdown_id")

    suspend fun setCountdownId(context: Context, glanceId: GlanceId, countdownId: Long) {
        updateAppWidgetState(context, glanceId) { prefs ->
            prefs[CountdownIdKey] = countdownId
        }
    }

    suspend fun getCountdownId(context: Context, glanceId: GlanceId): Long? {
        val prefs: Preferences =
            getAppWidgetState(context, PreferencesGlanceStateDefinition, glanceId)
        return prefs[CountdownIdKey]
    }
}
