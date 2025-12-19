package com.dev.timeflow.View.Widget.YearProgress

import android.content.Context
import androidx.compose.animation.AnimatedContent
import androidx.compose.runtime.Composable
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
import java.util.Calendar

class YearProgress : GlanceAppWidget() {

    override suspend fun provideGlance(
        context: Context,
        id: GlanceId
    ) {
        provideContent {
            YearProgressWidget()
        }
    }
}

@Composable
fun YearProgressWidget() {
    val calendar = Calendar.getInstance()
    val dayOfTheYear = calendar.get(Calendar.DAY_OF_YEAR)
    val totalDayOfTheYear = calendar.getActualMaximum(Calendar.DAY_OF_YEAR)
    val year = calendar.get(Calendar.YEAR)
    val decimalFormat = DecimalFormat("#.##")
    val remainingDay = totalDayOfTheYear - dayOfTheYear
    val yearPercentage = (dayOfTheYear.toFloat() / totalDayOfTheYear.toFloat()) * 100
    val formattedYearPercentage = decimalFormat.format(yearPercentage).toString() + "%"

    Column {
        Box (
            modifier = GlanceModifier
                .background(GlanceTheme.colors.widgetBackground)
                .fillMaxSize()
                .cornerRadius(16.dp)
        ){

            Column(
                modifier = GlanceModifier
                    .fillMaxSize()
                    .padding(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = year.toString(), style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontStyle = FontStyle.Normal,
                        fontSize = 16.sp,
                        color = GlanceTheme.colors.primary
                    ))

                Text(
                    modifier = GlanceModifier.padding(vertical = 2.dp),
                    text = "Progress : $formattedYearPercentage",
                    style = TextStyle(
                        fontWeight = FontWeight.Medium,
                        fontSize = 12.sp,
                        color = GlanceTheme.colors.primary
                    )
                )
                Text(
                    modifier = GlanceModifier.padding(
                        vertical = 0.dp
                    ),
                    text = "Day $dayOfTheYear • $remainingDay left",
                    style = TextStyle(
                        fontWeight = FontWeight.Medium,
                        fontSize = 10.sp,
                        color = GlanceTheme.colors.primary
                    )
                )

                Spacer(
                    modifier = GlanceModifier.defaultWeight()
                )
                Box(
                    modifier = GlanceModifier.padding(
                        bottom = 0.dp
                    )
                ) {
                    LinearProgressIndicator(
                        progress = yearPercentage / 100,
                        modifier = GlanceModifier.fillMaxWidth().height(12.dp).cornerRadius(12.dp),
                        color = GlanceTheme.colors.primary,
                        backgroundColor = GlanceTheme.colors.primaryContainer
                    )
                }


            }
        }
    }
}
