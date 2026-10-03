package com.dev.timeflow.View.utils

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.dev.timeflow.Managers.service.WidgetPin
import com.dev.timeflow.View.Widget.countDown.CountDownGlanceReceiver
import com.dev.timeflow.View.Widget.countDown.CountDownWidgetState
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date

class Utlis {

}

fun Long.toMidnight() : Long{
    val calendar = Calendar.getInstance().apply {
        timeInMillis = this@toMidnight
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE,0)
        set(Calendar.SECOND,0)
        set(Calendar.MILLISECOND, 0)
    }
    return  calendar.timeInMillis
}

fun Long.toLocalDate(): LocalDate {
    return Instant.ofEpochMilli(this)
        .atZone(ZoneId.systemDefault())
        .toLocalDate()
}

/**
 * Converts a value from Material 3's [androidx.compose.material3.DatePickerState.selectedDateMillis]
 * into a [LocalDate].
 *
 * Material 3 always encodes a picked day as midnight in UTC, so reading it with the system zone
 * (as [toLocalDate] does) shifts the day backwards on any device west of UTC. This is the exact
 * inverse of [toUtcMillis].
 */
fun Long.toUtcDate(): LocalDate {
    return Instant.ofEpochMilli(this)
        .atZone(ZoneOffset.UTC)
        .toLocalDate()
}


fun LocalDate.toMyFormat () : String {
    return  format(
        DateTimeFormatter.ofPattern("dd.MM.yyyy")
    )
}

fun LocalDate.toMillis (localTime: LocalTime) : Long {
    val calendar = Calendar.getInstance().apply {
        set(Calendar.DAY_OF_MONTH,dayOfMonth)
        set(Calendar.MONTH,month.value -1 )
        set(Calendar.YEAR,year)
        set(Calendar.HOUR_OF_DAY, localTime.hour)
        set(Calendar.MINUTE, localTime.minute)
        set(Calendar.SECOND, localTime.second)
        set(Calendar.MILLISECOND, 0)
    }

    return  calendar.timeInMillis
}


fun Long.toHour(): Int{
    val date = Date(this)
    val format = SimpleDateFormat("HH", java.util.Locale.getDefault())
    return format.format(date).toInt()
}

fun Calendar.toDateTimeInMillis(hour : Int, minute : Int, date: LocalDate) : Long{
    this.apply {
        set(Calendar.DAY_OF_MONTH,date.dayOfMonth)
        set(Calendar.MONTH, date.month.value -1)
        set(Calendar.YEAR, date.year)
        set(Calendar.HOUR_OF_DAY,hour)
        set(Calendar.MINUTE,minute)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND,0)
    }
    return this.timeInMillis
}


fun LocalDate.endOfDayMillis(): Long {
    return this.atTime(23, 59, 59, 999_000_000) // 999 ms
        .atZone(ZoneId.systemDefault())
        .toInstant()
        .toEpochMilli()
}

fun LocalDate.toUtcMillis(): Long {
    return this.atStartOfDay(ZoneOffset.UTC)
        .toInstant()
        .toEpochMilli()
}

fun Long.toMinute() : Int{
    val date = Date(this)
    val  format = SimpleDateFormat("mm", java.util.Locale.getDefault())
    return  format.format(date). toInt()
}

fun Long.toFormattedTime(): String {
    return Instant.ofEpochMilli(this)
        .atZone(ZoneId.systemDefault())
        .toLocalTime()
        .format(DateTimeFormatter.ofPattern("h:mm a"))
}


fun createCountDownWidget(context: Context, countdownId: Long) {
    val appWidgetManager = AppWidgetManager.getInstance(context)

    val id = AppWidgetManager.EXTRA_APPWIDGET_ID

    if (appWidgetManager.isRequestPinAppWidgetSupported) {
        val widgetProvider = ComponentName(context, CountDownGlanceReceiver::class.java)

        // requestCode is only a PendingIntent request code, unique per countdown so each pin
        // keeps its own extras. It is never used as a widget id or a Room id.
        val pinnedCallback = PendingIntent.getBroadcast(
            context,
            countdownId.hashCode(),
            Intent(context, WidgetPin::class.java).apply {
                putExtra(CountDownWidgetState.EXTRA_COUNTDOWN_ID, countdownId)
            },
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        appWidgetManager.requestPinAppWidget(
            widgetProvider,
            null,
            pinnedCallback
        )
    } else {
        Toast.makeText(
            context,
            "Widget pinning is not supported on this device",
            Toast.LENGTH_SHORT
        ).show()
    }
}
//fun cheakNotificationPermission(){
//    if ( == PackageManager.PERMISSION_GRANTED)
//}


