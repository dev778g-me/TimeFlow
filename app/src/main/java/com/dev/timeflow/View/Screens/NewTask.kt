package com.dev.timeflow.View.Screens

import android.util.Log
import android.widget.Toast
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.expandIn
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.animation.shrinkOut
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.AbsoluteCutCornerShape
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.AddBox
import androidx.compose.material.icons.rounded.AddCircle
import androidx.compose.material.icons.rounded.AddTask
import androidx.compose.material.icons.rounded.CalendarViewDay
import androidx.compose.material.icons.rounded.CalendarViewMonth
import androidx.compose.material.icons.rounded.CalendarViewWeek
import androidx.compose.material.icons.rounded.Circle
import androidx.compose.material.icons.rounded.Details
import androidx.compose.material.icons.rounded.FilterList
import androidx.compose.material.icons.rounded.NoteAdd
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.FloatingActionButtonMenu
import androidx.compose.material3.FloatingActionButtonMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeFloatingActionButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonColors
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SplitButtonLayout
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.ToggleFloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kizitonwose.calendar.compose.HorizontalCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.core.firstDayOfWeekFromLocale
import java.time.LocalDate
import java.time.YearMonth
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback

import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.toIntRect
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.AsyncImage
import com.composables.icons.lucide.Calendar
import com.composables.icons.lucide.ListTodo
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Notebook
import com.composables.icons.lucide.Plus
import com.composables.icons.lucide.Signature
import com.dev.timeflow.Data.Model.DropdownModel
import com.dev.timeflow.Data.Model.ImportanceChipModel
import com.dev.timeflow.Data.Model.SavingModel
import com.dev.timeflow.Data.Model.Tasks
import com.dev.timeflow.Managers.utils.componets.Tasktile
import com.dev.timeflow.Managers.utils.toMidnight
import com.dev.timeflow.Viewmodel.EventViewModel
import com.dev.timeflow.R
import com.dev.timeflow.View.Screens.calenderScreen.MonthCalender
import com.dev.timeflow.View.Screens.calenderScreen.WeekCalender
import com.dev.timeflow.View.Widget.RoundedCheckBox
import com.kizitonwose.calendar.compose.WeekCalendar
import com.kizitonwose.calendar.core.WeekDay
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

    var calenderChipState by rememberSaveable { mutableStateOf(false) }

    var showDropDownForChoosingCalender by rememberSaveable { mutableStateOf(false) }

    //variable to hold state of the currently selected date
    var currentSelectedDate by rememberSaveable{mutableStateOf(LocalDate.now())}

    // variable to hold state of the bottom sheet
    var showBottomSheet by rememberSaveable { mutableStateOf(false) }
    var expandFloatingActionButton by rememberSaveable { mutableStateOf(false) }
    var taskName by rememberSaveable { mutableStateOf("") }
    var taskDescription by rememberSaveable { mutableStateOf("") }

    val rotation by animateFloatAsState(
        if (expandFloatingActionButton) 90f else 0f
    )

    LaunchedEffect(currentSelectedDate) {
        Log.d("TASKDATE","we just updated the date${currentSelectedDate}")
        taskViewModel.getTasksForADate(
            date = currentSelectedDate
                .atStartOfDay(ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli()
        ).also {
            Log.d("TASKDATE","we just completed the functiom")
        }
    }

    val tasksForDate by taskViewModel.taskForDate.collectAsState(emptyList())
    val importanceChip = listOf<ImportanceChip>(
        ImportanceChip(
            label = "High",
            color = Color.Red.copy(
                alpha = 0.5f
            )
        ),
        ImportanceChip(
            label = "Medium",
            color = Color.Yellow.copy(
                alpha = 0.5f
            )
        ),
        ImportanceChip(
            label = "Low",
            color = Color.Green.copy(
                alpha = 0.5f
            )
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
    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = {
                showBottomSheet = false
            }
        ) {
            Row(
                modifier = modifier.fillMaxWidth()
                    .padding(
                        horizontal = 24.dp
                    )
            ) {

                type.forEachIndexed {
                    index , type ->
                    FilterChip(
                        modifier = modifier.padding(
                           end = 4.dp
                        ),
                        selected = index == selectedSavingType,
                        onClick = {
                            haptics.performHapticFeedback(
                                hapticFeedbackType = HapticFeedbackType.Confirm
                            )
                            selectedSavingType = index
                        },
                        label = {
                            Text(
                                text = type.title
                            )
                        },
                        leadingIcon = {
                            Icon(
                                modifier = modifier.size(FilterChipDefaults.IconSize),
                                imageVector = type.icon,
                                contentDescription = null
                            )
                        }
                    )
                }
            }
            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
            ) {
                OutlinedTextField(
                    modifier = modifier
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    leadingIcon = {
                        Icon(
                            imageVector = Lucide.Signature,
                            //tint = MaterialTheme.colorScheme.primary,
                            contentDescription = null
                        )
                    },
                    placeholder = {
                        Text(
                            text = "Enter a title"
                        )
                    },
                    value = taskName,
                    onValueChange = {
                        taskName = it
                    },
                    maxLines = 1,
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next
                    )
                )
                Spacer(
                    modifier = modifier.height(
                        8.dp
                    )
                )
                OutlinedTextField(
                    modifier = modifier
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    leadingIcon = {
                        Icon(
                            imageVector = Lucide.Notebook,
                           // tint = MaterialTheme.colorScheme.primary,
                            contentDescription = null
                        )
                    },
                    placeholder = {
                        Text(
                            text = "Enter a description"
                        )
                    },
                    value = taskDescription,
                    onValueChange = {
                        taskDescription = it
                    },
                    maxLines = 1,
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            taskViewModel.insertTask(
                                tasks = Tasks(
                                    id = 0,
                                    name = taskName,
                                    description = taskDescription,
                                    importance = importanceChip[selectedChip].label,
                                    createdAt = System.currentTimeMillis().toMidnight()
                                )
                            )
                            showBottomSheet = false
                        }
                    )
                )
                 Spacer(
                     modifier = modifier.height(8.dp)
                 )

               Text(
                   text = "Priority",
                   style = MaterialTheme.typography.labelLarge.copy(
                       fontWeight = FontWeight.SemiBold,
                       color = MaterialTheme.colorScheme.primary
                   )
               )
               Row (
                   modifier = modifier.fillMaxWidth(),
                   horizontalArrangement = Arrangement.Start
               ){
                   importanceChip.forEachIndexed {
                           index , chip ->
                       Row(
                           verticalAlignment = Alignment.CenterVertically
                       ) {
                           RadioButton(
                               colors = RadioButtonDefaults.colors(
                                   selectedColor = chip.color,
                                   unselectedColor = chip.color
                               ),
                               selected = index == selectedChip,
                               onClick = {
                                   selectedChip = index
                               },
//                           text = chip.label
                           )
                           Text(
                               text = chip.label,
                               style = MaterialTheme.typography.labelMedium
                           )
                       }
                   }
               }
                Button(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(
                            vertical = 8.dp
                        ),
                    onClick = {
                        taskViewModel.insertTask(
                            tasks = Tasks(
                                id = 0,
                                name = taskName,
                                description = taskDescription,
                                importance = importanceChip[selectedChip].label,
                                createdAt = System.currentTimeMillis().toMidnight()
                            )
                        )
                        showBottomSheet = false
                    }
                ) {
                    Text(
                        modifier = modifier.padding(
                            vertical = 8.dp
                        ),
                        text = "Save"
                    )
                }
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
            Row(
                modifier = modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Box {
                    FilterChip(
                        modifier = modifier.padding(horizontal = 16.dp),
                        selected = calenderChipState,
                        onClick = {
                            showDropDownForChoosingCalender = ! showDropDownForChoosingCalender

                        },
                        label = {
                            Text(
                                text = selectedDropDownMenu
                            )
                        },

                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Rounded.FilterList,
                                contentDescription = null,
                                modifier = modifier.size(
                                    FilterChipDefaults.IconSize
                                )
                            )
                        }
                    )
                   if (showDropDownForChoosingCalender){
                       DropdownMenu(
                           expanded = showDropDownForChoosingCalender,
                           onDismissRequest = {
                               showDropDownForChoosingCalender = false
                           }
                       ) {

                          dropDownItems.forEach {
                              DropdownMenuItem(
                                  leadingIcon = {
                                      Icon(imageVector = it.icon , contentDescription = null)
                                  },
                                  text = {
                                      Text(it.title)
                                  },
                                  onClick = {
                                      showDropDownForChoosingCalender = false
                                      selectedDropDownMenu = it.title
                                  }
                              )
                          }
                       }
                   }
                }

            }

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
                            Text(
                                modifier = modifier.padding(
                                    horizontal = 16.dp
                                ),
                                text = it.yearMonth.month.toString().toLowerCase().replaceFirstChar {
                                    it.toUpperCase()
                                },
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    )}
                }
            }

            AnimatedContent(
                modifier = modifier.align(Alignment.CenterHorizontally),
                targetState = tasksForDate.isNotEmpty()
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
                                tasks = it
                            )
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
