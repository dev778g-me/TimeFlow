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
        Log.d(
            TAG,
            "Pin callback: extras=${intent.extras} " +
                "appWidgetId=${intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID)} " +
                "countdownId=$countdownId"
        )

        if (countdownId <= 0L) {
            Log.w(TAG, "Pin callback missing countdownId: $countdownId")
            return
        }

        val pendingResult = goAsync()
        CoroutineScope(Dispatchers.Default).launch {
            try {
                val appWidgetId = resolveAppWidgetId(context, intent, countdownId)
                val glanceId = awaitGlanceId(context, appWidgetId)
                CountDownWidgetState.setCountdownId(context, glanceId, countdownId)
                CountDownWidget().update(context, glanceId)

            } catch (t: Throwable) {
                Log.e(TAG, "Failed to bind countdown $countdownId", t)
            } finally {
                pendingResult.finish()
            }
        }
    }

    private suspend fun resolveAppWidgetId(
        context: Context,
        intent: Intent,
        countdownId: Long
    ): Int {
        val fromExtra = intent.getIntExtra(
            AppWidgetManager.EXTRA_APPWIDGET_ID,
            AppWidgetManager.INVALID_APPWIDGET_ID
        )
        if (fromExtra != AppWidgetManager.INVALID_APPWIDGET_ID) return fromExtra

        val appWidgetManager = AppWidgetManager.getInstance(context)
        val glanceManager = GlanceAppWidgetManager(context)
        val component = ComponentName(context, CountDownGlanceReceiver::class.java)
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
                when (CountDownWidgetState.getCountdownId(context, glanceId)) {
                    null -> return id
                    countdownId -> if (alreadyBound == null) alreadyBound = id
                }
            }
            if (alreadyBound != null) return alreadyBound
            delay(RETRY_DELAY_MS.milliseconds)
        }
        throw IllegalStateException(
            "No widget available for countdown $countdownId, known ids=$knownIds"
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
    }
}
