package com.dev.timeflow.View.Widget.countDown

import android.content.Context
import androidx.compose.runtime.Composable
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
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.padding
import androidx.glance.preview.ExperimentalGlancePreviewApi
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextAlign
import androidx.glance.text.TextStyle
import com.dev.timeflow.Data.Repo.WidgetRepo
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

class CountDownWidget : GlanceAppWidget() {

    override suspend fun provideGlance(context: Context, id: GlanceId) {

        val countdownId = CountDownWidgetState.getCountdownId(context, id)


        val countdown = countdownId?.let { id ->
            WidgetRepo.get(context).getCountDown(id = id)
        }

        provideContent {
            when {
                countdownId == null -> CountDownPlaceholder(
                    text = "Create a countdown, then pin it to show it here"
                )
                countdown == null -> CountDownPlaceholder(
                    text = "This countdown no longer exists"
                )
                else -> CountDownContent(
                    name = countdown.name,
                    startDate = countdown.startTime,
                    endDate = countdown.endTime
                )
            }
        }
    }
}

@OptIn(ExperimentalGlancePreviewApi::class)
@androidx.glance.preview.Preview(widthDp = 300, heightDp = 200)
@Composable
fun CountDownContent(
    modifier: GlanceModifier = GlanceModifier,
    name: String,
    startDate: Long,
    endDate: Long,
    now: Long = System.currentTimeMillis()
) {
    val zone = ZoneId.systemDefault()
    val total = (endDate - startDate).coerceAtLeast(1L)
    val elapsed = (now - startDate).coerceIn(0L, total)
    val progress = elapsed.toFloat() / total
    val daysLeft = ChronoUnit.DAYS.between(
        Instant.ofEpochMilli(now).atZone(zone).toLocalDate(),
        Instant.ofEpochMilli(endDate).atZone(zone).toLocalDate()
    ).coerceAtLeast(0L)
    val targetDate = DateTimeFormatter.ofPattern("d MMM yyyy").format(
        Instant.ofEpochMilli(endDate).atZone(zone).toLocalDate()
    )

    Box(
        modifier = modifier
            .background(GlanceTheme.colors.widgetBackground)
            .fillMaxSize()
            .cornerRadius(16.dp)
    ) {
        Column(
            modifier = GlanceModifier.fillMaxSize().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = GlanceModifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = name.ifBlank { "Countdown" },
                    modifier = GlanceModifier.defaultWeight(),
                    maxLines = 1,
                    style = TextStyle(
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = GlanceTheme.colors.primary
                    )
                )
                Text(
                    text = daysLeft.toString(),
                    style = TextStyle(
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = GlanceTheme.colors.primary
                    )
                )
            }

            Spacer(modifier = GlanceModifier.defaultWeight())

            Row(
                modifier = GlanceModifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = targetDate,
                    modifier = GlanceModifier.defaultWeight(),
                    maxLines = 1,
                    style = TextStyle(
                        fontSize = 12.sp,
                        color = GlanceTheme.colors.secondary
                    )
                )
                Text(
                    text = "Days left",
                    style = TextStyle(
                        fontSize = 12.sp,
                        color = GlanceTheme.colors.secondary
                    )
                )
            }

            Spacer(modifier = GlanceModifier.defaultWeight())

            LinearProgressIndicator(
                progress = progress,
                modifier = GlanceModifier.fillMaxWidth(),
                color = GlanceTheme.colors.primary,
                backgroundColor = GlanceTheme.colors.primaryContainer
            )
        }
    }
}

@OptIn(ExperimentalGlancePreviewApi::class)
@androidx.glance.preview.Preview(widthDp = 300, heightDp = 200)
@Composable
fun CountDownPlaceholder(
    modifier: GlanceModifier = GlanceModifier,
    text: String
) {
    Box(
        modifier = modifier
            .background(GlanceTheme.colors.widgetBackground)
            .fillMaxSize()
            .cornerRadius(16.dp)
    ) {
        Column(
            modifier = GlanceModifier.fillMaxSize().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = text,
                maxLines = 3,
                style = TextStyle(
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center,
                    color = GlanceTheme.colors.onSurfaceVariant
                )
            )
        }
    }
}


class CountDownDateSelectActivity
