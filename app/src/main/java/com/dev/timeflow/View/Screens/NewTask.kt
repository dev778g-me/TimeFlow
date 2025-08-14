package com.dev.timeflow.View.Screens

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.CalendarViewDay
import androidx.compose.material.icons.rounded.CalendarViewMonth
import androidx.compose.material.icons.rounded.CalendarViewWeek
import androidx.compose.material.icons.rounded.Details
import androidx.compose.material.icons.rounded.FilterList
import androidx.compose.material.icons.rounded.NoteAdd
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback

import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.AsyncImage
import com.dev.timeflow.Data.Model.DropdownModel
import com.dev.timeflow.Data.Model.ImportanceChipModel
import com.dev.timeflow.Viewmodel.EventViewModel
import com.dev.timeflow.R
import com.dev.timeflow.View.Screens.calenderScreen.MonthCalender
import com.dev.timeflow.View.Screens.calenderScreen.WeekCalender
import com.kizitonwose.calendar.compose.WeekCalendar
import com.kizitonwose.calendar.core.WeekDay

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
    var taskName by rememberSaveable { mutableStateOf("") }
    var taskDescription by rememberSaveable { mutableStateOf("") }

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
    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = {
                showBottomSheet = false
            }
        ) {

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
                            imageVector = Icons.Rounded.NoteAdd,
                            tint = MaterialTheme.colorScheme.primary,
                            contentDescription = null
                        )
                    },
                    placeholder = {
                        Text(
                            text = "Enter task name "
                        )
                    },
                    value = taskName,
                    onValueChange = {
                        taskName = it
                    },
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
                            imageVector = Icons.Rounded.Details,
                            tint = MaterialTheme.colorScheme.primary,
                            contentDescription = null
                        )
                    },
                    placeholder = {
                        Text(
                            text = "Enter task description"
                        )
                    },
                    value = taskDescription,
                    onValueChange = {
                        taskDescription = it
                    },
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Done
                    )
                )
                 Spacer(
                     modifier = modifier.height(8.dp)
                 )

               Row (
                   modifier = modifier.fillMaxWidth(),
                   horizontalArrangement = Arrangement.SpaceAround
               ){
                   importanceChip.forEachIndexed {
                           index , chip ->
                       FilterChip(
                           selected = index == selectedChip,
                           onClick = {
                               selectedChip = index
                           },
                           leadingIcon = {
                               Icon(
                                   imageVector = chip.icon,
                                   contentDescription = chip.label,
                                   tint = chip.color,
                                   modifier = modifier.size(FilterChipDefaults.IconSize-4.dp)
                               )
                           },
                           label ={
                               Text(
                                   text = chip.label
                               )
                           }
                       )
                   }
               }
                Button(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(
                            vertical = 8.dp
                        ),
                    onClick = {}
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
                    showBottomSheet = true
                }
            ) {
                Icon(
                    imageVector = Icons.Rounded.Add,
                    contentDescription = null
                )
            }


        }
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

             Spacer(
                 modifier = modifier.weight(1f)
             )

            AsyncImage(
                 modifier = modifier.aspectRatio(1f)
                     .padding(
                        24.dp
                     ),
                model = R.drawable.emptytask ,
                contentDescription = null
            )
        }
    }

}
