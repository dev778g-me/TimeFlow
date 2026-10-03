package com.dev.timeflow.Managers.service

import android.appwidget.AppWidgetManager
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.glance.GlanceId
import androidx.glance.appwidget.GlanceAppWidgetManager
import com.dev.timeflow.View.Widget.EventProgress.EventProgress
import com.dev.timeflow.View.Widget.EventProgress.EventProgressGlanceReceiver
import com.dev.timeflow.View.Widget.EventProgress.EventWidgetState
import com.dev.timeflow.View.Widget.countDown.CountDownGlanceReceiver
import com.dev.timeflow.View.Widget.countDown.CountDownWidget
import com.dev.timeflow.View.Widget.countDown.CountDownWidgetState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.time.Duration.Companion.milliseconds


class WidgetPin : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent?) {
        if (intent == null) return

        val countdownId = intent.getLongExtra(CountDownWidgetState.EXTRA_COUNTDOWN_ID, -1L)
        val eventId = intent.getLongExtra(EventWidgetState.EXTRA_EVENT_ID, -1L)
        val isEvent = eventId > 0L
        val domainId = if (isEvent) eventId else countdownId
        val kind = if (isEvent) "event" else "countdown"

        Log.d(
            TAG,
            "Pin callback: extras=${intent.extras} countdownId=$countdownId eventId=$eventId"
        )

        if (domainId <= 0L) {
            Log.w(TAG, "Pin callback missing ids: countdownId=$countdownId eventId=$eventId")
            return
        }

        val pendingResult = goAsync()
        CoroutineScope(Dispatchers.Default).launch {
            try {
                val appWidgetId = resolveAppWidgetId(context, intent, domainId, isEvent)
                val glanceId = awaitGlanceId(context, appWidgetId)
                if (isEvent) {
                    EventWidgetState.setEventId(context, glanceId, eventId)
                    EventProgress().update(context, glanceId)
                } else {
                    CountDownWidgetState.setCountdownId(context, glanceId, countdownId)
                    CountDownWidget().update(context, glanceId)
                }
                Log.i(TAG, "Bound $kind $domainId to widget $appWidgetId")
                // The widget's first Glance session may have rendered with empty state
                // before the write above and can apply its stale views afterwards.
                // Render once more after that session settles to win the race.
                delay(RE_RENDER_DELAY_MS.milliseconds)
                if (isEvent) {
                    EventProgress().update(context, glanceId)
                } else {
                    CountDownWidget().update(context, glanceId)
                }
                Log.i(TAG, "Re-rendered $kind widget $appWidgetId after bind")
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Widget added", Toast.LENGTH_SHORT).show()
                }
            } catch (t: Throwable) {
                Log.e(TAG, "Failed to bind $kind $domainId", t)
            } finally {
                pendingResult.finish()
            }
        }
    }

    private suspend fun resolveAppWidgetId(
        context: Context,
        intent: Intent,
        domainId: Long,
        isEvent: Boolean
    ): Int {
        val fromExtra = intent.getIntExtra(
            AppWidgetManager.EXTRA_APPWIDGET_ID,
            AppWidgetManager.INVALID_APPWIDGET_ID
        )
        if (fromExtra != AppWidgetManager.INVALID_APPWIDGET_ID) return fromExtra

        val appWidgetManager = AppWidgetManager.getInstance(context)
        val glanceManager = GlanceAppWidgetManager(context)
        val component = ComponentName(
            context,
            if (isEvent) EventProgressGlanceReceiver::class.java
            else CountDownGlanceReceiver::class.java
        )
        var knownIds: List<Int> = emptyList()

        repeat(MAX_ATTEMPTS) {
            knownIds = appWidgetManager.getAppWidgetIds(component)?.toList().orEmpty()
            var alreadyBound: Int? = null
            for (id in knownIds.sortedDescending()) {
                val glanceId = try {
                    glanceManager.getGlanceIdBy(id)
                } catch (e: IllegalArgumentException) {
                    continue
                }
                val bound = if (isEvent) {
                    EventWidgetState.getEventId(context, glanceId)
                } else {
                    CountDownWidgetState.getCountdownId(context, glanceId)
                }
                when (bound) {
                    null -> return id
                    domainId -> if (alreadyBound == null) alreadyBound = id
                }
            }
            if (alreadyBound != null) return alreadyBound
            delay(RETRY_DELAY_MS.milliseconds)
        }
        throw IllegalStateException(
            "No widget available for ${if (isEvent) "event" else "countdown"} $domainId, known ids=$knownIds"
        )
    }

    private suspend fun awaitGlanceId(context: Context, appWidgetId: Int): GlanceId {
        var lastError: Exception? = null
        repeat(MAX_ATTEMPTS) {
            try {
                return GlanceAppWidgetManager(context).getGlanceIdBy(appWidgetId)
            } catch (e: IllegalArgumentException) {
                lastError = e
                delay(RETRY_DELAY_MS.milliseconds)
            }
        }
        throw lastError ?: IllegalStateException("No GlanceId for appWidgetId=$appWidgetId")
    }

    private companion object {
        const val TAG = "WidgetPin"
        const val MAX_ATTEMPTS = 10
        const val RETRY_DELAY_MS = 200L
        const val RE_RENDER_DELAY_MS = 3000L
    }
}
