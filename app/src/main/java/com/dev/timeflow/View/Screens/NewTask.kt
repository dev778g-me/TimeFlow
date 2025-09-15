package com.dev.timeflow.View.Screens

import android.media.metrics.Event
import android.util.Log
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CalendarViewDay
import androidx.compose.material.icons.rounded.CalendarViewMonth
import androidx.compose.material.icons.rounded.CalendarViewWeek
import androidx.compose.material.icons.rounded.FilterList
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.ToggleButton
import androidx.compose.material3.ToggleButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kizitonwose.calendar.compose.HorizontalCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.core.firstDayOfWeekFromLocale
import java.time.LocalDate
import java.time.YearMonth
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback

import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.glance.text.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.AsyncImage
import com.composables.icons.lucide.Bell
import com.composables.icons.lucide.Calendar
import com.composables.icons.lucide.Clock
import com.composables.icons.lucide.FlagTriangleRight
import com.composables.icons.lucide.ListTodo
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Notebook
import com.composables.icons.lucide.Plus
import com.composables.icons.lucide.Signature
import com.dev.timeflow.Data.Model.DropdownModel
import com.dev.timeflow.Data.Model.Events
import com.dev.timeflow.Data.Model.ImportanceChipModel
import com.dev.timeflow.Data.Model.SavingModel
import com.dev.timeflow.Data.Model.Tasks
import com.dev.timeflow.Managers.utils.componets.EventTile
import com.dev.timeflow.Managers.utils.componets.Tasktile
import com.dev.timeflow.Managers.utils.toMidnight
import com.dev.timeflow.Viewmodel.EventViewModel
import com.dev.timeflow.R
import com.dev.timeflow.View.Screens.calenderScreen.MonthCalender
import com.dev.timeflow.View.Screens.calenderScreen.MonthHeader
import com.dev.timeflow.View.Screens.calenderScreen.WeekCalender
import com.dev.timeflow.View.Widget.SheetToAddEventAndTask
import com.kizitonwose.calendar.compose.WeekCalendar
import com.maxkeppeker.sheets.core.models.base.rememberUseCaseState
import com.maxkeppeler.sheets.calendar.CalendarDialog
import com.maxkeppeler.sheets.calendar.models.CalendarConfig
import com.maxkeppeler.sheets.calendar.models.CalendarSelection
import com.maxkeppeler.sheets.calendar.models.CalendarStyle
import java.time.ZoneId


