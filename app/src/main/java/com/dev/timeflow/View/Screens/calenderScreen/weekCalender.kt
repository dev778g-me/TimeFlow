package com.dev.timeflow.View.Screens.calenderScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import com.kizitonwose.calendar.core.WeekDay
import java.time.LocalDate


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
    Box(
        modifier = Modifier
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
                    onClick.invoke(
                        weekDate.date
                    )
                }
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                fontWeight = if (isToday) FontWeight.ExtraBold else FontWeight.Normal,
                color = if (isu) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface,
                text = weekDate.date.dayOfMonth.toString()
            )

        }

    }

}