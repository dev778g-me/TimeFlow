package com.dev.timeflow.Data.Model

import android.accessibilityservice.GestureDescription
import android.icu.text.CaseMap
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Events")
data class Events(
    @PrimaryKey(autoGenerate = true)
    val id : Long,
    val title: String,
    val description: String,
    val startTime : Long,
    val endTime : Long
)
