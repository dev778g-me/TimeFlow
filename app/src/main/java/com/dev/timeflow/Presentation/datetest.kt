//package com.dev.timeflow.Presentation
//
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.padding
//import androidx.compose.material3.Button
//import androidx.compose.material3.DatePicker
//import androidx.compose.material3.DatePickerDialog
//import androidx.compose.material3.ExperimentalMaterial3Api
//import androidx.compose.material3.FilledTonalButton
//import androidx.compose.material3.Text
//import androidx.compose.material3.rememberDatePickerState
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableLongStateOf
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.glance.layout.Column
//import java.text.SimpleDateFormat
//import java.util.Date
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun DateTest(modifier: Modifier = Modifier) {
//    var datePickerController by remember { mutableStateOf(false) }
//    val today = remember { Date().time }
//    val dateState= rememberDatePickerState(
//       initialSelectedDateMillis = today,
//        initialDisplayedMonthMillis = today,
//
//   )
//     var selectedDate by remember { mutableLongStateOf(Date().time) }
//    androidx.compose.foundation.layout.Column(
//        modifier = Modifier.fillMaxSize(),
//        verticalArrangement = Arrangement.Center
//    ) {
//        androidx.compose.foundation.layout.Column(
//            modifier = Modifier.fillMaxSize(),
//            verticalArrangement = Arrangement.Center,
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//            Button(onClick = {
//                datePickerController = true
//            }) {
//                Text(text = "Show Date Picker")
//            }
//
//            if (datePickerController){
//                DatePickerDialog(
//                    onDismissRequest = {datePickerController = false},
//                    confirmButton = {
//                        val selectedmilies = dateState.selectedDateMillis ?: 0L
//                        val isFutureReady = selectedmilies>= today
//                        FilledTonalButton(
//                            enabled = isFutureReady,
//                            onClick = {
//                                if (dateState.selectedDateMillis != null) {
//                                    selectedDate = dateState.selectedDateMillis!!
//                                }
//                                datePickerController = false
//
//                            }
//                        ) {
//                            Text(text = "Confirm")
//                        }
//                    },
//                    dismissButton = {
//                        FilledTonalButton(
//                            onClick = {
//                                datePickerController = false
//                            }
//                        ) {
//                            Text(text = "Cancel")
//                        }
//                    }
//                ) {
//                    DatePicker(
//                        state =dateState,
//                    //    dateV = {selectedDateMillis -> selectedDateMillis >= today}
//                    )
//                }
//            }
//
//            Text(convertLongDate(selectedDate))
//        }
//    }
//}
//
//
