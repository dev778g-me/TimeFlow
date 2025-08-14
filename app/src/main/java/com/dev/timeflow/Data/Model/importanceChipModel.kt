package com.dev.timeflow.Data.Model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Circle
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

data class ImportanceChipModel(
    val label : String,
    val color: Color,
    val type : Int = 0,
    val icon : ImageVector = Icons.Filled.Circle
)