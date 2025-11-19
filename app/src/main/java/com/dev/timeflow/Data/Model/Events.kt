package com.dev.timeflow.Data.Model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Events")
data class Events(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val name: String,
    val description: String,
    val notification: Boolean = false,
    val eventNotificationTime: Long,
    val eventStartTime: Long,
    val eventEndTime: Long,
    val createdAt: Long
)
