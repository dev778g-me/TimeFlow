package com.dev.timeflow.Presentation.Screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.AddBox
import androidx.compose.material.icons.rounded.ArrowDropDown
import androidx.compose.material.icons.rounded.Attachment
import androidx.compose.material.icons.rounded.Circle
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.Notifications
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
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
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
    val hapticFeedback = LocalHapticFeedback.current
    var showDialog by remember { mutableStateOf(false) }
    var expand by remember { mutableStateOf(false) }
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
   var isNotification by remember { mutableStateOf(false) }
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

                    ListItem(
                        modifier = modifier.clip(
                            RoundedCornerShape(16.dp)
                        ),
                        colors = ListItemDefaults.colors(
                            containerColor = Color.Transparent
                        ),
                        tonalElevation = 10.dp,
                        headlineContent = {
                            Text("Notification")
                        },
                        leadingContent = {
                            Icon(
                                imageVector = Icons.Rounded.Notifications,
                                contentDescription = null
                            )
                        },
                        trailingContent = {
                            Switch(
                                checked = isNotification,
                                onCheckedChange = {
                                      isNotification = it
                                    hapticFeedback.performHapticFeedback(
                                        HapticFeedbackType.Confirm
                                    )
                                }
                            )
                        }
                    )
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
                                    notification = isNotification,
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
            if (it) {
                Column(
                    modifier = modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    AsyncImage(
                        modifier = modifier
                            .aspectRatio(1f)
                            .padding(34.dp),
                        model = R.drawable.emptytask,
                        contentScale = ContentScale.Inside,
                        contentDescription = null
                    )

                    Text(
                        text = "Add tasks ",
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            } else {
                Column(
                    modifier = modifier
                        .fillMaxSize()
                        .verticalScroll(state = rememberScrollState())
                ) {
                    AnimatedContent(
                        targetState = notCompleted.isEmpty()
                    ) {
                        if (it){
                            CompletedAllTaskCard()
                        }else{
                            NotCompletedTaskCard(
                                tasks = notCompleted,
                                taskViewModel = taskViewModel
                            )
                        }

                    }
                    CompletedTask(
                        completedTasks = completedTask,
                        expand = expand,
                        onExpand = {
                            expand = !expand
                        },
                        taskViewModel = taskViewModel
                    )
                }
            }
        }

    }
}

data class ImportanceChip(
    val label : String,
    val color: Color,
    val type : Int = 0,
    val icon : ImageVector = Icons.Filled.Circle
)


@Composable
fun CompletedAllTaskCard(modifier: Modifier = Modifier,) {
     Card (
         modifier = modifier.padding(
             horizontal = 16.dp
         ),
         colors = CardDefaults.cardColors(
             containerColor = MaterialTheme.colorScheme.inverseOnSurface
         )
     ){
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            AsyncImage(
                modifier = modifier
                    .aspectRatio(1f)
                    .padding(
                        24.dp
                    ),
                model = R.drawable.alltaskdone,
                contentDescription = null
            )
            Text(
                text = "All task are Completed",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                modifier = modifier.padding(
                    bottom = 16.dp
                ),
                text = "Well Done !",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }
     }
}


