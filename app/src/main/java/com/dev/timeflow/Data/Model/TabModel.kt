package com.dev.timeflow.Data.Model

import androidx.compose.ui.graphics.vector.ImageVector

data class TabModel(
    val title : String,
    val unSelectedIcon : ImageVector,
    val selectedIcon : ImageVector = unSelectedIcon
)