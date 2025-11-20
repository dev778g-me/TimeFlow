package com.dev.timeflow.View.Screens

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonGroup
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerDialog
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope

import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kizitonwose.calendar.compose.HorizontalCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.core.firstDayOfWeekFromLocale
import java.time.LocalDate
import java.time.YearMonth
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.window.DialogProperties

import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.AsyncImage
import com.composables.icons.lucide.Calendar
import com.composables.icons.lucide.CalendarRange
import com.composables.icons.lucide.ListTodo
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Plus
import com.dev.timeflow.Data.Model.Events
import com.dev.timeflow.Data.Model.ImportanceChipModel
import com.dev.timeflow.Data.Model.SavingModel
import com.dev.timeflow.Data.Model.TabModel
import com.dev.timeflow.Data.Model.Tasks
import com.dev.timeflow.View.utils.componets.EventTile
import com.dev.timeflow.View.utils.componets.TaskTile
import com.dev.timeflow.View.utils.endOfDayMillis
import com.dev.timeflow.View.utils.toDateTimeInMillis
import com.dev.timeflow.View.utils.toMillis
import com.dev.timeflow.Viewmodel.TaskAndEventViewModel
import com.dev.timeflow.R
import com.dev.timeflow.View.Screens.calenderScreen.MonthCalender
import com.dev.timeflow.View.Screens.calenderScreen.MonthHeader
import com.dev.timeflow.View.Screens.calenderScreen.WeekCalender
import com.dev.timeflow.View.utils.componets.SheetToAddEventAndTask
import com.dev.timeflow.View.utils.componets.SheetToEditEvent
import com.dev.timeflow.View.utils.componets.SheetToEditTask
import com.dev.timeflow.View.utils.toHour
import com.dev.timeflow.View.utils.toLocalDate
import com.dev.timeflow.View.utils.toMinute
import com.dev.timeflow.View.utils.toUtcMillis
import com.kizitonwose.calendar.compose.WeekCalendar
import com.kizitonwose.calendar.compose.weekcalendar.rememberWeekCalendarState
import com.kizitonwose.calendar.core.atStartOfMonth
import com.kizitonwose.calendar.core.yearMonth
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.util.Calendar


