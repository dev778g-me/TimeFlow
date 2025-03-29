package com.dev.timeflow.Presentation.Widget.YearProgress

import android.content.Context
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
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
    val yearPercentage = (dayOfTheYear.toFloat() / totalDayOfTheYear.toFloat()) * 100
    val formattedYearPercentage = decimalFormat.format(yearPercentage).toString() + "%"

    Column {
        Box (
            modifier = GlanceModifier.background(MaterialTheme.colorScheme.primaryContainer).fillMaxSize()
        ){
            Column(
                modifier = GlanceModifier.padding(horizontal = 10.dp, vertical = 12.dp)
            ) {
                Text(text = "YEAR-${year.toString()}", style = TextStyle(

                    fontWeight = FontWeight.Medium,
                    fontStyle = FontStyle.Normal,
                    fontSize = 13.sp
                ))
                 Text(
                     modifier = GlanceModifier.padding(bottom = 20.dp),
                     text = "Progress-${formattedYearPercentage}",
                     style = TextStyle(
                         fontSize = 12.sp
                     )
                 )
             //   Spacer(modifier = GlanceModifier.height(60.dp))

                LinearProgressIndicator(
                    progress = yearPercentage / 100,
                    modifier = GlanceModifier.fillMaxWidth().height(10.dp),

                )
            }
        }
    }
}
