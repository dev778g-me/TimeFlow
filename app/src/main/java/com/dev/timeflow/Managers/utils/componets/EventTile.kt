package com.dev.timeflow.Managers.utils.componets

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.dev.timeflow.Managers.utils.toLocalDate
import com.dev.timeflow.Managers.utils.toMyFormat

@Composable
fun EventTile(
    modifier: Modifier = Modifier,
    eventName : String,
    eventDescription : String,
    eventDate : Long
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
                onClick = {}
            ),
        headlineContent = {
            Text(
                text = eventName,
                color = MaterialTheme.colorScheme.primary
            )
        },
        supportingContent = {
            Text(
                text = eventDate.toLocalDate().toMyFormat()
            )
        }
    )
}