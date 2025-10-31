package com.dev.timeflow.View.Screens.calenderScreen

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.kizitonwose.calendar.core.WeekDay
import java.time.LocalDate
import java.time.format.DateTimeFormatter


@Composable
fun WeekCalender(
    modifier: Modifier = Modifier,
    selectedDate: LocalDate,
    onClick: (LocalDate) -> Unit,
    weekDate : WeekDay
)
{
    val date = LocalDate.now()
    val today = date.dayOfMonth
    val isToday = today == weekDate.date.dayOfMonth
    val dayPosition = date.dayOfMonth == weekDate.date.dayOfMonth
    val isu = selectedDate == weekDate.date

    val color =  if (isu) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
    val scale by animateFloatAsState(
        targetValue = if (isu) 1f else 0.9f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        )
    )
    Box(
        modifier = Modifier
            .graphicsLayer{
            scaleY = scale
            scaleX =scale
        }
            .padding(
                horizontal = 2.dp
            )
            .clip(RoundedCornerShape(8.dp))
            .background(
                if (isu) {
                    MaterialTheme.colorScheme.primary
                } else {
                    Color.Transparent
                }
            )
            .clickable(
                onClick = {
                    onClick.invoke(
                        weekDate.date
                    )
                }
            ),
        contentAlignment = Alignment.Center
    ) {

        Column(
            modifier = modifier.

            fillMaxWidth().padding(
                vertical = 8.dp
            ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text( style = MaterialTheme.typography.titleSmall,
                color = color,
                text = weekDate.date.format(DateTimeFormatter.ofPattern("MMM"))
            )
            Text(
                style = MaterialTheme.typography.titleMedium,
                fontWeight = if (isu) FontWeight.ExtraBold else FontWeight.Medium,
                color = color,
                text = weekDate.date.dayOfMonth.toString()
            )
            Text(
                style = MaterialTheme.typography.titleSmall,
                color = color.copy(
                    alpha = 0.8f
                ),
                text = weekDate.date.format(DateTimeFormatter.ofPattern("EEE"))
            )

        }

    }

}