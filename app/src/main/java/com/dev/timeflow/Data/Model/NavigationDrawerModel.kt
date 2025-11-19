package com.dev.timeflow.Data.Model

import androidx.compose.ui.graphics.vector.ImageVector

data class NavigationDrawerModel(
    val name : String,
    val unSelectedIcon : ImageVector,
    val selectedIcon : ImageVector,
    val onClick : () -> Unit
)