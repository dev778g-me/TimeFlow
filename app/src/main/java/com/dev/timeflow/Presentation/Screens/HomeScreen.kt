package com.dev.timeflow.Presentation.Screens

import android.icu.util.Calendar
import android.os.SystemClock
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.glance.LocalContext
import com.dev.timeflow.Managers.WidgetAlarmService
import kotlinx.coroutines.delay
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(modifier: Modifier = Modifier) {
  //  val context = LocalContext.current




    var datePickerController by remember { mutableStateOf(false) }
    val today = remember { Date().time }
    val dateState= rememberDatePickerState(
        initialSelectedDateMillis = today,
        initialDisplayedMonthMillis = today,

        )

    val calender = Calendar.getInstance()
    val decimalFormatter = DecimalFormat("#.##")


    var time by remember { mutableStateOf("") }
    var dayProgress by remember {
        mutableStateOf(0f)
    }
    val formateddayProgress = decimalFormatter.format(dayProgress)

    var dayOfWeek = calender.get(Calendar.DAY_OF_WEEK)
    val dayOfMonth = calender.get(Calendar.DAY_OF_MONTH)
    val dayOfYear = calender.get(Calendar.DAY_OF_YEAR)

    val totalDaysInMonth = calender.getActualMaximum(Calendar.DAY_OF_MONTH)
    val totalDaysInYear = calender.getActualMaximum(Calendar.DAY_OF_YEAR)

     //week percent
    val weekPercentage = (dayOfWeek.toFloat() / 7.0f) * 100
    val weekPercentageFormat = decimalFormatter.format(weekPercentage)
    // month percent
    val monthPercentage = (dayOfMonth.toFloat() / totalDaysInMonth.toFloat()) * 100
    val formatedMonthPercentage = decimalFormatter.format(monthPercentage)
     //year percent
    val yearlyPercentage = (dayOfYear.toFloat() / totalDaysInYear.toFloat()) * 100
    val formatedYearlyPercentage = decimalFormatter.format(yearlyPercentage)

    //names
    val yearName = calender.get(Calendar.YEAR)
    //Month NAme
    val monthDate = SimpleDateFormat("MMMM")
    val monthName = monthDate.format(calender.time)
    //Day Name
    val dayDate = SimpleDateFormat("EEEE")
    val days = dayDate.format(calender.time)

    // custom date
    val nowMilis = System.currentTimeMillis()
    val selectedCalendar = Calendar.getInstance().apply {
        timeInMillis = nowMilis
    }
    val currentCalendar = Calendar.getInstance()
   // future date variable
    var currentDate = System.currentTimeMillis()
    var selectedDate by remember { mutableLongStateOf(0L) }
    var percentage by remember { mutableStateOf(0f) }
    if (selectedDate > 0) {
        val totalMillisToFuture = selectedDate - today
        val elapsedMillis = today - System.currentTimeMillis()
        percentage = (elapsedMillis.toFloat() / totalMillisToFuture) * 100
    }





    LaunchedEffect(Unit) {
        while (true) {
            calender.timeInMillis = System.currentTimeMillis()
            val currentHour = calender.get(Calendar.HOUR_OF_DAY)
            val currentMin = calender.get(Calendar.MINUTE)
            val currentSecond = calender.get(Calendar.SECOND)

            val totalSecondsInDay = 24 * 60 * 60
            val elapsedSeconds = (currentHour * 3600) + (currentMin * 60)
            dayProgress = (elapsedSeconds.toFloat() /totalSecondsInDay * 100)

            val simpleDateFormat = SimpleDateFormat("hh:mm:ss a")
           time = simpleDateFormat.format(calender.time)
            delay(1000L)
        }
    }
   // val context = LocalContext.current
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = "TimeFlow")
                }
            )
        },

        floatingActionButton = {
            FloatingActionButton(onClick = {
             datePickerController = true
            }) {
                Icon(imageVector = Icons.Default.DateRange, contentDescription = null)
            }
        }
    ){ innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            Text(text = selectedDate.toString())
            Text(text = currentDate.toString())
            Text(text = percentage.toString())
            //Text(text = futureProgress.toString())
//            Text(text = decimalFormatter.format(datePercentage))
//           Text(text = currentdate.toString())
//            Text(text = selectedDate.toString())

            ProgressBox(
                progress = dayProgress.toFloat(),"Day Progress",
                time.toString()
                ,formateddayProgress.toFloat()
            )
            ProgressBox(
                progress = weekPercentage.toFloat(),"WeekProgress",days,weekPercentageFormat.toFloat()
            )
            ProgressBox(
                progress = monthPercentage.toFloat(), "Month Progress",monthName.toString(),formatedMonthPercentage.toFloat()
            )

            ProgressBox(
                progress = yearlyPercentage.toFloat(),"Year Progress",yearName.toString(),formatedYearlyPercentage.toFloat()
            )
//            if (datePercentage.isFinite() && datePercentage > 0){
//                ProgressBox(
//                    progress = datePercentage.toFloat(),
//                    "date",
//                    "iuf",
//                    9f
//                )
//            }



            if (datePickerController){
                DatePickerDialog(
                    onDismissRequest = {
                        datePickerController = false
                    },
                    confirmButton = {
                        val selectedMilies = dateState.selectedDateMillis ?: 0L
                        val isFutureReady = selectedMilies>= today
                        FilledTonalButton(onClick = {
                            if (dateState.selectedDateMillis != null) {
                                selectedDate = dateState.selectedDateMillis!!
                            }
                            datePickerController = false
                        }, enabled = isFutureReady) {
                            Text(text = "Confirm Date")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = {
                            datePickerController = false
                        }) {
                            Text(text = "Cancel")
                        }
                    }
                ) {
                    DatePicker(
                        state = dateState
                    )
                }
            }
        }


    }
}


@Composable
fun ProgressBox(progress: Float,heading : String,subHeading: String,percentage: Float ,modifier: Modifier = Modifier) {

    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        ListItem(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp)
                .clip(RoundedCornerShape(20.dp)),
            tonalElevation = 10.dp,

            overlineContent = {
                Text(text = heading)
            },
            headlineContent = {
                Text(text = subHeading, modifier = Modifier.padding(bottom = 10.dp))
            }
            , trailingContent = {
                Text(text = "${percentage} %")
            }

            , supportingContent = {
                LinearProgressIndicator(
                    modifier = Modifier.fillMaxWidth(),
                    progress = progress/100,
                    trackColor = if (progress>= 90f) {
                        Color.Red
                    } else {
                        MaterialTheme.colorScheme.onPrimary
                    },
                    color = if (progress>= 90f) {
                        Color.Red
                    } else {
                        MaterialTheme.colorScheme.secondary
                    },
                )
            }
        )
    }

}
private fun convertLongDate(date: Long) : String{
    val datenew = Date(date)
    val format = SimpleDateFormat.getInstance()
    return format.format(datenew)
}