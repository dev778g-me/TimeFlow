package com.dev.timeflow.Presentation.Widget.MonthProgress

import android.content.Context
import android.util.Log
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.LinearProgressIndicator
import androidx.glance.appwidget.provideContent
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
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Calendar

class MonthProgress : GlanceAppWidget() {

    override suspend fun provideGlance(
        context: Context,
        id: GlanceId
    ) {

        Log.d("MonthProgress", "provideGlance called for id: $id")

        provideContent {
            CompositionLocalProvider(androidx.compose.ui.platform.LocalContext provides context) {
                MonthProgressWidget()
            }
        }
    }
}


@Composable
fun MonthProgressWidget(modifier: Modifier = Modifier) {
    val calendar = Calendar.getInstance()
    val decimalFormat = DecimalFormat("#.##")

    val monthFormat = SimpleDateFormat("MMMM")
    val monthName = monthFormat.format(calendar.time)
    //current day of month
    val currentDay = calendar.get(Calendar.DAY_OF_MONTH)
     val totalDaysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)

    val monthlyPercentage = currentDay.toFloat() / totalDaysInMonth.toFloat() * 100
    val formatedMonthlyPecentage = decimalFormat.format(monthlyPercentage).toString() + "%"

    Box (
        modifier = GlanceModifier.background(MaterialTheme.colorScheme.primaryContainer).fillMaxSize()
    ){
        Column(
            modifier = GlanceModifier.padding(horizontal = 10.dp, vertical = 12.dp)
        ) {
            Text(text = "MONTH-${monthName}", style = TextStyle(

                fontWeight = FontWeight.Medium,
                fontStyle = FontStyle.Normal,
                fontSize = 13.sp
            ))
            Text(
                modifier = GlanceModifier.padding(bottom = 20.dp),
                text = "Progress-${formatedMonthlyPecentage}",
                style = TextStyle(
                    fontSize = 12.sp
                )
            )
            //   Spacer(modifier = GlanceModifier.height(60.dp))

            LinearProgressIndicator(
                progress = monthlyPercentage / 100,
                modifier = GlanceModifier.fillMaxWidth().height(10.dp),

                )
        }
    }

}