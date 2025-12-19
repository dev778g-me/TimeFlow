package com.dev.timeflow.View.Widget.WeekProgress

import android.content.Context
import android.icu.text.DecimalFormat
import android.icu.util.Calendar
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.LocalSize
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.LinearProgressIndicator
import androidx.glance.appwidget.SizeMode
import androidx.glance.appwidget.cornerRadius
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.text.FontStyle
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle

import java.text.SimpleDateFormat

class WeekProgress : GlanceAppWidget(){



    override val sizeMode = SizeMode.Exact
    override suspend fun provideGlance(
        context: Context,
        id: GlanceId
    ) {
        provideContent {
            WeekProgressWidget()
        }

    }
}

@Composable
fun WeekProgressWidget(modifier: Modifier = Modifier) {
    val size = LocalSize.current
    val calendar = Calendar.getInstance()
    val decimalFormat = DecimalFormat("#.##")
    val dateFormat = SimpleDateFormat("E")
    val currentDate = calendar.get(Calendar.DAY_OF_WEEK)
    val dayName = dateFormat.format(calendar.time)
    val currentWeek = calendar.get(Calendar.WEEK_OF_MONTH)
    val remainingDay = 7 - currentDate
    val weekProgressPercentage = (currentDate.toFloat() / 7.0f) * 100
    val formattedWeekPercentage = decimalFormat.format(weekProgressPercentage).toString() + "%"
    val SMALL_SQUARE = null
    val isTall = size.height > 100.dp
    Log.d("SIZE","${size.height}")
    Box (
        modifier = GlanceModifier
            .background(GlanceTheme.colors.widgetBackground)
            .fillMaxSize()
            .cornerRadius(16.dp)
    ){
        Box {
            LinearProgressIndicator(
                progress = weekProgressPercentage / 100,
                modifier = GlanceModifier.fillMaxSize(),
                color = GlanceTheme.colors.primary,
                backgroundColor = GlanceTheme.colors.primaryContainer
            )
        }
        Column(
            modifier = GlanceModifier
                .fillMaxSize()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Week-${currentWeek}($dayName)", style = TextStyle(

                fontWeight = FontWeight.Bold,
                fontStyle = FontStyle.Normal,
                fontSize = 16.sp,
                color = GlanceTheme.colors.surface

            ))
            Text(
                modifier = GlanceModifier.padding(
                    vertical = 2.dp
                ),
                text = "Progress-${formattedWeekPercentage}",
                style = TextStyle(
                    fontWeight = FontWeight.Medium,
                    fontSize = 12.sp,
                    color = GlanceTheme.colors.surface
                ),


            )
            Text(
                modifier = GlanceModifier.padding(
                    vertical = 0.dp
                ),
                text = "Day $currentDate • $remainingDay left",
                style = TextStyle(
                    fontWeight = FontWeight.Medium,
                    fontSize = 10.sp,
                    color = GlanceTheme.colors.surface
                )
            )



        }
    }
}