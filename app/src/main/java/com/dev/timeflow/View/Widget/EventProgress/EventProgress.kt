package com.dev.timeflow.View.Widget.EventProgress

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.LinearProgressIndicator
import androidx.glance.appwidget.SizeMode
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
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.preview.ExperimentalGlancePreviewApi
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextAlign
import androidx.glance.text.TextStyle
import com.dev.timeflow.Data.Repo.WidgetRepo
import java.text.DecimalFormat
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

class EventProgress : GlanceAppWidget() {

    override val sizeMode: SizeMode = SizeMode.Exact

    override suspend fun provideGlance(context: Context, id: GlanceId) {

        val eventId = EventWidgetState.getEventId(context, id)

        val event = eventId?.let { id ->
            WidgetRepo.get(context).getEvent(id = id)
        }

        provideContent {
            when {
                eventId == null -> EventProgressPlaceholder(
                    text = "Open an event in TimeFlow and tap \"Add to Home Screen\""
                )
                event == null -> EventProgressPlaceholder(
                    text = "This event no longer exists"
                )
                else -> EventProgressWidget(
                    name = event.name,
                    startDate = event.eventStartTime,
                    endDate = event.eventEndTime
                )
            }
        }
    }
}

@OptIn(ExperimentalGlancePreviewApi::class)
@androidx.glance.preview.Preview(widthDp = 300, heightDp = 200)
@Composable
fun EventProgressWidget(
    modifier: GlanceModifier = GlanceModifier,
    name: String = "Event",
    startDate: Long = System.currentTimeMillis(),
    endDate: Long = System.currentTimeMillis() + 3L * 24 * 60 * 60 * 1000,
    now: Long = System.currentTimeMillis()
) {
    val zone = ZoneId.systemDefault()
    val decimalFormat = DecimalFormat("#.##")
    val total = (endDate - startDate).coerceAtLeast(1L)
    val elapsed = (now - startDate).coerceIn(0L, total)
    val progress = elapsed.toFloat() / total
    val percent = decimalFormat.format(progress * 100) + "%"
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
            modifier = GlanceModifier
                .fillMaxSize()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = GlanceModifier.fillMaxWidth()
            ) {
                Text(
                    text = name.ifBlank { "Event" },
                    maxLines = 1,
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = GlanceTheme.colors.primary
                    )
                )
                Spacer(modifier = GlanceModifier.defaultWeight())
                Text(
                    text = percent,
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = GlanceTheme.colors.primary
                    )
                )
            }

            Spacer(modifier = GlanceModifier.defaultWeight())

            Row(
                modifier = GlanceModifier.fillMaxWidth()
            ) {
                Text(
                    modifier = GlanceModifier.padding(bottom = 4.dp),
                    text = targetDate,
                    style = TextStyle(
                        fontWeight = FontWeight.Medium,
                        fontSize = 12.sp,
                        color = GlanceTheme.colors.secondary
                    )
                )
                Spacer(modifier = GlanceModifier.defaultWeight())
                Text(
                    modifier = GlanceModifier.padding(bottom = 4.dp),
                    text = if (daysLeft == 1L) "1 day left" else "$daysLeft days left",
                    style = TextStyle(
                        fontWeight = FontWeight.Medium,
                        fontSize = 12.sp,
                        color = GlanceTheme.colors.secondary
                    )
                )
            }

            Spacer(modifier = GlanceModifier.defaultWeight())

            LinearProgressIndicator(
                progress = progress,
                modifier = GlanceModifier
                    .fillMaxWidth()
                    .height(12.dp)
                    .cornerRadius(12.dp),
                color = GlanceTheme.colors.primary,
                backgroundColor = GlanceTheme.colors.primaryContainer
            )
        }
    }
}

@OptIn(ExperimentalGlancePreviewApi::class)
@androidx.glance.preview.Preview(widthDp = 300, heightDp = 200)
@Composable
fun EventProgressPlaceholder(
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
