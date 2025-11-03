package com.dev.timeflow.Data.Model

import android.accessibilityservice.GestureDescription
import android.app.Notification
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@kotlinx.parcelize.Parcelize
@Entity
data class Tasks(
    @PrimaryKey(autoGenerate = true)
    val id : Long,
    val name : String,
    val description: String?,
    val importance : String,
    val notification: Boolean = false,
    val isCompleted : Boolean = false,
    val taskTime : Long?,
    val createdAt : Long
): Parcelable