@OptIn(ExperimentalMaterial3ExpressiveApi::class, ExperimentalMaterial3Api::class)
@Composable
fun CalenderScreen(
    modifier: Modifier = Modifier,
    selectedTab : Int
) {
    val haptics = LocalHapticFeedback.current
    val localContext = LocalContext.current
    val taskViewModel: TaskAndEventViewModel = hiltViewModel()
    val currentMonth = remember { YearMonth.now() }
    val startMonth = remember { currentMonth.minusMonths(100) }
    val endMonth = remember { currentMonth.plusMonths(100) }
    val firstDayOfWeek = remember { firstDayOfWeekFromLocale() }
    val state = rememberCalendarState(
        startMonth = startMonth,
        endMonth = endMonth,
        firstVisibleMonth = currentMonth,
        firstDayOfWeek = firstDayOfWeek
    )
    val currentDate = remember { LocalDate.now() }

    val startDate = remember { currentMonth.minusMonths(100).atStartOfMonth() }
    val endDate = remember { currentMonth.plusMonths(100).atEndOfMonth() }
    val weekState = rememberWeekCalendarState(
        startDate = startDate,
        endDate = endDate,
        firstVisibleWeekDate = currentDate,
        firstDayOfWeek = firstDayOfWeek
    )



    //variable to hold state of the currently selected date
    var currentSelectedDate by rememberSaveable { mutableStateOf(LocalDate.now()) }

    // var to hold the switch state of the bottom sheet
    var switchState by rememberSaveable { mutableStateOf(false) }

    // variable to hold state of the bottom sheet
    var showBottomSheet by rememberSaveable { mutableStateOf(false) }

    // var to hold state of the task name textfield
    var taskName by rememberSaveable { mutableStateOf("") }

    // var to hols the timePicker
    var showTime by rememberSaveable { mutableStateOf(false) }

    // var to hold the from timepicker  state
    var showFromTimePicker by rememberSaveable() {mutableStateOf(false) }

    // var to hold the to timepicker state

    var showToTimePicker by rememberSaveable() { mutableStateOf(false)}

    // var to  hold the datePicker
    var showDate by rememberSaveable() {mutableStateOf(false) }

    // var to hold the second datePicker

    var showToDate by rememberSaveable() {mutableStateOf(false) }

    // var to hold the navigate to app info page
    var showPermissionDialog by rememberSaveable() {mutableStateOf(false) }

    // var to hold the state of the task description textfield
    var taskDescription by rememberSaveable { mutableStateOf("") }


    var showTaskDetails by rememberSaveable { mutableStateOf(false) }

    var showEventDetails by rememberSaveable() { mutableStateOf(false) }


    var selectedIndex by rememberSaveable { mutableIntStateOf(0) }

    val localTime = LocalTime.now()

    // state for the timePicker
    val timePickerState = rememberTimePickerState(
        is24Hour = false,
        initialHour = localTime.hour,
        initialMinute = localTime.minute
    )

    val fromToTime = LocalTime.of(12,0)
    // timepicker for the from time
    val fromTimePickerState = rememberTimePickerState(
        //is24Hour =  false,
        initialHour =  fromToTime.hour,
        initialMinute = fromToTime.minute
    )

    // timePicker for the to time
    val toTimeTimePickerState = rememberTimePickerState(
        is24Hour =  false,
        initialHour = fromToTime.hour,
        initialMinute = fromToTime.minute
    )

    val fromDatePickerState = rememberDatePickerState(
        initialSelectedDate = currentSelectedDate,
        initialDisplayedMonth = currentMonth,
        selectableDates =  object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                val date = Instant.ofEpochMilli(utcTimeMillis)
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate()

                return !date.isBefore(currentSelectedDate)
            }
        },
        yearRange = currentDate.year .. currentDate.plusYears(20).year,
        initialDisplayMode = DisplayMode.Picker,
    )

    val toDatePickerState = rememberDatePickerState(
        initialSelectedDate = currentSelectedDate.plusDays(1),
        initialDisplayedMonth = currentSelectedDate.plusDays(1).yearMonth,
        selectableDates =  object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                val date = Instant.ofEpochMilli(utcTimeMillis)
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate()

                return !date.isBefore(currentSelectedDate.plusDays(1))
            }
        },
        yearRange = currentDate.year .. currentDate.plusYears(20).year,
        initialDisplayMode = DisplayMode.Picker
    )

    val pageState = rememberPagerState(initialPage = 0, pageCount = { 2 })

    val scope = rememberCoroutineScope()

    LaunchedEffect(currentSelectedDate) {
        Log.d("TASKDATE", "the function ran with the updated date $currentSelectedDate")
        taskViewModel.getTasksForADate(
            start = currentSelectedDate
                .atStartOfDay(ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli(),
            end = currentSelectedDate.endOfDayMillis()

        ).also {
            Log.d("TASKDATE", "the function just completed with the date $currentSelectedDate")
        }

        taskViewModel.getAllEventsForADate(
            start = currentSelectedDate
                .atStartOfDay(ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli(),
            end = currentSelectedDate.endOfDayMillis()
        )

        val currentSelectedDateUtc = currentSelectedDate.toUtcMillis()
        val tomorrowSelectedDateUtc = currentSelectedDate.plusDays(1).toUtcMillis()
        if (fromDatePickerState.selectedDateMillis != currentSelectedDateUtc){
            fromDatePickerState.selectedDateMillis = currentSelectedDateUtc
            toDatePickerState.selectedDateMillis = tomorrowSelectedDateUtc
        }

        fromDatePickerState.displayedMonthMillis = currentSelectedDateUtc
        toDatePickerState.displayedMonthMillis = tomorrowSelectedDateUtc

    }

    val tasksForDate by taskViewModel.taskForDate.collectAsState(emptyList())
    val eventsForDate by taskViewModel.eventForDate.collectAsState(emptyList())
    val currentTask by taskViewModel.currentTask.collectAsState(null)
    val currentEvent by taskViewModel.currentEvent.collectAsState(null)
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
    // var to hold the state of the importance chip

    var selectedChip by rememberSaveable { mutableIntStateOf(0) }
    LaunchedEffect(pageState) {
        snapshotFlow { pageState.currentPage }
            .collect { page ->
                selectedIndex = page
            }
    }
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
    var selectedSavingType by rememberSaveable { mutableIntStateOf(0) }
    var isButtonEnabled by rememberSaveable(
        taskName,
        selectedSavingType,

        ) {
        if (selectedSavingType == 0) mutableStateOf(taskName.isNotEmpty())
        else {
            mutableStateOf(taskName.isNotEmpty())
        }
    }

    if (showPermissionDialog){
        AlertDialog(
            title = {
                Text(
                    text = "Notification Access Required"
                )
            },
            text = {
                Text(
                    text = "To ensure your reminders trigger reliably, we need access to send notifications. Please go to Settings to enable this permission"
                )
            },
            onDismissRequest = {
                showPermissionDialog = false
            },
            confirmButton = {
                Button(
                    onClick = {
                        showPermissionDialog = false
                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                            data = Uri.fromParts("package",localContext.packageName, null)
                            addCategory(Intent.CATEGORY_DEFAULT)
                            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        }

                        try {
                            localContext.startActivity(intent)
                        }catch (e: Exception){
                            Toast.makeText(localContext,"Some error has occurred", Toast.LENGTH_LONG).show()
                        }
                    }
                ) {
                    Text(
                        text = "Settings"
                    )
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = {

                        showPermissionDialog = false
                    }
                ) {
                    Text(
                        text =  "Cancel"
                    )
                }
            }
        )
    }

    if (showBottomSheet) {
        SheetToAddEventAndTask(
            onDismiss = {
                showBottomSheet = false
                taskName = ""
                taskDescription = ""
                switchState = false
                timePickerState.hour = localTime.hour
                timePickerState.minute = localTime.minute
            },
            modifier = modifier,
            onSwitchState = {
                switchState = it
            },
            selectedSavingType = selectedSavingType,
            isButtonEnabled = isButtonEnabled,
            onTaskSave = {
                taskViewModel.insertTask(
                    tasks = Tasks(
                        id = 0,
                        name = taskName,
                        description = taskDescription,
                        notification = switchState,
                        importance = importanceChip[selectedChip].label,
                        taskTime = if (switchState) Calendar.getInstance().toDateTimeInMillis(
                            hour = timePickerState.hour,
                            minute = timePickerState.minute,
                            date = currentSelectedDate
                        ) else {
                            0
                        },
                        createdAt = currentSelectedDate.toMillis(localTime = localTime)
                    )
                )
                switchState = false
                timePickerState.hour = localTime.hour
                timePickerState.minute = localTime.minute
            }, onEventSave = {
                taskViewModel.insertEvent(
                    events = Events(
                        id = 0,
                        name = taskName,
                        notification = switchState,
                        description = taskDescription,
                        eventNotificationTime = if (switchState) Calendar.getInstance().toDateTimeInMillis(
                            hour = timePickerState.hour,
                            minute = timePickerState.minute,
                            date = currentSelectedDate
                        ) else {
                            0
                        },
                        createdAt = currentSelectedDate.toMillis(localTime = localTime),
                        eventStartTime = fromDatePickerState.selectedDateMillis?.toLocalDate()
                            ?.toMillis(
                                localTime = LocalTime.of(
                                    fromTimePickerState.hour,
                                    fromTimePickerState.minute
                                )
                            )!!,
                        eventEndTime = toDatePickerState.selectedDateMillis?.toLocalDate()
                            ?.toMillis(
                                localTime = LocalTime.of(
                                    toTimeTimePickerState.hour,
                                    toTimeTimePickerState.minute
                                )
                            )!!


                    )
                )
                switchState = false
                timePickerState.hour = localTime.hour
                timePickerState.minute = localTime.minute
            },
            onSelectedImportantChipChange = {
                selectedChip = it
            },
            onTaskNameChange = {
                taskName = it
            },
            onTaskDescriptionChange = {
                taskDescription = it
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
            selectedImportantChip = selectedChip,
            showTimeState = showTime,
            onTimeState = {
                showTime = it
            },
            timerState = timePickerState,
            context = localContext,
            onPermissionState = {
                showPermissionDialog = it
            },
            onFromTileClick = {
                showDate = !showDate
            },
            onToTileClick = {
                showToDate = true
            },
            fromTimePickerState = fromTimePickerState,
            toTimePickerState = toTimeTimePickerState,
            fromDatePickerState = fromDatePickerState,
            toDatePickerState = toDatePickerState,
            onFromTimePicker = {
                showFromTimePicker = true
            },
            onToTimePicker = {
                showToTimePicker = true
            }
        )
    }



    if (showTaskDetails && currentTask != null) {
        val latestTask = tasksForDate.find { it.id == currentTask!!.id } ?: currentTask!!

        var editedDescription by remember(latestTask.id) { mutableStateOf(latestTask.description) }
        var editedName by remember(latestTask.id) { mutableStateOf(latestTask.name) }

        SheetToEditTask(
            tasks = latestTask,
            onDismiss = {
                showTaskDetails = false
                taskViewModel.clearTask()
            },
            onCheckBoxValueChange = {
                taskViewModel.updateTask(
                    latestTask.copy(
                        isCompleted = it
                    )
                )
            },
            onValueChange = {
                editedDescription = it
            },
            onNameValueChange = {
                editedName = it
            }
        )
    }

    if (showEventDetails && currentEvent != null) {
        val latestEvent = eventsForDate.find { it.id == currentEvent!!.id } ?: currentEvent!!

        var showEditFromDatePicker by remember { mutableStateOf(false) }
        var showEditToDatePicker by remember { mutableStateOf(false) }
        var showEditFromTimePicker by remember { mutableStateOf(false) }
        var showEditToTimePicker by remember { mutableStateOf(false) }
        var editedDescription by remember(latestEvent.id) { mutableStateOf(latestEvent.description) }
        var editedName by remember(latestEvent.id) { mutableStateOf(latestEvent.name) }


        val editFromDatePicker = rememberDatePickerState(
            initialSelectedDateMillis = latestEvent.eventStartTime
                .toLocalDate()
                .atStartOfDay(ZoneOffset.UTC)
                .toInstant()
                .toEpochMilli(),
            selectableDates = object : SelectableDates {
                override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                    val minDate = latestEvent.eventStartTime
                        .toLocalDate()
                        .atStartOfDay(ZoneOffset.UTC)
                        .toInstant()
                        .toEpochMilli()

                    return utcTimeMillis >= minDate
                }
            }
        )


        val editToDatePicker = rememberDatePickerState(
            initialSelectedDate = latestEvent.eventEndTime.toLocalDate(),
            selectableDates = object : SelectableDates {
                override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                    val minDate = latestEvent.eventStartTime
                        .toLocalDate().plusDays(1)
                        .atStartOfDay(ZoneOffset.UTC)
                        .toInstant()
                        .toEpochMilli()

                    return utcTimeMillis >= minDate
                }
            }
        )
        val editFromTimePickerState = rememberTimePickerState(
            initialHour = latestEvent.eventStartTime.toHour(),
            initialMinute = latestEvent.eventStartTime.toMinute()
        )
        val editToTimePickerState = rememberTimePickerState(
            initialHour =  latestEvent.eventEndTime.toHour(),
            initialMinute = latestEvent.eventEndTime.toMinute()
        )

        if (showEditToDatePicker){
            DatePickerDialog(
                onDismissRequest = {
                    showEditToDatePicker = false
                },
                confirmButton = {
                    Button(
                        onClick = {
                            showEditToDatePicker = false
                        }
                    ) {
                        Text(text = "Confirm")
                    }
                },

            ) {

                DatePicker(
                    state = editToDatePicker
                )
            }
        }

        if (showEditFromDatePicker){
           DatePickerDialog(
               onDismissRequest = {
                   showEditFromDatePicker = false
               },

               confirmButton = {
                   Button(
                       onClick = {
                           showEditFromDatePicker = false

                       }
                   ) {
                       Text(
                           text = "Confirm"
                       )
                   }
               }
           ) {
               DatePicker(
                   state = editFromDatePicker
               )
           }
        }

        if (showEditFromTimePicker){
            TimePickerDialog(
                title = {
                    Text(
                        modifier = modifier.padding(
                            bottom = 8.dp
                        ),
                        text = "Select start time",
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                onDismissRequest = {
                    showEditFromTimePicker = false
                },

                confirmButton = {
                    Button(
                        onClick = {
                            showEditFromTimePicker = false
                        }
                    ) {
                        Text(
                            text = "Confirm"
                        )
                    }
                }
            ) {
                TimePicker(
                    state = editFromTimePickerState
                )
            }
        }
        if (showEditToTimePicker){
            TimePickerDialog(
                title = {
                    Text(
                        modifier = modifier.padding(
                            bottom = 8.dp
                        ),
                        text = "Select end time",
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                onDismissRequest = {
                    showEditToTimePicker = false
                },
                confirmButton = {
                      Button(
                          onClick = {
                              showEditToTimePicker = false
                          }
                      ) {
                          Text(
                              text = "Confirm"
                          )
                      }
                }
            ) {
                TimePicker(
                    state = editToTimePickerState
                )
            }
        }
        SheetToEditEvent(
            onDismiss = {
                showEventDetails = false
                taskViewModel.clearEvent()
            },
            onValueChange = {
                editedDescription = it
            },
            onNameValueChange = {
                editedName = it
            },
            onStartDateChipClick = {
                showEditFromDatePicker = true
            },
            onEndDateChipClick = {
                showEditToDatePicker = true
            },
            onStartTimeChipClick = {
               showEditFromTimePicker = true
            },
            onEndTimeChipClick = {
                showEditToTimePicker = true
            },
            event = currentEvent!!,
            fromDatePickerState = editFromDatePicker,
            toDatePickerState = editToDatePicker,
            fromTimePickerState = editFromTimePickerState,
            toTimePickerState = editToTimePickerState,
            onUpdateEvent = {
                if (editToDatePicker.selectedDateMillis!! < editFromDatePicker.selectedDateMillis!!.toLong()) {
                    Toast.makeText(
                        localContext,
                        "Start date must be earlier than or equal to the end date.",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    taskViewModel.updateEvent(

                        latestEvent.copy(
                            name = editedName,
                            description = editedDescription,
                            eventStartTime = editFromDatePicker.selectedDateMillis!!.toLocalDate()
                                .toMillis(
                                    localTime = LocalTime.of(
                                        editFromTimePickerState.hour, editFromTimePickerState.minute
                                    ),
                                ),
                            eventEndTime = editToDatePicker.selectedDateMillis!!.toLocalDate()
                                .toMillis(
                                    localTime = LocalTime.of(
                                        editToTimePickerState.hour, editToTimePickerState.minute
                                    )
                                )
                        )
                    )
                }

            },
            onDeleteEvent = {
                taskViewModel.deleteEvent(
                    latestEvent
                )
            }
        )
    }


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

        ) { innerPadding ->
        if (showTime) {
            AlertDialog(
                text = {
                    TimePicker(
                        state = timePickerState,
                    )
                },
                dismissButton = {
                    OutlinedButton(
                        onClick = {
                            showTime = false
                            switchState =false
                            timePickerState.hour = localTime.hour
                            timePickerState.minute = localTime.minute
                        }
                    ) {
                        Text(
                            "Cancel"
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            showTime = false
                        }
                    ) {
                        Text("Confirm")
                    }
                },
                onDismissRequest = {
                    showTime = false
                    switchState = false
                    timePickerState.hour = localTime.hour
                    timePickerState.minute = localTime.minute
                },
                properties = DialogProperties(),
                title = {
                    Text(
                        text = "Pick Time"
                    )
                },

                )
        }

        if (showFromTimePicker){
            TimePickerDialog(
                title = {
                    Text(
                        text = "Select from time "
                    )
                },
                onDismissRequest = {
                    showFromTimePicker = false
                },
                dismissButton = {
                    OutlinedButton(
                        onClick = {
                            showFromTimePicker = false
                        }
                    ) {
                        Text(
                            text = "Cancel"
                        )
                    }
                },

                confirmButton = {
                    Button(
                        modifier = modifier.padding(
                            start = 8.dp
                        ),
                        onClick = {

                            showFromTimePicker = false
                        }
                    ) {
                        Text(
                            text = "Confirm"
                        )
                    }
                }
            ) {
                TimePicker(
                    state = fromTimePickerState
                )
            }
        }
        if (showToTimePicker){
            TimePickerDialog(
                title = {
                    Text(
                        text = "Select To time "
                    )
                },
                onDismissRequest = {
                    showToTimePicker = false
                },
                dismissButton = {
                    OutlinedButton(
                        onClick = {
                           showToTimePicker = false
                        }
                    ) {
                        Text(
                            text = "Cancel"
                        )
                    }
                },
                confirmButton = {
                    Button(
                        modifier = modifier.padding(
                            start = 8.dp
                        ),
                        onClick = {
                            showToTimePicker = false
                        }
                    ) {
                        Text(
                            text = "Confirm"
                        )
                    }
                }
            ) {
                TimePicker(
                    state = toTimeTimePickerState
                )
            }
        }
        if (showDate){
            DatePickerDialog(
                onDismissRequest = {
                    showDate = false
                },
                confirmButton = {
                    Button(
                        onClick = {
                            showDate = false
                        }
                    ) {
                        Text(
                            text = "Confirm"
                        )
                    }
                },
                dismissButton = {
                    OutlinedButton(
                        onClick = {
                            showDate = false
                        }
                    ) {
                        Text(
                            text = "Cancel"
                        )
                    }
                }
            ) {
                DatePicker(
                    state = fromDatePickerState
                )
            }

        }
        if (showToDate){
            DatePickerDialog(
                onDismissRequest = {
                    showToDate = false
                },
                confirmButton = {
                    Button(
                        onClick = {
                           showToDate = false
                        }
                    ) {
                        Text(
                            text = "Confirm"
                        )
                    }
                },
                dismissButton = {
                    OutlinedButton(
                        onClick = {
                            showToDate = false
                        }
                    ) {
                        Text(
                            text = "Cancel"
                        )
                    }
                }
            ) {
                DatePicker(
                    state = toDatePickerState
                )
            }

        }
        Column(
            modifier = modifier
                .fillMaxSize()
//                .animateContentSize(
//                    animationSpec = spring(dampingRatio = Spring.DampingRatioLowBouncy)
//                ),
            ,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AnimatedContent(
                modifier = modifier.animateContentSize(
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessMedium
                    )
                ),
                targetState = selectedTab,
                transitionSpec = {
                 scaleIn(
                     animationSpec = spring(
                         dampingRatio = Spring.DampingRatioLowBouncy,
                         stiffness = Spring.StiffnessMediumLow
                     )
                 ) + fadeIn() togetherWith scaleOut(
                     animationSpec = spring(
                         dampingRatio = Spring.DampingRatioLowBouncy,
                         stiffness = Spring.StiffnessMediumLow
                 )
             ) + fadeOut()
             }
         ) {
             if (it ==0){
                 WeekCalendar(
                     modifier = modifier.padding(
                         horizontal = 16.dp
                     ),
                     state = weekState,
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
             else if(it ==1){
                 HorizontalCalendar(
                     modifier = modifier.padding(
                         horizontal = 8.dp
                     ),
                     state = state,
                     reverseLayout = false,
                     dayContent = {
                         MonthCalender(
                             day = it,
                             hapticFeedback = haptics,
                             selectedDate = currentSelectedDate,
                             onClick = { date ->
                                 currentSelectedDate = date
                             }
                         )
                     },
                     monthHeader = {
                         MonthHeader(

                             monthName = it.yearMonth.month.toString(),
                             weekName = it.weekDays.first().map {
                                 it.date.dayOfWeek.toString().take(3).lowercase()
                                     .replaceFirstChar {
                                         it.uppercase()
                                     }
                             },
                             onClick = {
                                 scope.launch {
                                     state
                                     state.animateScrollToMonth(currentMonth)

                                 }
                             }
                         )
                     }
                 )
             }
         }


          Column(
              modifier = modifier
          ) {
              AnimatedContent(
                  modifier = modifier
                     // .animateContentSize()
                      .align(Alignment.CenterHorizontally),
                  targetState = tasksForDate.isNotEmpty() || eventsForDate.isNotEmpty()
              ) { it ->

                  if (it) {
                      Column(
                          modifier = modifier
                              .fillMaxSize()
                              .padding(
                                  horizontal = 16.dp
                              )

                      ) {

                              ButtonGroup(
                                  modifier = modifier
                                      .fillMaxWidth()
                                      .padding(
                                          vertical = 8.dp,
                                      )
                                  ,
                                  overflowIndicator = {}
                              ) {
                                  tabs.forEachIndexed { index, model ->
                                      val isSelected = index == selectedIndex
                                      toggleableItem(
                                          weight = 1f,
                                          checked = isSelected,
                                          onCheckedChange = {
                                              selectedIndex = index
                                              haptics.performHapticFeedback(
                                                  hapticFeedbackType = HapticFeedbackType.Confirm
                                              )
                                              scope.launch {
                                                  pageState.animateScrollToPage(
                                                      index
                                                  )
                                              }
                                          },
                                          label = if (model.title == "Tasks") "${model.title} ${tasksForDate.size}" else "${model.title} ${eventsForDate.size}",
                                      )
                                  }
                              }




                                  HorizontalPager(
                                      modifier = modifier
                                          .fillMaxWidth()
                                          .weight(1f),
                                      state = pageState,
                                      beyondViewportPageCount = 0,
                                      key = {
                                          it
                                      }
                                  ) { page ->
                                      Box(modifier = modifier
                                          .fillMaxSize()
                                          .wrapContentHeight(),contentAlignment = Alignment.TopStart){
                                          when(page) {
                                              0 -> LazyColumn(
                                                  modifier = modifier.fillMaxSize()
                                              ) {
                                                  items(tasksForDate){
                                                      TaskTile(
                                                          onUpdateTask = { value ->
                                                              taskViewModel.updateTask(
                                                                  tasks = it.copy(
                                                                      isCompleted = value
                                                                  )
                                                              )


                                                          },
                                                          taskName = it.name,
                                                          taskDescription = it.description,
                                                          taskIsCompleted = it.isCompleted,
                                                          taskImportance = it.importance,
                                                          taskNotification = it.notification,
                                                          taskTime = it.taskTime!!,
                                                          onClick = {
                                                              taskViewModel.selectTask(tasks = it)
                                                              showTaskDetails = true
                                                          }
                                                      )
                                                  }
                                              }
                                              1->  LazyColumn (
                                                  modifier = modifier.fillMaxSize()
                                              ){
                                                  items(eventsForDate){
                                                      EventTile(
                                                          eventName = it.name,
                                                          onClick = {
                                                              taskViewModel.selectEvent(
                                                                  events = it
                                                              )
                                                              showEventDetails = true
                                                          },
                                                          eventFromDay = it.eventStartTime,
                                                          eventEndDay = it.eventEndTime
                                                      )
                                                  }
                                              }

                                          }
                                      }
                                  }



                      }
                  } else {
                      // using a placeholder image if both task and events are empty ...>>>>
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

