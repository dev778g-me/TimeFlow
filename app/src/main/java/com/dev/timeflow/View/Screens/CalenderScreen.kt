package com.dev.timeflow.View.Screens

import android.util.Log
import android.view.HapticFeedbackConstants
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CalendarViewDay
import androidx.compose.material.icons.rounded.CalendarViewMonth
import androidx.compose.material.icons.rounded.CalendarViewWeek
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonGroup
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerLayoutType
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

import androidx.compose.ui.window.Popup
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.AsyncImage
import com.composables.icons.lucide.Calendar
import com.composables.icons.lucide.CalendarRange
import com.composables.icons.lucide.ListTodo
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Plus
import com.dev.timeflow.Data.Model.DropdownModel
import com.dev.timeflow.Data.Model.Events
import com.dev.timeflow.Data.Model.ImportanceChipModel
import com.dev.timeflow.Data.Model.SavingModel
import com.dev.timeflow.Data.Model.TabModel
import com.dev.timeflow.Data.Model.Tasks
import com.dev.timeflow.Managers.utils.componets.EventTile
import com.dev.timeflow.Managers.utils.componets.Tasktile
import com.dev.timeflow.Viewmodel.TaskAndEventViewModel
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
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId


@OptIn(ExperimentalMaterial3ExpressiveApi::class, ExperimentalMaterial3Api::class)
@Composable
fun CalenderScreen(modifier: Modifier = Modifier) {
    val haptics = LocalHapticFeedback.current
    val taskViewModel: TaskAndEventViewModel = hiltViewModel()
    val currentMonth = remember { YearMonth.now() }
    val startMonth = remember { currentMonth.minusMonths(100) }
    val endMonth = remember { currentMonth.plusMonths(100) }
    val firstDayOfWeek = remember { firstDayOfWeekFromLocale() }
    val localTime = LocalDateTime.now()
    val disabledDates = listOf(
        localTime.toLocalDate(),
    )

    // local view for haptic feedback
    val localView = LocalView.current


    //variable to hold state of the currently selected date
    var currentSelectedDate by rememberSaveable{mutableStateOf(LocalDate.now())}

    // var to hold the switch state of the bottom sheet
    var switchState by rememberSaveable { mutableStateOf(false) }

    // variable to hold state of the bottom sheet
    var showBottomSheet by rememberSaveable { mutableStateOf(false) }

    // var to hold state of the task name textfield
    var taskName by rememberSaveable { mutableStateOf("") }

    // var to hols the timePicker
    var showTime by rememberSaveable { mutableStateOf(false) }

    // var to hold the state of the task description textfield
    var taskDescription by rememberSaveable { mutableStateOf("") }

    // var to hold to choose the date for the event
    var showCalendar by rememberSaveable { mutableStateOf(false) }

    val today = localTime.toLocalDate()
    val selectedDates = rememberSaveable { mutableStateOf<LocalDate>(today) }

    var selectedIndex by remember { mutableIntStateOf(0) }


    // state for the timePicker
    val timePickerState = rememberTimePickerState(
        initialHour = localTime.hour,
        initialMinute = localTime.minute
    )



    val pageState = rememberPagerState(initialPage = 0, pageCount = {2})

    val scope = rememberCoroutineScope()

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

    var selectedChip by rememberSaveable { mutableStateOf(0) }
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
                       taskTime = currentSelectedDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
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
           selectedImportantChip = selectedChip,
           showTimeState = showTime,
           onTimeState = {
               showTime = it
           }
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
                        boundary = LocalDate.now()..LocalDate.of(2030, 12, 31),
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

        ) { innerPadding ->
        if (showTime) {
            AlertDialog(
                text = {
                    TimePicker(
                        state = timePickerState,
                       // layoutType = TimePickerLayoutType.Horizontal
                    )
                },
                dismissButton = {
                    OutlinedButton(
                        onClick = {}
                    ) {
                        Text(
                            "Cancel"
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {}
                    ) {
                        Text("Confirm")
                    }
                },
                onDismissRequest = {
                    showTime = false
                },
                properties = DialogProperties(),
                title = {
                    Text(
                        text = "Pick Time"
                    )
                },

            )
        }
        Column(
            modifier = modifier
                .fillMaxSize()
                .animateContentSize(),
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
                        visible = selectedDropDownMenu == "Monthly"
                    ) {
                        HorizontalCalendar(
                            state = state,
                            reverseLayout = true,
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
                                        it.date.dayOfWeek.toString().take(3).toLowerCase()
                                            .replaceFirstChar {
                                                it.toUpperCase()
                                            }
                                    }
                                )
                            }
                        )
                    }
                }
            }

          Column(
            //  modifier = modifier.animateContentSize()
          ) {
              AnimatedContent(
                  modifier = modifier.align(Alignment.CenterHorizontally),
                  targetState = tasksForDate.isNotEmpty() || eventsForDate.isNotEmpty()
              ) { it ->

                  if (it){
                      LazyColumn(
                          modifier = modifier.padding(
                              horizontal = 16.dp)
                      ) {
                          item {
                              ButtonGroup(
                                  modifier = modifier
                                      .fillMaxWidth()
                                      .padding(
                                          vertical = 8.dp,
                                      ),
                                  overflowIndicator = {}
                              ) {
                                  tabs.forEachIndexed { index, model ->
                                      val isSelected = index == selectedIndex
                                      toggleableItem(
                                          weight = 1f,
                                          checked = isSelected,
                                          onCheckedChange = {
                                              selectedIndex = index
                                              localView.performHapticFeedback(
                                                  HapticFeedbackConstants.CONFIRM
                                              )
                                              scope.launch {
                                                  pageState.animateScrollToPage(
                                                      index
                                                  )
                                              }
                                          },
                                          label = model.title
                                      )
                                  }
                              }
                          }

                          item {
                              HorizontalPager(
                                  state = pageState
                              ) { page ->
                                  when(page){
                                     0-> AnimatedContent(
                                          targetState = tasksForDate.isNotEmpty(),
                                         transitionSpec = {
                                             scaleIn(
                                                 animationSpec = spring(
                                                     dampingRatio = Spring.DampingRatioLowBouncy,
                                                     stiffness = Spring.StiffnessLow
                                                 )
                                             ) togetherWith scaleOut(
                                                 animationSpec = spring(
                                                     dampingRatio = Spring.DampingRatioLowBouncy,
                                                     stiffness = Spring.StiffnessLow
                                                 )
                                             )
                                         }
                                      ) {
                                          if (it){
                                              Column {
                                                  tasksForDate.forEach {
                                                      Tasktile(
                                                          modifier = modifier
                                                              .padding(
                                                                  vertical = 2.dp
                                                              )
                                                              .animateEnterExit(
                                                                  enter = scaleIn(
                                                                      animationSpec = spring(
                                                                          dampingRatio = Spring.DampingRatioMediumBouncy,
                                                                          stiffness = Spring.StiffnessMedium
                                                                      )
                                                                  ),
                                                                  exit = scaleOut(
                                                                      animationSpec = spring(
                                                                          dampingRatio = Spring.DampingRatioMediumBouncy,
                                                                          stiffness = Spring.StiffnessMedium
                                                                      )
                                                                  )
                                                              ),
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
                                                          taskDate = it.taskTime,
                                                          taskIsCompleted = it.isCompleted,
                                                          taskImportance = it.importance,
                                                          taskNotification = it.notification
                                                      )
                                                  }
                                              }
                                          }
                                      }
                                      1-> AnimatedContent(
                                          targetState = eventsForDate.isNotEmpty(),
                                          transitionSpec = {
                                              scaleIn() togetherWith scaleOut()
                                          }
                                      ) {
                                          if (it){
                                              Column {
                                                  eventsForDate.forEach {
                                                      EventTile(
                                                          modifier = modifier.animateEnterExit(
                                                              enter = scaleIn(
                                                                  animationSpec = spring(
                                                                      dampingRatio = Spring.DampingRatioMediumBouncy,
                                                                      stiffness = Spring.StiffnessMedium
                                                                  )
                                                              ),
                                                              exit = scaleOut(
                                                                  animationSpec = spring(
                                                                      dampingRatio = Spring.DampingRatioMediumBouncy,
                                                                      stiffness = Spring.StiffnessMedium
                                                                  )
                                                              )
                                                          ),
                                                          events = it
                                                      )
                                                  }
                                              }
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

