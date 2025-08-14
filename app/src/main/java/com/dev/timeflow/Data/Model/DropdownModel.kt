package com.dev.timeflow.Data.Model

import androidx.compose.ui.graphics.vector.ImageVector

data class DropdownModel(
    val title : String,
    val icon : ImageVector,
    val onClick : () -> Unit
)