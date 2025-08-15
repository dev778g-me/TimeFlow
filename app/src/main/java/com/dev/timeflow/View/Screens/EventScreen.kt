package com.dev.timeflow.View.Screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.EventBusy
import androidx.compose.material.icons.rounded.AccessTime
import androidx.compose.material.icons.rounded.DriveFileRenameOutline
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeFloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup

import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.AsyncImage
import com.dev.timeflow.Data.Model.Events
import com.dev.timeflow.Viewmodel.EventViewModel
import com.dev.timeflow.R
import com.maxkeppeker.sheets.core.models.base.rememberUseCaseState
import com.maxkeppeler.sheets.calendar.CalendarDialog
import com.maxkeppeler.sheets.calendar.models.CalendarConfig
import com.maxkeppeler.sheets.calendar.models.CalendarSelection
import com.maxkeppeler.sheets.calendar.models.CalendarStyle
import java.time.LocalDate
import java.time.ZoneId
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun EventScreen(
    modifier: Modifier = Modifier,
    onNavigate : () -> Unit
) {


    val eventViewModel: EventViewModel = hiltViewModel()
    var showDate by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()
    val selectedDate = datePickerState.selectedDateMillis
    LaunchedEffect(Unit) {
        eventViewModel.getAllEvents()
    }

    val today = LocalDate.now()
   // val calendarRange = ClosedRange()
    val allEvents by eventViewModel.allEvents.collectAsState(emptyList())
    var showDialog by remember { mutableStateOf(false) }
    var eventName by remember { mutableStateOf("") }
    var eventDescription by remember { mutableStateOf("") }
    var selectedDates = remember { mutableStateOf<LocalDate>(today) }
    val disabledDates = listOf(
        LocalDate.now(),
    )
    fun localDateToMillis(date: LocalDate): Long{
        return date.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButtonMenu(
                modifier = modifier,
                expanded = showDialog,
                button = {
                    FloatingActionButton(
                        onClick = {}
                    ) { }
                }
            ) { }
        }
    ) {
        innerPadding ->
        if (allEvents.isEmpty()) {
            Box(
                modifier = modifier.fillMaxSize()
                    .padding(horizontal = 16.dp),
                contentAlignment = Alignment.Center
            ) {
               Column(
                   horizontalAlignment = Alignment.CenterHorizontally
               ) {
                   AsyncImage(
                       modifier = modifier.padding(
                           24.dp
                       ),
                       contentScale = ContentScale.Fit,
                       model = R.drawable.resource_new,
                       contentDescription = null
                   )

                   Text(
                       text = "Add your first event",
                       style = MaterialTheme.typography.titleMedium,
                       color = MaterialTheme.colorScheme.primary
                   )
               }
            }
        } else {
            LazyColumn(
                modifier = modifier
                    .fillMaxSize()
                    .padding(
                        horizontal = 16.dp
                    )

            ) {
                items(allEvents) {
                    val currentDate = System.currentTimeMillis()
                    val startDate = it.startTime
                    val endDate = it.endTime

                    val difference = endDate - startDate

                    val elapsed = currentDate - startDate
                    val  percentage = (elapsed.toDouble()/ difference.toDouble()) * 100.0.roundToInt()
                    val progress = percentage.toFloat()
                    ProgressBox(
                        progress =progress.roundToInt().toFloat(),
                        heading = it.title,
                        subHeading = it.description,
                        percentage = progress.roundToInt().toFloat(),
                        modifier = modifier.padding(
                            horizontal = 16.dp
                        )
                    )
                }
            }

        }
        if (showDialog){
            ModalBottomSheet(
                onDismissRequest = { showDialog = false}

            ) {
                Column(
                    modifier = modifier
                        .padding(
                            horizontal = 24.dp
                        )
                ){
                    OutlinedTextField(
                        modifier = modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        value = eventName,
                        onValueChange = {
                            eventName = it
                        },
                        placeholder = {
                            Text(
                                text = "Add Event Name"
                            )
                        },
                        label = {
                            Text(
                                text = "Event Name"

                            )
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Rounded.DriveFileRenameOutline,
                                contentDescription = null
                            )
                        },
                        shape = RoundedCornerShape(12.dp),
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Next
                        )

                    )
                    OutlinedTextField(
                        modifier = modifier
                            .fillMaxWidth()
                            .padding(
                                vertical = 4.dp
                            ),
                        value = eventDescription,
                        onValueChange = {
                            eventDescription = it
                        },
                        placeholder = {
                            Text(
                                text = "Add Event Description"
                            )
                        },
                        label = {
                            Text(
                                text = "Event Description"
                            )
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Filled.Description,
                                contentDescription = null
                            )
                        },
                        shape = RoundedCornerShape(12.dp),
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Done
                        )
                    )
                    FilledTonalButton(
                        modifier = modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        onClick = {
                            showDate  = true
                        }
                    ){
                        Icon(
                            modifier = modifier.padding(end = 4.dp),
                            imageVector = Icons.Rounded.AccessTime,
                            contentDescription = null
                        )
                        Text(
                            modifier = modifier.padding(
                                vertical = 8.dp
                            ),
                            text = if(selectedDates.value == today){
                                "Select Date"
                            } else {
                                "Selected Date : ${selectedDates.value}"
                            }
                        )
                    }


                    Button(
                        modifier = modifier
                            .fillMaxWidth()
                            .padding(
                                vertical = 16.dp
                            ),
                        onClick = {
                            eventViewModel.insertEvent(
                                Events(
                                    id = 0,
                                    title = eventName,
                                    description = eventDescription,
                                    startTime = System.currentTimeMillis(),
                                    endTime = localDateToMillis(selectedDates.value)
                                )
                            )
                            showDialog = false
                            eventName = ""
                            eventDescription = ""
                            selectedDates.value = LocalDate.now()
                        }
                    ) {
                        Text(
                            modifier = modifier.padding(
                                vertical = 8.dp
                            ),
                            text = "Save Event"
                        )
                    }
                }
            }
        }

        if (showDate){
            Popup(
                onDismissRequest = {
                    showDate = false
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
                            showDate = false

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
    }
}