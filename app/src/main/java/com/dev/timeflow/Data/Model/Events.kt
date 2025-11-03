package com.dev.timeflow.Data.Model

import android.accessibilityservice.GestureDescription
import android.icu.text.CaseMap
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Events")
data class Events(
    @PrimaryKey(autoGenerate = true)
    val id : Long,
    val name: String,
    val description: String,
    val eventTime : Long,
    val notification : Boolean = false,
    val createdAt : Long = System.currentTimeMillis()
)
