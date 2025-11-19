package com.dev.timeflow.View.utils.componets

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularWavyProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.LinearWavyProgressIndicator
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.glance.layout.Row
import com.composables.icons.lucide.CalendarFold
import com.composables.icons.lucide.Lucide
import com.dev.timeflow.Data.Model.Events
import com.dev.timeflow.View.utils.toLocalDate
import com.dev.timeflow.View.utils.toMyFormat
import java.time.format.DateTimeFormatter


@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun EventTile(
    eventFromDay : Long,
    eventEndDay : Long,
    modifier: Modifier = Modifier,
    eventName : String,
    onClick : () -> Unit
) {

    val currentTime = System.currentTimeMillis()

    val elapsed = currentTime - eventFromDay  // time passed
    val total = eventEndDay - eventFromDay   // total event duration



    val percent = ((elapsed.toFloat() / total.toFloat()) * 100f)
        .coerceIn(0f, 100f)
    ListItem(
        tonalElevation = 1.dp,
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
        overlineContent = {
            androidx.compose.foundation.layout.Row (
                modifier = modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Start : ${
                        eventFromDay.toLocalDate().toMyFormat()
                    }"
                )
                Text(
                    text = "End : ${
                        eventEndDay.toLocalDate().toMyFormat()
                    }"
                )
            }
        },
        headlineContent = {
            Text(
                text = eventName,
                style = MaterialTheme.typography.titleMedium,
                modifier = modifier.padding(
                    bottom = 8.dp
                )
            )
        },

        supportingContent = {
          androidx.compose.foundation.layout.Row(
              modifier = modifier.fillMaxWidth(),
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.SpaceBetween
          ) {
              LinearProgressIndicator(
                  progress = { percent / 100f }
              )
              Text(
                  text = "${"%.2f".format(percent)}%",
                  style = MaterialTheme.typography.titleSmall.copy(
                      fontWeight = FontWeight.Bold
                  ),
                  color = MaterialTheme.colorScheme.primary
              )
          }
        },

    )
}