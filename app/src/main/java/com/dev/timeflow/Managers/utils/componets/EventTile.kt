package com.dev.timeflow.Managers.utils.componets

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Event
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.composables.icons.lucide.CalendarFold
import com.composables.icons.lucide.CalendarSearch
import com.composables.icons.lucide.Lucide
import com.dev.timeflow.Managers.utils.toLocalDate
import com.dev.timeflow.Managers.utils.toMyFormat


@Composable
fun EventTile(
    modifier: Modifier = Modifier,
    eventName : String,
    onClick : () -> Unit
) {
    ListItem(

        modifier = modifier
            .padding(
                vertical = 2.dp
            )
            .clip(
                RoundedCornerShape(16.dp)
            )
            .clickable(
                onClick = {
                    onClick()
                }
            ),
        headlineContent = {
            Text(
                text = eventName,
                color = MaterialTheme.colorScheme.primary
            )
        },
//        supportingContent = {
//            Text(
//                text = eventDate.toLocalDate().toMyFormat()
//            )
//        },
        leadingContent = {
            Icon(
                tint = MaterialTheme.colorScheme.primary,
                imageVector = Lucide.CalendarFold,
                contentDescription = null
            )
        }
    )
}