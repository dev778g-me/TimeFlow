package com.dev.timeflow.Data.Model

import java.time.LocalDate

data class NotificationAlarmManagerModel(
    val id : Long,
    val title : String,
    val hour : Int,
    val minute : Int,
    val localDate : LocalDate
)