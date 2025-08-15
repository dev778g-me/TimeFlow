package com.dev.timeflow.View.Screens.calenderScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
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
    val isToday = todayDayOfMonth == day.date.dayOfMonth
    val dayPosition = day.position == DayPosition.MonthDate
    val isu = selectedDate == day.date

    Column {
        Box(
            modifier = modifier
                .aspectRatio(1f)
                .clip(CircleShape)
                .background(
                    if (isu) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        Color.Transparent
                    }
                )
                .clickable(
                    onClick = {
                        onClick(
                            day.date
                        )
                        println(day)
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
                    text = day.date.dayOfMonth.toString(),
                    fontWeight = if (isu) FontWeight.ExtraBold else FontWeight.Normal,
                    color = when{
                        isu -> MaterialTheme.colorScheme.onPrimary
                        dayPosition -> MaterialTheme.colorScheme.onSurface
                        else -> MaterialTheme.colorScheme.onSurface.copy(
                            alpha = 0.2f
                        )
                    }
                )
            }
        }
    }
}