@OptIn(ExperimentalMaterial3ExpressiveApi::class, ExperimentalMaterial3Api::class)
@Preview(showBackground = true, showSystemUi = true
)
@Composable
fun NewTask(modifier: Modifier = Modifier) {
    val haptics = LocalHapticFeedback.current
    val taskViewModel: EventViewModel = hiltViewModel()
    val currentMonth = remember { YearMonth.now() }
    val startMonth = remember { currentMonth.minusMonths(100) }
    val endMonth = remember { currentMonth.plusMonths(100) }
    val firstDayOfWeek = remember { firstDayOfWeekFromLocale() }

    val disabledDates = listOf(
        LocalDate.now(),
    )

    //variable to hold state of the currently selected date
    var currentSelectedDate by rememberSaveable{mutableStateOf(LocalDate.now())}

    // var to hold the switch state of the bottom sheet
    var switchState by rememberSaveable { mutableStateOf(false) }

    // variable to hold state of the bottom sheet
    var showBottomSheet by rememberSaveable { mutableStateOf(false) }

    // var to hold state of the task name textfield
    var taskName by rememberSaveable { mutableStateOf("") }

    // var to hold the state of the task description textfield
    var taskDescription by rememberSaveable { mutableStateOf("") }

    // var to hold to choose the date for the event
    var showCalendar by rememberSaveable { mutableStateOf(false) }
    val today = LocalDate.now()
    var selectedDates = rememberSaveable { mutableStateOf<LocalDate>(today) }



    LaunchedEffect(currentSelectedDate) {
        Log.d("TASKDATE","the function ran with the updated date $currentSelectedDate")
        taskViewModel.getTasksForADate(
            date = currentSelectedDate
                .atStartOfDay(ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli()
        ).also {
            Log.d("TASKDATE","the function just completed with the date $currentSelectedDate")
        }

        taskViewModel.getAllEventsForADate(
            date = currentSelectedDate.atStartOfDay(ZoneId.systemDefault())
                .toInstant().toEpochMilli()
        )
    }

    val tasksForDate by taskViewModel.taskForDate.collectAsState(emptyList())
    val eventsForDate by taskViewModel.eventForDate.collectAsState(emptyList())
    val importanceChip = listOf<ImportanceChipModel>(
        ImportanceChipModel(
            label = "Low",
            color = Color(0xFF4CAF50) // Material Green 500
        ),
        ImportanceChipModel(
            label = "Medium",
            color = Color(0xFFFFC107) // Material Amber 500
        ),
        ImportanceChipModel(
            label = "High",
            color = Color(0xFFF44336) // Material Red 500
        )
    )

    // var to hold the state of the importance chip

    var selectedChip by rememberSaveable { mutableStateOf(0) }

    //choosing event or tsk
    val type = listOf<SavingModel>(
        SavingModel(
            title = "Event",
            icon = Lucide.Calendar
        ),
        SavingModel(
            title = "Task",
            icon = Lucide.ListTodo
        )
    )
    var selectedSavingType by rememberSaveable { mutableStateOf(0) }
    var isButtonEnabled by rememberSaveable(
        taskName,
        selectedSavingType,
        selectedDates
    ) {
        if (selectedSavingType == 0) mutableStateOf(taskName.isNotEmpty() && selectedDates.value == today)
        else {
            mutableStateOf(taskName.isNotEmpty())
        }
    }
   if (showBottomSheet){
       SheetToAddEventAndTask(
           onDismiss = {
               showBottomSheet = false
               taskName = ""
               taskDescription = ""
           },
           modifier = modifier,
           onSwitchState = {
               switchState = it
           },
           selectedSavingType =selectedSavingType,
           isButtonEnabled = isButtonEnabled,
           onTaskSave = {
               taskViewModel.insertTask(
                   tasks = Tasks(
                       id = 0,
                       name = taskName,
                       description = taskDescription,
                       notification = switchState,
                       importance = importanceChip[selectedChip].label,
                       taskDate = currentSelectedDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
                   )
               )
           },
           onEventSave = {
               taskViewModel.insertEvent(
                   events = Events(
                       id = 0,
                       title = taskName,
                       description = taskDescription,
                       eventTime = currentSelectedDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
                   )
               )
           },
           showCalendar = {
               showCalendar = true
           },
           onselectedImportantChipChange = {
               selectedChip = it
           },
           onTaskNameChange = {
               taskName = it
           },
           onTaskDescriptionChange = {
               taskDescription = it
           },
           onSwitchChange = {
               switchState = it
           },
           changeSavingType = {
               selectedSavingType = it
           },
           savingChipList = type,
           importanceChip = importanceChip,
           hapticFeedback = haptics,
           switchState = switchState,
           taskName = taskName,
           taskDescription = taskDescription,
           selectedImportantChip = selectedChip
       )
   }

    if (showCalendar) {
        Popup(
            onDismissRequest = {
                showCalendar = false
            },
            alignment = Alignment.TopStart
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(y = 64.dp)
                    .shadow(elevation = 4.dp)
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(16.dp)
            ) {
                CalendarDialog(
                    state = rememberUseCaseState(visible = true, onCloseRequest = {
                        showCalendar = false

                    }),
                    config = CalendarConfig(
                        yearSelection = true,
                        monthSelection = true,
                        boundary = LocalDate.now() ..LocalDate.of(2030, 12, 31),
                        style = CalendarStyle.MONTH,
                        disabledDates = disabledDates
                    ),
                    selection = CalendarSelection.Date {
                        selectedDates.value = it
                    }
                )
            }
        }
    }

    val state = rememberCalendarState(
        startMonth = startMonth,
        endMonth = endMonth,
        firstVisibleMonth = currentMonth,
        firstDayOfWeek = firstDayOfWeek
    )


    val dropDownItems = listOf<DropdownModel>(
        DropdownModel(
            title = "Weekly",
            icon = Icons.Rounded.CalendarViewWeek,
            onClick = {}
        ),
        DropdownModel(
            title = "Monthly",
            icon = Icons.Rounded.CalendarViewMonth,
            onClick = {}
        ),
        DropdownModel(
            title = "Yearly",
            icon = Icons.Rounded.CalendarViewDay,
            onClick = {

            }
        )

    )
    var selectedDropDownMenu by rememberSaveable { mutableStateOf(dropDownItems[1].title) }
    Scaffold(
        //contentWindowInsets = WindowInsets(0.dp),
        floatingActionButton = {
           FloatingActionButton(
               onClick = {
                   showBottomSheet = !showBottomSheet
               }
           ) {
               Icon(imageVector = Lucide.Plus, contentDescription = null)
           }
        },

    ) {
        innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            when (selectedDropDownMenu) {
                "Weekly" -> {
                    WeekCalendar(
                        dayContent = {
                            weekDate ->
                            WeekCalender(
                                weekDate = weekDate,
                                selectedDate = currentSelectedDate,
                                onClick = {
                                    currentSelectedDate = it
                                }
                            )
                        },
                    )
                }
                "Monthly" -> {
                    AnimatedVisibility(
                        visible = selectedDropDownMenu =="Monthly"
                    ) { HorizontalCalendar(
                        state = state,
                        reverseLayout = true,
                        dayContent = {
                            MonthCalender(
                                day = it,
                                hapticFeedback = haptics,
                                selectedDate = currentSelectedDate,
                                onClick = {
                                    currentSelectedDate = it
                                }
                            )
                        },
                        monthHeader = {
                            MonthHeader(
                                monthName = it.yearMonth.month.toString(),
                                weekName = it.weekDays.first().map {
                                    it.date.dayOfWeek.toString().take(3).toLowerCase().replaceFirstChar {
                                        it.toUpperCase()
                                    }
                                }
                            )
                        }
                    )}
                }
            }

          Column {
              AnimatedContent(
                  modifier = modifier.align(Alignment.CenterHorizontally),
                  targetState = tasksForDate.isNotEmpty() || eventsForDate.isNotEmpty()
              ) {
                  if (it){
                      LazyColumn(
                          modifier = modifier.padding(
                              horizontal = 8.dp
                          )
                      ) {
                          item {
                              Text(
                                  modifier = modifier.padding(horizontal = 12.dp),
                                  text = "Tasks",
                                  color = MaterialTheme.colorScheme.primary
                              )
                          }
                          items(tasksForDate) {
                              Tasktile(
                                  onUpdateTask = { value ->
                                      println(value)
                                      taskViewModel.updateTask(
                                          tasks = it.copy(
                                              isCompleted = value
                                          )
                                      )
                                  },
                                  taskName = it.name,
                                  taskDescription = it.description,
                                  taskCreatedAt = it.createdAt,
                                  taskDate = it.taskDate,
                                  taskIsCompleted = it.isCompleted,
                                  taskImortance = it.importance
                              )
                          }

                          item {
                              AnimatedContent(
                                  targetState = eventsForDate.isNotEmpty()
                              ) {
                                  if (it){
                                      Column {
                                          Text(
                                              modifier = modifier.padding(horizontal = 12.dp),
                                              text = "Events",
                                              color = MaterialTheme.colorScheme.primary
                                          )
                                          eventsForDate.forEach {
                                              EventTile(
                                                  events = it
                                              )
                                          }
                                      }
                                  }
                              }

                          }
                      }
                  } else {
                      Column(
                          modifier = modifier
                              .fillMaxSize()
                              .weight(1f),
                          horizontalAlignment = Alignment.CenterHorizontally,
                          verticalArrangement = Arrangement.Center
                      ) {
                          AsyncImage(
                              modifier = modifier.size(150.dp),
                              model = R.drawable.emptytask,
                              contentDescription = null
                          )
                          Text(
                              text = "Chill out buddy\nyou got nothing"
                          )
                      }
                  }
              }


          }


        }
    }

}

