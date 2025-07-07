package com.dev.timeflow.Presentation.Widget

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

@Composable
fun RoundedCheckBox(
    modifier: Modifier = Modifier,
    isChecked : Boolean ,
    onCheckedChange : (Boolean) -> Unit
) {

    val color = if (isChecked) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant
    val icon = if (isChecked) Icons.Rounded.Check else null
    val iconColor = if (isChecked) MaterialTheme.colorScheme.onPrimary else null
    Box(
        modifier = modifier
            .size(24.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(color)
            .clickable(
                onClick = {
                    onCheckedChange(
                        !isChecked
                    )
                }
            )
        ,
        contentAlignment = Alignment.Center
    ) {
        AnimatedVisibility(
            visible = isChecked
        ) {
            icon?.let {
                Icon(
                    modifier = modifier.size(16.dp),
                    tint = iconColor!!,
                    imageVector = it,
                    contentDescription = null
                )
            }
        }
    }
}