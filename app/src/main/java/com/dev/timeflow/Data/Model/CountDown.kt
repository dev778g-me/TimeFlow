package com.dev.timeflow.Data.Model

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "countdown_table")
data class CountDown(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val name: String,
    val startTime: Long,
    val endTime: Long,
)