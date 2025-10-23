package com.dev.timeflow.View.Screens

import android.icu.util.Calendar
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults

import androidx.compose.material3.ExperimentalMaterial3Api

import androidx.compose.material3.Icon
import androidx.compose.material3.InputChipDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.dev.timeflow.Data.Model.ImportanceChipModel
import com.dev.timeflow.Viewmodel.EventViewModel
import kotlinx.coroutines.delay
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController
) {
  val context = androidx.compose.ui.platform.LocalContext.current.applicationContext

    val today = remember { Date().time }


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



   // future date variable

    var selectedDate by remember { mutableLongStateOf(0L) }
    var percentage by remember { mutableFloatStateOf(0f) }
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
    val taskViewModel : EventViewModel = hiltViewModel()
    val allTask by taskViewModel.allTasks.collectAsState(emptyList())
    val completedTask by remember(allTask) {
        derivedStateOf {
            allTask.filter {
                it.isCompleted
            }
        }
    }

    val inCompletedTask by remember(allTask) {
        derivedStateOf {
            allTask.filter {
                !it.isCompleted
            }
        }
    }

    val lowImportanceTask by remember(allTask) {
        derivedStateOf {
            allTask.filter {
                it.importance == "Low"
            }
        }
    }
    val mediumImportanceTask by remember {
        derivedStateOf {
            allTask.filter {
                it.importance == "Medium"
            }
        }
    }
    val highImportanceTask by remember {
        derivedStateOf {
            allTask.filter {
                it.importance == "High"
            }
        }
    }
    val chipItem = listOf<ImportanceChipModel>(
        ImportanceChipModel(
            label = "Low",
            type = lowImportanceTask.size,
            color = Color(0xFF4CAF50) // Material Green 500
        ),
        ImportanceChipModel(
            label = "Medium",
            type = mediumImportanceTask.size,
            color = Color(0xFFFFC107) // Material Amber 500
        ),
        ImportanceChipModel(
            label = "High",
            type = highImportanceTask.size ,
            color = Color(0xFFF44336) // Material Red 500
        )


    )
   val todayDate = LocalDate.now()
    val formattedDate = todayDate.format(DateTimeFormatter.ofPattern("EEEE, MMM dd", Locale.getDefault()))
    Scaffold(

    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {


            AnimatedVisibility(visible = allTask.isNotEmpty()) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            vertical = 4.dp
                        ),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.inverseOnSurface
                    )
                ) {
                   Column (
                       modifier = Modifier.fillMaxWidth()
                   ){
                       Text(
                           modifier = Modifier.padding(start = 16.dp, top = 12.dp, bottom = 4.dp),
                           text = "Daily Summary",
                           style = MaterialTheme.typography.titleMedium.copy(
                               fontWeight = FontWeight.Bold,
                               color = MaterialTheme.colorScheme.primary
                           )
                       )
                       Text(
                           modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
                           text = "Today is $formattedDate",
                           style = MaterialTheme.typography.bodyMedium.copy(
                               fontWeight = FontWeight.SemiBold,
                               color = MaterialTheme.colorScheme.onPrimaryContainer
                           )
                       )

                       Text(
                           modifier = Modifier.padding(
                               horizontal = 16.dp
                           ),
                           text  = "${completedTask.size} of ${allTask.size} tasks completed",
                       )
                       val progress = (completedTask.size.toFloat() / allTask.size.toFloat()) *100f
                      Box(
                          modifier = Modifier.padding(
                              start = 16.dp,
                              end = 16.dp,
                              top = 4.dp,
                              bottom = 4.dp
                          )
                      ){
                          LinearProgressIndicator(
                              progress = progress / 100f,
                              modifier = Modifier
                                  .fillMaxWidth()
                                  .height(10.dp),
                                  //.padding(horizon),
                              trackColor =  MaterialTheme.colorScheme.onPrimary,
                              color = if (progress== 100f) {
                                  Color(0xFF4CAF50)
                              } else {
                                  MaterialTheme.colorScheme.secondary
                              },
                          )
                      }
                       Row (
                           modifier = Modifier
                               .padding(start = 16.dp, end = 16.dp, bottom = 8.dp),
                           horizontalArrangement = Arrangement.SpaceBetween
                       ){
                           chipItem.reversed().forEachIndexed {
                               index , chip ->
                               AssistChip(
                                   modifier = Modifier.padding(
                                       start = if (index == 0) 0.dp else 4.dp
                                   ),
                                   onClick = {},
                                   label = {
                                       Text(
                                           text = "${chip.type} ${chip.label}"
                                       )
                                   },
                                   leadingIcon = {
                                       Icon(
                                           modifier = Modifier.size(
                                               InputChipDefaults.IconSize -4.dp
                                           ),
                                           imageVector = chip.icon,
                                           tint = chip.color,
                                           contentDescription = null
                                       )
                                   }
                               )
                           }
                       }
                   }

                }

            }

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
            Spacer(modifier = Modifier.weight(1f))

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        bottom = 16.dp
                    ),
                textAlign = TextAlign.End,
                text = "Time is Running ......",
                style = MaterialTheme.typography.titleLarge,
                fontFamily = androidx.compose.ui.text.font.FontFamily.Cursive
            )



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
                .padding(vertical = 4.dp)
                .clip(RoundedCornerShape(16.dp)),
            tonalElevation = 20.dp,
colors = ListItemDefaults.colors(
    containerColor = MaterialTheme.colorScheme.inverseOnSurface
),
            overlineContent = {
                Text(text = heading)
            },
            headlineContent = {
                Text(text = subHeading, modifier = Modifier.padding(bottom = 10.dp))
            }
            , trailingContent = {
                Text(text = "$percentage %")
            }

            , supportingContent = {
                LinearProgressIndicator(
                    modifier = Modifier.fillMaxWidth(),
                    progress = { progress / 100f },
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
    val dateNew = Date(date)
    val format = SimpleDateFormat.getInstance()
    return format.format(dateNew)
}

