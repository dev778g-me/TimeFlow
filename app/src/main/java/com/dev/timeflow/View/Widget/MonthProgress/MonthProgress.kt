package com.dev.timeflow.View.Widget.MonthProgress

import android.content.Context
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.LinearProgressIndicator
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

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MonthProgressWidget(modifier: Modifier = Modifier) {
    val calendar = Calendar.getInstance()
    val decimalFormat = DecimalFormat("#.##")

    val monthFormat = SimpleDateFormat("MMMM")
    val monthName = monthFormat.format(calendar.time)
    //current day of month
    val currentDay = calendar.get(Calendar.DAY_OF_MONTH)
    val totalDaysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
     val remainingDays = totalDaysInMonth - currentDay
    val monthlyPercentage = currentDay.toFloat() / totalDaysInMonth.toFloat() * 100
    val formatedMonthlyPercentage = decimalFormat.format(monthlyPercentage).toString() + "%"

    Box (
        modifier = GlanceModifier
            .background(GlanceTheme.colors.widgetBackground)
            .fillMaxSize()
            .cornerRadius(16.dp),

    ){
        Column(
            modifier = GlanceModifier
                .fillMaxSize()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "$monthName",
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontStyle = FontStyle.Normal,
                    fontSize = 16.sp,
                    color = GlanceTheme.colors.onPrimaryContainer
                )
            )

            Text(
                modifier = GlanceModifier.padding(
                    vertical = 2.dp
                ),
                text = "Progress : $formatedMonthlyPercentage",
                style = TextStyle(
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = GlanceTheme.colors.onPrimaryContainer
                )
            )
            Text(
                modifier = GlanceModifier.padding(
                    vertical = 0.dp
                ),
                text = "Day $currentDay • $remainingDays left",
                style = TextStyle(
                    fontWeight = FontWeight.Medium,
                    fontSize = 10.sp,
                    color = GlanceTheme.colors.onPrimaryContainer
                )
            )
            Spacer(modifier = GlanceModifier.defaultWeight())

            Box(
                modifier = GlanceModifier.padding(
                    bottom = 0.dp
                )
            ) {
                LinearProgressIndicator(
                    progress = monthlyPercentage / 100,
                    modifier = GlanceModifier.
                    fillMaxWidth()
                        .height(10.dp),
                    color = GlanceTheme.colors.primary,
                    backgroundColor = GlanceTheme.colors.primaryContainer
                )
            }
        }
    }

}