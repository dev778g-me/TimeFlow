package com.dev.timeflow.Managers.service

import android.appwidget.AppWidgetManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.glance.GlanceId
import androidx.glance.appwidget.GlanceAppWidgetManager
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
        val appWidgetId = intent?.getIntExtra(
            AppWidgetManager.EXTRA_APPWIDGET_ID,
            AppWidgetManager.INVALID_APPWIDGET_ID
        ) ?: AppWidgetManager.INVALID_APPWIDGET_ID

        val countdownId = intent?.getLongExtra(CountDownWidgetState.EXTRA_COUNTDOWN_ID, -1L) ?: -1L

        if (appWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID || countdownId <= 0L) {
            Log.w(TAG, "Pin callback missing ids: appWidgetId=$appWidgetId countdownId=$countdownId")
            return
        }

        val pendingResult = goAsync()
        CoroutineScope(Dispatchers.Default).launch {
            try {
                val glanceId = awaitGlanceId(context, appWidgetId)
                CountDownWidgetState.setCountdownId(context, glanceId, countdownId)
                CountDownWidget().update(context, glanceId)
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Countdown widget added", Toast.LENGTH_SHORT).show()
                }
            } catch (t: Throwable) {
                Log.e(TAG, "Failed to bind countdown $countdownId to widget $appWidgetId", t)
            } finally {
                pendingResult.finish()
            }
        }
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
