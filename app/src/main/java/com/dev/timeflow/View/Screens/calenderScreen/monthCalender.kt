package com.dev.timeflow.View.Screens.calenderScreen

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.EaseInElastic
import androidx.compose.animation.core.EaseInOutCirc
import androidx.compose.animation.core.EaseInOutSine
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.DayPosition
import java.time.LocalDate

@Composable
fun MonthCalender(
    day: CalendarDay,
    modifier: Modifier = Modifier,
    hapticFeedback: HapticFeedback,
    selectedDate : LocalDate,
    onClick : (LocalDate) -> Unit
) {
    val date = LocalDate.now()
    val todayDayOfMonth = date.dayOfMonth
    val dayPosition = day.position == DayPosition.MonthDate
    val isSelected = selectedDate == day.date

    val boxSelectedColor by animateColorAsState(

        targetValue = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent
    )

    val boxTextColor by animateColorAsState(

        targetValue = when {
            isSelected -> MaterialTheme.colorScheme.onPrimary
            dayPosition -> MaterialTheme.colorScheme.onSurface
            else -> MaterialTheme.colorScheme.onSurface.copy(
                alpha = 0.2f
            )
        }
    )
    Column {
        Box(
            modifier = modifier
                .aspectRatio(1f)
                .padding(4.dp)
                .clip(CircleShape)
                .background(
                    boxSelectedColor
                )
                .clickable(
                    onClick = {
                        onClick(
                            day.date
                        )
                        hapticFeedback.performHapticFeedback(
                            hapticFeedbackType = HapticFeedbackType.Confirm
                        )
                    }
                ),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    modifier = modifier.padding(0.dp),
                    text = day.date.dayOfMonth.toString(),
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                    color = boxTextColor
                )
            }
        }
    }
}


@Composable
fun MonthHeader(
    modifier: Modifier = Modifier,
    weekName : List<String>,
    monthName : String
    ) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            modifier = modifier.padding(
                bottom = 8.dp,
                start = 12.dp

            ),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.primary,
            text = monthName.toLowerCase().replaceFirstChar {
                it.toUpperCase()
            },
            fontWeight = FontWeight.Bold
        )
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(
                    bottom = 8.dp
                )
            //horizontalArrangement = Arrangement.SpaceAround
        ) {
            weekName.forEach {
                Text(
                    modifier = modifier.weight(1f),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                    fontSize = 15.sp,
                    text = it
                )
            }
        }
    }
}