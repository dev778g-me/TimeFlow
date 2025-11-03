package com.dev.timeflow.View.Screens

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.ButtonGroup
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FloatingToolbarDefaults
import androidx.compose.material3.FloatingToolbarExitDirection.Companion.Bottom
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.composables.icons.lucide.CalendarRange
import com.composables.icons.lucide.ListTodo
import com.composables.icons.lucide.Lucide
import com.dev.timeflow.Data.Model.TabModel
import com.dev.timeflow.Managers.utils.componets.EventTile
import com.dev.timeflow.Managers.utils.componets.TaskTile
import com.dev.timeflow.Viewmodel.TaskAndEventViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.Calendar

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun TodayScreen(modifier: Modifier = Modifier) {

    val today = LocalDate.now()
    val calendar = Calendar.getInstance()
    val currentMonth = today.month.name
    val currentDate = today.dayOfMonth


    // year logic
    val dayOfTheYear = calendar.get(Calendar.DAY_OF_YEAR)
    val maximumDayOfYear = calendar.getActualMaximum(Calendar.DAY_OF_YEAR)
    val yearPercentage =( dayOfTheYear.toFloat() / maximumDayOfYear.toFloat() ) * 100

    // month logic
    val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)
    val maximumDayInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
    val monthPercentage =( dayOfMonth.toFloat() / maximumDayInMonth.toFloat()) * 100

    //week logic
    val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
    val weekPercentage = (dayOfWeek.toFloat()/ 7f) * 100

    fun getDayProgressPercent(): Float {
        val now = LocalDateTime.now()

        val startOfDay = now.toLocalDate().atStartOfDay()
        val endOfDay = now.toLocalDate().atTime(23, 59, 59)

        val totalSeconds = Duration.between(startOfDay, endOfDay).seconds.toFloat()
        val passedSeconds = Duration.between(startOfDay, now).seconds.toFloat()

        return (passedSeconds / totalSeconds) * 100f
    }

    //day logic
    val dayPercentage = getDayProgressPercent()


    val primary = MaterialTheme.colorScheme.primary
    // viewmodel instance
    val taskViewModel  : TaskAndEventViewModel = hiltViewModel()
    //variable to hold the task for today
    val taskForToday by taskViewModel.taskForToday.collectAsState()
    // variable to hold the event for today
    val eventForToday by taskViewModel.eventForToday.collectAsState()
    var animate by remember { mutableStateOf(false) }
    var selectedTab by remember { mutableStateOf(0) }
    val scrollState = rememberScrollState()
    val pagerState = rememberPagerState(initialPage = 0, pageCount = {2})

    val scope = rememberCoroutineScope ()
    Log.d("SCROLLSTATE","${scrollState.value}")

    val tabs = listOf<TabModel>(
        TabModel(
            title = "Tasks",
            unSelectedIcon = Lucide.ListTodo
        ),
        TabModel(
            title = "Events",
            unSelectedIcon = Lucide.CalendarRange
        )
    )
    LaunchedEffect(taskForToday) {
        taskViewModel.getTasksForToday(
          start = today.atStartOfDay(ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli(),
            end = System.currentTimeMillis()
        )
        taskViewModel.getEventForToday(
            start = today.atStartOfDay(ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli(),
            end = System.currentTimeMillis()
        )
        delay(200)
        animate = true
    }



    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }
            .collect { page ->
                selectedTab = page
            }
    }

    val highPriorityTask by remember(taskForToday) {
        derivedStateOf {
            taskForToday.count {
                it.importance == "High"
            }
        }
    }

    val mediumPriorityTask by remember(taskForToday) {
        derivedStateOf {
            taskForToday.count {
                it.importance == "Medium"
            }
        }
    }

    val lowPriorityTask by remember(taskForToday) {
        derivedStateOf {
            taskForToday.count {
                it.importance == "Low"
            }
        }
    }
    val taskNumber by animateIntAsState(
        targetValue = if (animate) taskForToday.size else 0,
        animationSpec = tween(
            durationMillis = 700,
            easing = LinearEasing
        )
    )
    val eventNumber by animateIntAsState(
        targetValue = if (animate) eventForToday.size else 0,
        animationSpec = tween(
            durationMillis = 700,
            easing = LinearEasing
        )
    )

    Scaffold(
        modifier = modifier.nestedScroll(
            FloatingToolbarDefaults.exitAlwaysScrollBehavior(exitDirection =Bottom )
        ),
        contentWindowInsets = WindowInsets(0.dp)
    ) { paddingValues ->
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(
                    start = 16.dp, end = 16.dp,
                )

        ) {
            item {
                Text(
                    text = "today is",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.SemiBold
                    )
                )
            }

            item {
                Text(
                    text = "${currentMonth.lowercase().replaceFirstChar { it.uppercase()}} ${currentDate},",
                    style = MaterialTheme.typography.displayMedium.copy(
                        fontWeight = FontWeight.W900,
                        color = primary
                    )
                )
            }

           item {
               Text(
                   text = today.dayOfWeek.name.lowercase().replaceFirstChar {
                       it.uppercase()
                   },
                   style = MaterialTheme.typography.headlineMedium.copy(
                       fontWeight = FontWeight.W100,

                       )
               )
           }
           item {
               Spacer(
                   modifier = modifier.height(8.dp)
               )
           }
            item {
                Column() {
                    ProgressCard(
                        percent = dayPercentage,
                        type = "day"
                    )
                    ProgressCard(
                        percent = weekPercentage,
                        type = "week"
                    )
                    ProgressCard(
                        percent = monthPercentage,
                        type = "month"
                    )
                    ProgressCard(
                        percent = yearPercentage,
                        type = "year"
                    )
                }
            }


           item {
               Spacer(
                   modifier = modifier.height(8.dp)
               )
           }

            // texts for the task and events
           item {
               Text(
                   text = buildAnnotatedString {
                       append("you have")
                       withStyle(style = SpanStyle(fontWeight = FontWeight.SemiBold)) {
                           append(" $taskNumber tasks")
                       }
                       append(" today")
                   },
                   style = MaterialTheme.typography.headlineSmall.copy(

                   )
               )
           }
            item {
                Text(
                    text = buildAnnotatedString {
                        // for high priority
                        withStyle(
                            style = SpanStyle(
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Bold
                            )

                        ){
                            append("$highPriorityTask high priority\n")
                        }

                        withStyle(
                            style = SpanStyle(
                                color = MaterialTheme.colorScheme.primary.copy(
                                    alpha = 0.8f
                                ),
                                fontWeight = FontWeight.SemiBold
                            )
                        ){
                            append("$mediumPriorityTask medium priority\n")
                        }
                        withStyle(
                            style = SpanStyle(
                                color = MaterialTheme.colorScheme.primary.copy(
                                    alpha = 0.6f
                                ),
                                fontWeight = FontWeight.Normal
                            )
                        ){
                            append("$lowPriorityTask low priority &")
                        }
                    },
                    style = MaterialTheme.typography.titleLarge.copy(
                        lineHeight = 34.sp
                    )
                )
            }
           item {
               Text(
                   text = buildAnnotatedString {
                       withStyle(style = SpanStyle(fontWeight = FontWeight.SemiBold)){
                           append("$eventNumber events")
                       }
                       append(" planned")
                   },
                   style = MaterialTheme.typography.headlineSmall.copy(

                   )
               )
           }

            item {
                AnimatedVisibility(
                    visible = taskForToday.isNotEmpty() || eventForToday.isNotEmpty(),
                    enter = scaleIn(
                        animationSpec =  spring(
                            dampingRatio = Spring.DampingRatioLowBouncy,
                            stiffness = Spring.StiffnessMediumLow
                        )
                    ),
                    exit = scaleOut(
                        animationSpec =  spring(
                            dampingRatio = Spring.DampingRatioLowBouncy,
                            stiffness = Spring.StiffnessMediumLow
                        )
                    ),
                ) {
                    ButtonGroup(
                        modifier = modifier.fillMaxWidth(),
                        overflowIndicator = {}
                    ) {
                        tabs.forEachIndexed { index, model ->
                            val isSelected = index == selectedTab
                            toggleableItem(
                                weight = 1f,
                                checked = isSelected,
                                label = if (model.title =="Tasks") "${model.title} ${taskForToday.size}" else "${model.title} ${eventForToday.size}",
                                onCheckedChange = {
                                    selectedTab = index
                                    scope.launch {
                                        pagerState.animateScrollToPage(
                                            page = index
                                        )
                                    }
                                }
                            )

                        }
                    }
                }
            }

            item {
                HorizontalPager(
                    modifier = modifier.padding(
                        top = 8.dp
                    ),
                    state = pagerState,
                ) { page ->
                    when (page) {
                        0 -> AnimatedVisibility(
                            visible = taskForToday.isNotEmpty(),
                            enter = scaleIn(
                                animationSpec = spring(
                                    dampingRatio = Spring.DampingRatioLowBouncy,
                                    stiffness = Spring.StiffnessMedium
                                )
                            ),
                            exit = scaleOut(
                                animationSpec = spring(
                                    dampingRatio = Spring.DampingRatioLowBouncy,
                                    stiffness = Spring.StiffnessMedium
                                )
                            )
                        ) {
                            Column() {
                                taskForToday.forEach { task ->
                                    TaskTile(
                                        modifier = modifier
                                            .padding(
                                                vertical = 2.dp
                                            )
                                            .animateEnterExit(
                                                enter = slideInVertically(
                                                    initialOffsetY = { it / 4 }
                                                ) + fadeIn(
                                                    animationSpec = tween(250)
                                                ) + scaleIn(
                                                    animationSpec = spring(
                                                        dampingRatio = Spring.DampingRatioMediumBouncy,
                                                        stiffness = Spring.StiffnessLow
                                                    ),
                                                    initialScale = 0.95f
                                                ),
                                                exit = slideOutVertically(
                                                    targetOffsetY = { it / 4 }
                                                ) + fadeOut(
                                                    animationSpec = tween(200)
                                                ) + scaleOut()
                                            ),
                                        taskName = task.name,
                                        taskDescription = task.description,
                                        taskImportance = task.importance,
                                        taskIsCompleted = task.isCompleted,
                                        onUpdateTask = {
                                            taskViewModel.updateTask(
                                                tasks = task.copy(
                                                    isCompleted = it
                                                )
                                            )
                                        },

                                        taskNotification = task.notification,
                                        taskTime = task.taskTime!!,
                                        onClick = {}
                                    )
                                }
                            }
                        }

                        1 -> AnimatedVisibility(
                            visible = eventForToday.isNotEmpty()
                        ) {
                            Column() {
                                eventForToday.forEach {
                                    EventTile(
                                        modifier = modifier.animateEnterExit(
                                            enter = scaleIn(
                                                animationSpec = spring(
                                                    dampingRatio = Spring.DampingRatioLowBouncy,
                                                    stiffness = Spring.StiffnessLow
                                                )
                                            ),
                                            exit = scaleOut(
                                                animationSpec = spring(
                                                    dampingRatio = Spring.DampingRatioLowBouncy,
                                                    stiffness = Spring.StiffnessLow
                                                )
                                            )
                                        ),
                                        eventName = it.name,
                                        onClick = {},
                                    )
                                }
                            }
                        }
                    }
                }
            }





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


