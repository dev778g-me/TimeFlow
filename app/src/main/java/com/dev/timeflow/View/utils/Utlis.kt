package com.dev.timeflow.View.utils

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


//fun cheakNotificationPermission(){
//    if ( == PackageManager.PERMISSION_GRANTED)
//}


