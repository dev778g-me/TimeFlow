package com.dev.timeflow.Presentation.Screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.AddBox
import androidx.compose.material.icons.rounded.ArrowDropDown
import androidx.compose.material.icons.rounded.Attachment
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.Remove
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.InputChip
import androidx.compose.material3.InputChipDefaults
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.view.HapticFeedbackConstantsCompat
import androidx.glance.text.FontWeight
import androidx.hilt.navigation.compose.hiltViewModel
import app.rive.runtime.kotlin.RiveAnimationView
import coil3.compose.AsyncImage
import com.dev.timeflow.Data.Model.Tasks
import com.dev.timeflow.Presentation.Viewmodel.EventViewModel
import com.dev.timeflow.Presentation.Widget.RoundedCheckBox
import com.dev.timeflow.R
import java.text.SimpleDateFormat
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun TaskScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val taskViewModel: EventViewModel = hiltViewModel()
    LaunchedEffect(Unit){
        taskViewModel.getAllTasks()
    }
    val hapticFeedback = LocalHapticFeedback.current
    var showDialog by remember { mutableStateOf(false) }
    var showMe by remember { mutableStateOf(false) }
    var taskName by remember { mutableStateOf("") }
    var taskDescription by remember { mutableStateOf("") }
    var isNotEmpty by remember(taskName) {mutableStateOf(taskName.isNotEmpty()) }
    var showCompleted by remember { mutableStateOf(false) }

    val chipItem = listOf<ImportanceChip>(
        ImportanceChip(
            label = "Low",
            color = Color(0xFF4CAF50) // Material Green 500
        ),
        ImportanceChip(
            label = "Medium",
            color = Color(0xFFFFC107) // Material Amber 500
        ),
        ImportanceChip(
            label = "High",
            color = Color(0xFFF44336) // Material Red 500
        )


    )
    var selectedChip by remember { mutableStateOf(0) }

    val allTask by taskViewModel.allTasks.collectAsState(emptyList())
    val completedTask by remember(allTask) {
       derivedStateOf {
           allTask.filter {
               it.isCompleted
           }
       }
    }
    val notCompleted by remember(allTask) {
        derivedStateOf {
            allTask.filter {
                !it.isCompleted
            }
        }
    }
    fun getDateFromLong(long: Long): String{
       val sdf =SimpleDateFormat("dd-MM-yyyy", java.util.Locale.getDefault())
        return sdf.format(Date(long))
    }
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    showDialog = true
                }
            ) {
                Icon(
                    imageVector = Icons.Rounded.Add,
                    contentDescription = null
                )
            }
        }
    ) { innerPadding ->
        if (showDialog) {
            ModalBottomSheet(
                onDismissRequest = {
                    showDialog = false
                }
            ) {
                Column(
                    modifier = modifier.padding(
                        horizontal = 24.dp
                    )
                ) {
                    OutlinedTextField(
                        modifier = modifier
                            .fillMaxWidth()
                            .padding(
                                vertical = 4.dp
                            ),
                        value = taskName,

                        onValueChange = {
                            taskName = it
                        },
                        placeholder = {
                            Text(
                                text = "Enter Task Name"
                            )
                        },
                        label = {
                            Text(
                                text = "Enter Task"
                            )
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Rounded.AddBox,
                                contentDescription = null
                            )
                        },
                        shape = RoundedCornerShape(12.dp)
                    )
                    OutlinedTextField(
                        modifier = modifier.fillMaxWidth(),
                        value = taskDescription,
                        onValueChange = {
                            taskDescription = it
                        },
                        placeholder = {
                            Text(
                                text = "Enter Task Description"
                            )
                        },
                        label = {
                            Text(
                                text = "Enter Task Description"
                            )
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Rounded.Attachment,
                                contentDescription = null
                            )
                        },
                        shape = RoundedCornerShape(12.dp)
                    )
                    Row(
                        modifier = modifier.fillMaxWidth(),

                    ) {
                        chipItem.forEachIndexed { index, importance ->
                            InputChip(
                                modifier = modifier.padding(
                                    horizontal = 4.dp
                                ),
                                selected = selectedChip == index,
                                label = {
                                    Text(
                                        text = importance.label,
                                        style = MaterialTheme.typography.labelMedium
                                    )
                                },
                                onClick = {
                                    selectedChip = index
                                    hapticFeedback.performHapticFeedback(
                                        hapticFeedbackType = HapticFeedbackType.Confirm
                                    )
                                },
                                leadingIcon = {
                                    Icon(
                                        modifier = modifier.size(
                                            InputChipDefaults.IconSize
                                        ),
                                        imageVector = importance.icon,
                                        tint = importance.color,
                                        contentDescription = null
                                    )
                                }
                            )
                        }
                    }
                    Button(
                        enabled = isNotEmpty,
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
                                    importance = chipItem[selectedChip].label,
                                    isCompleted = false,
                                    createdAt = System.currentTimeMillis(),
                                )
                            )
                            showDialog = false
                            taskName = ""
                            taskDescription = ""
                        }
                    ) {
                        Text(
                            modifier = modifier.padding(
                                vertical = 8.dp
                            ),
                            text = "Add Task"
                        )
                    }
                }
            }
        }
        AnimatedContent(
            targetState = allTask.isEmpty()
        ) {
            if (it == true){
                Box(
                    modifier = modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ){
                    Column(
                        modifier = modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        AsyncImage(
                            modifier = modifier.size(
                                250.dp
                            ),
                            model = R.drawable.emptytask,
                            contentDescription = null
                        )
                        Text(
                            modifier = modifier.padding(
                                vertical = 4.dp
                            ),
                            text = "No tasks yet. Let's get productive!",
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
                   item {
                       AnimatedContent(
                           targetState = notCompleted.isEmpty()
                       ) {
                           if (it == true){
                               Card(
                                   colors = CardDefaults.cardColors(
                                       containerColor = MaterialTheme.colorScheme.inverseOnSurface
                                   )
                               ) {
                                   Column(
                                       horizontalAlignment = Alignment.CenterHorizontally
                                   ) {
                                       AsyncImage(
                                           modifier = modifier.padding(24.dp),
                                           model = R.drawable.chk,
                                           contentDescription = null
                                       )

                                       Text(
                                           modifier = modifier.padding(
                                               top = 16.dp,
                                               bottom = 8.dp
                                           ),
                                           text = "You're All Caught Up!",
                                           fontWeight = androidx.compose.ui.text.font.FontWeight.SemiBold
                                       )

                                       Text(
                                           modifier = modifier.padding(
                                               bottom = 16.dp
                                           ),
                                           text = "Fantastic job staying on track!"
                                       )
                                   }

                               }
                           }
                           else{
                               Spacer(
                                   modifier = modifier.height(1.dp)
                               )
                           }


                       }
                   }
                    items(
                        items = notCompleted,

                    ) {
                        ListItem(
                            tonalElevation = 10.dp,
                            headlineContent = {
                                Text(
                                    text = it.name
                                )
                            }
                        )
                    }
                    item {
                        Card(
                            modifier = modifier
                                .fillMaxWidth()
                                .padding(
                                    vertical = 8.dp
                                ),

                            ) {
                            Column (
                                modifier = modifier
                            ){
                                Row(
                                    modifier = modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        modifier = modifier.padding(
                                            vertical = 8.dp,
                                            horizontal = 16.dp
                                        ),
                                        text = "Completed Tasks (${completedTask.size})"
                                    )
                                    Spacer(
                                        modifier = modifier.weight(1f)
                                    )
                                    IconButton(
                                        onClick = {
                                            showCompleted = !showCompleted
                                        }
                                    ) {
                                        Icon(
                                            imageVector = Icons.Rounded.KeyboardArrowDown,
                                            contentDescription = null
                                        )
                                    }
                                }
                                AnimatedVisibility(
                                    visible = showCompleted
                                ) {
                                    Column(
                                    ) {
                                        completedTask.forEachIndexed {
                                                index , comTask ->
                                            ListItem(
                                                leadingContent = {
                                                    RoundedCheckBox(
                                                        isChecked = comTask.isCompleted,
                                                        onCheckedChange = {
                                                            taskViewModel.updateTask(
                                                                comTask.copy(
                                                                    isCompleted = it
                                                                )
                                                            )
                                                        }
                                                    )

                                                },
                                                tonalElevation = 10.dp,
                                                headlineContent = {
                                                    Text(
                                                        text = comTask.name
                                                    )
                                                }
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

    }
}

data class ImportanceChip(
    val label : String,
    val color: Color,
    val icon : ImageVector = Icons.Filled.Circle
)