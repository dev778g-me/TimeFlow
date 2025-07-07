package com.dev.timeflow.Data.Model

import android.accessibilityservice.GestureDescription
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Tasks(
    @PrimaryKey(autoGenerate = true)
    val id : Long,
    val name : String,
    val description: String?,
    val importance : String,
    val isCompleted : Boolean = false,
    val createdAt : Long,
)