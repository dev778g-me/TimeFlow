package com.dev.timeflow.Managers.utils

import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.util.Calendar

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

fun Long.toLocalDate() : LocalDate {
          return Instant.ofEpochMilli(this)
              .atZone(ZoneId.systemDefault())
              .toLocalDate()
}