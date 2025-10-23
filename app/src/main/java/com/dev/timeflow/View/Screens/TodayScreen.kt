package com.dev.timeflow.View.Screens

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularWavyProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.LinearWavyProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dev.timeflow.Viewmodel.EventViewModel
import kotlinx.coroutines.delay
import java.time.LocalDate
import java.time.ZoneId

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun TodayScreen(modifier: Modifier = Modifier) {

    val today = LocalDate.now()
    val currentMonth = today.month.name
    val currentDate = today.dayOfMonth

    val primary = MaterialTheme.colorScheme.primary
    // viewmodel instance
    val taskViewModel  : EventViewModel = hiltViewModel()
    //variable to hold the task for today
    val taskForToday by taskViewModel.taskForToday.collectAsState()
    // variable to hold the event for today
    val eventForToday by taskViewModel.eventForToday.collectAsState()
    var animate by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        taskViewModel.getTasksForToday(
            date = today.atStartOfDay(ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli()
        )
        taskViewModel.getEventForToday(
            date = today.atStartOfDay(ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli()
        )
        delay(200)
        animate = true
    }

    val taskNumber by animateIntAsState(
        targetValue = if (animate) taskForToday.size else 0,
        animationSpec = tween(
            durationMillis = 700,
            easing = LinearEasing
        )
    )

    val scale by animateFloatAsState(
        targetValue = if (animate) 1f else 0.8f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        )
    )



    Scaffold (

    ){ paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(
                    horizontal = 16.dp
                )
        ) {
            Text(
                text = "today is",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.SemiBold
                )
            )

            Text(
                text = "${currentMonth.lowercase().replaceFirstChar { it.uppercase()}} ${currentDate},",
                style = MaterialTheme.typography.displayMedium.copy(
                    fontWeight = FontWeight.W900,
                    color = primary
                )
            )

            Text(
                text = today.dayOfWeek.name.lowercase().replaceFirstChar {
                    it.uppercase()
                },
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.W100,

                )
            )
            Spacer(
                modifier = modifier.height(8.dp)
            )
             Column(

             ) {
                 ProgressCard(
                     percent = 56f,
                     type = "day"
                 )
                 ProgressCard(
                     percent = 4f,
                     type = "month"
                 )
                 ProgressCard(
                     percent = 100f,
                     type = "year"
                 )
             }

            Text(
                text = buildAnnotatedString {

                },

            )

            Text(
                text = buildAnnotatedString {
                    append("you have")
                    withStyle(style = SpanStyle(fontWeight = FontWeight.SemiBold)){
                        append(" $taskNumber tasks")
                    }
                    append(" today")
                },
                style = MaterialTheme.typography.headlineSmall.copy(

                )
            )
            Text(
                text = buildAnnotatedString {
                    // for high priority
                    withStyle(
                        style = SpanStyle(
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold
                        )

                    ){
                        append("2 high priority\n")
                    }

                    withStyle(
                        style = SpanStyle(
                            color = MaterialTheme.colorScheme.primary.copy(
                                alpha = 0.8f
                            ),
                            fontWeight = FontWeight.SemiBold
                        )
                    ){
                        append("1 medium priority\n")
                    }
                    withStyle(
                        style = SpanStyle(
                            color = MaterialTheme.colorScheme.primary.copy(
                                alpha = 0.6f
                            ),
                            fontWeight = FontWeight.Normal
                        )
                    ){
                        append("1 low priority &")
                    }
                },
                style = MaterialTheme.typography.titleLarge.copy(
                    lineHeight = 34.sp
                )
            )
            Text(
                text = buildAnnotatedString {

                    withStyle(style = SpanStyle(fontWeight = FontWeight.SemiBold)){
                        append("$taskNumber events")
                    }
                    append(" planned")
                },
                style = MaterialTheme.typography.headlineSmall.copy(

                )
            )


        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ProgressCard(
    modifier: Modifier = Modifier,
    percent: Float,
    type: String
) {
    var animate by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        delay(300)
        animate = true
    }
     val progress by animateFloatAsState(
          targetValue = if (animate) percent else 0f,
         animationSpec = tween(
             durationMillis = 1200,
             easing = LinearOutSlowInEasing
         )
     )

    Column(
        modifier = modifier.padding(vertical = 4.dp)
    ) {
        Text(
            text = "you're ${percent.toInt()}% through the $type",
            style = MaterialTheme.typography.titleSmall.copy(
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            ),
            modifier = Modifier.padding(vertical = 4.dp)
        )

        LinearProgressIndicator(
            color = if (percent >= 90f) MaterialTheme.colorScheme.error else ProgressIndicatorDefaults.linearColor,
            progress = { progress / 100f },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 2.dp)
        )
    }
}


