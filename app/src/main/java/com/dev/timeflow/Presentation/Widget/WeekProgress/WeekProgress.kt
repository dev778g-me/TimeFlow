package com.dev.timeflow.Presentation.Widget.WeekProgress

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.icu.text.DecimalFormat
import android.icu.util.Calendar
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.LinearProgressIndicator
import androidx.glance.appwidget.cornerRadius
import androidx.glance.appwidget.provideContent
import androidx.glance.appwidget.updateAll
import androidx.glance.background
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.text.FontStyle
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import com.dev.timeflow.Presentation.Widget.Constants

import java.text.SimpleDateFormat

class WeekProgress : GlanceAppWidget(){

    companion object{
       suspend fun updateWidget(context: Context){
            WeekProgress().updateAll(context = context)
        }
    }


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

    val calendar = Calendar.getInstance()
    val decimalFormat = DecimalFormat("#.##")
    val dateFormat = SimpleDateFormat("E")
    val currentDate = calendar.get(Calendar.DAY_OF_WEEK)
    val dayName = dateFormat.format(calendar.time)
    val currentWeek = calendar.get(Calendar.WEEK_OF_MONTH)

    val weekProgressPercentage =( currentDate.toFloat() / 7.0f) * 100
    val formattedWeekPercentage = decimalFormat.format(weekProgressPercentage).toString() + "%"


    Box (
        modifier = GlanceModifier
            .background(GlanceTheme.colors.widgetBackground)
            .fillMaxSize()
            .cornerRadius(16.dp)
    ){
        Column(
            modifier = GlanceModifier.padding(horizontal = 10.dp, vertical = 12.dp)
        ) {
            Text(text = "Week-${currentWeek}($dayName)", style = TextStyle(

                fontWeight = FontWeight.Medium,
                fontStyle = FontStyle.Normal,
                fontSize = 13.sp,
                color = GlanceTheme.colors.onPrimaryContainer

            ))
            Text(
                modifier = GlanceModifier.padding(bottom = 20.dp),
                text = "Progress-${formattedWeekPercentage}",
                style = TextStyle(
                    fontSize = 12.sp,
                    color = GlanceTheme.colors.onPrimaryContainer
                ),


            )

            LinearProgressIndicator(
                progress = weekProgressPercentage / 100,
                modifier = GlanceModifier.fillMaxWidth().height(10.dp),
                color = GlanceTheme.colors.primary,
                backgroundColor = GlanceTheme.colors.primaryContainer
                )
        }
    }
}