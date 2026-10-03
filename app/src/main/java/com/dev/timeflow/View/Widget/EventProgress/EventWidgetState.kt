package com.dev.timeflow.View.Widget.EventProgress

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.glance.GlanceId
import androidx.glance.appwidget.state.getAppWidgetState
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.glance.state.PreferencesGlanceStateDefinition


object EventWidgetState {


    const val EXTRA_EVENT_ID = "event_id"

    private val EventIdKey = longPreferencesKey("event_id")

    suspend fun setEventId(context: Context, glanceId: GlanceId, eventId: Long) {
        updateAppWidgetState(context, glanceId) { prefs ->
            prefs[EventIdKey] = eventId
        }
    }

    suspend fun getEventId(context: Context, glanceId: GlanceId): Long? {
        val prefs: Preferences =
            getAppWidgetState(context, PreferencesGlanceStateDefinition, glanceId)
        return prefs[EventIdKey]
    }
}
