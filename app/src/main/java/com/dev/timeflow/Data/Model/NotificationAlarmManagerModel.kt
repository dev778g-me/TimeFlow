package com.dev.timeflow.Data.Model

import java.time.LocalDate

data class NotificationAlarmManagerModel(
    val id : Long,
    val title : String,
    val type : Int,
    val startTime : Long ? =0,
    val endTime : Long ? = 0,
    val hour : Int,
    val minute : Int,
    val localDate : LocalDate
)