@Composable
fun NotCompletedTaskCard(modifier: Modifier = Modifier, tasks: List<Tasks>, taskViewModel: EventViewModel) {

    Column(
        modifier = modifier
            .padding(
                horizontal = 16.dp,
            )
            .clip(RoundedCornerShape(24.dp))
            .background(
                MaterialTheme.colorScheme.inverseOnSurface
            ),

    ) {
        tasks.forEach{
            incompleteTask ->
            ListItem(
                colors = ListItemDefaults.colors(
                    containerColor = MaterialTheme.colorScheme.inverseOnSurface
                ),
               tonalElevation = 10.dp,
                modifier = modifier
                    .padding(
                        vertical = 4.dp,
                        horizontal = 8.dp
                    )
                    .clip(
                        RoundedCornerShape(16.dp)
                    ),
               trailingContent = {
                   AssistChip(
                       onClick = {},
                       leadingIcon = {
                           Icon(
                               imageVector = Icons.Rounded.Circle,
                               tint = when(incompleteTask.importance){
                                   "Low" -> Color(0xFF4CAF50)
                                   "Medium" ->  Color(0xFFFFC107)
                                   "High" ->Color(0xFFF44336)
                                   else -> Color.Transparent
                               },
                               contentDescription = null,
                               modifier = modifier.size(
                                   AssistChipDefaults.IconSize -8.dp
                               )
                           )
                       },
                       label = {
                           Text(
                               text = incompleteTask.importance,
                               style = MaterialTheme.typography.labelSmall
                           )
                       }
                   )
               },
                headlineContent = {
                    Row(
                        modifier = modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = incompleteTask.name
                        )

                    }
                },
                supportingContent = {
                    Text(
                        text = if (incompleteTask.description.isNullOrBlank()) "25-06-2025" else "${incompleteTask.description} 25-06-2025"
                    )
                },
                leadingContent = {
                    RoundedCheckBox(
                        isChecked = incompleteTask.isCompleted,
                        onCheckedChange = {
                            taskViewModel.updateTask(
                                tasks = incompleteTask.copy(
                                    isCompleted = it
                                )
                            )
                        }
                    )
                }
            )
        }
    }

}

@Composable
fun CompletedTask(
    modifier: Modifier = Modifier,
    completedTasks: List<Tasks>,
    expand : Boolean,
    onExpand : () -> Unit,
    taskViewModel : EventViewModel
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                horizontal = 16.dp,
                vertical = 8.dp
            ),
        colors = CardDefaults.cardColors(
            containerColor =  MaterialTheme.colorScheme.inverseOnSurface
        )
    ) {
        Column(
            modifier = modifier.fillMaxWidth()

        ) {
            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Completed Tasks (${completedTasks.size})"
                )
                IconButton(
                    onClick = {
                        onExpand.invoke()
                    }
                ) {
                    Icon(imageVector = Icons.Rounded.ArrowDropDown, contentDescription = null)
                }
            }
            AnimatedContent(
                targetState = expand
            ) {
                if (it){
                    Column(
                        modifier = modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(
                                MaterialTheme.colorScheme.inverseOnSurface
                            ),
                    ) {
                        completedTasks.forEach { completeTask ->

                            AnimatedVisibility(
                                visible = true,
                                enter = fadeIn(tween(300)) + expandVertically(tween(300)),
                                exit = fadeOut(tween(200)) + shrinkVertically(tween(200))
                            ) {
                                ListItem(
                                    colors = ListItemDefaults.colors(
                                        containerColor = MaterialTheme.colorScheme.inverseOnSurface
                                    ),
                                    tonalElevation = 10.dp,
                                    modifier = modifier
                                        .padding(vertical = 4.dp, horizontal = 8.dp)
                                        .clip(RoundedCornerShape(16.dp)),
                                    trailingContent = {
                                        AssistChip(
                                            onClick = {},
                                            leadingIcon = {
                                                Icon(
                                                    imageVector = Icons.Rounded.Circle,
                                                    tint = when (completeTask.importance) {
                                                        "Low" -> Color(0xFF4CAF50)
                                                        "Medium" -> Color(0xFFFFC107)
                                                        "High" -> Color(0xFFF44336)
                                                        else -> Color.Transparent
                                                    },
                                                    contentDescription = null,
                                                    modifier = modifier.size(
                                                        AssistChipDefaults.IconSize - 8.dp
                                                    )
                                                )
                                            },
                                            label = {
                                                Text(
                                                    text = completeTask.importance,
                                                    style = MaterialTheme.typography.labelSmall
                                                )
                                            }
                                        )
                                    },
                                    headlineContent = {
                                        Row(
                                            modifier = modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Text(text = completeTask.name)
                                        }
                                    },
                                    supportingContent = {
                                        Text(
                                            text = if (completeTask.description.isNullOrBlank())
                                                "25-06-2025"
                                            else "${completeTask.description} 25-06-2025"
                                        )
                                    },
                                    leadingContent = {
                                        RoundedCheckBox(
                                            isChecked = completeTask.isCompleted,
                                            onCheckedChange = {
                                                taskViewModel.updateTask(
                                                    tasks = completeTask.copy(isCompleted = it)
                                                )
                                            }
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