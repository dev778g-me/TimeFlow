package com.dev.timeflow.Managers.utils.componets

import android.graphics.pdf.models.ListItem
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Circle
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import androidx.glance.text.TextAlign
import androidx.glance.text.TextDecoration
import com.dev.timeflow.Data.Model.Events
import com.dev.timeflow.Data.Model.Tasks
import com.dev.timeflow.Managers.utils.toLocalDate
import com.dev.timeflow.View.Widget.RoundedCheckBox

@Composable
fun Tasktile(
    modifier: Modifier = Modifier,
    taskName : String,
    taskDescription  : String ?,
    taskCreatedAt : Long,
    taskDate : Long,
    taskIsCompleted : Boolean,
    taskImortance : String,
  //  tasks: Tasks,
    onUpdateTask : (Boolean) -> Unit
) {
    ListItem(
        modifier = modifier
            .padding(
                vertical = 2.dp
            )
            .clip(
                RoundedCornerShape(16.dp)
            )
            .clickable(
                onClick = {}
            ),
        headlineContent = {
            Text(
//                textDecoration = if (tasks.isCompleted) {
//                    androidx.compose.ui.text.style.TextDecoration.LineThrough
//                } else {
//                    androidx.compose.ui.text.style.TextDecoration.None
//                },
                text = taskName
            )
        },

        trailingContent = {
            AssistChip(
                border = AssistChipDefaults.assistChipBorder(
                    enabled = false,
                    disabledBorderColor = Color.Transparent
                ),
                onClick = {},
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Rounded.Circle,
                        tint = when(taskImortance){
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
                        text = taskImortance,
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            )
        },
        supportingContent = {
            Text(
                text = if (taskDescription.isNullOrEmpty()){
                    taskDate.toLocalDate().toString()
                } else {
                    "$taskDescription ${taskDate.toLocalDate().toString()}"
                }
            )
        },
        leadingContent = {
            Checkbox(
                checked = taskIsCompleted,
                onCheckedChange = {
                    onUpdateTask.invoke(it)
                },

            )
        }
    )
}

@Composable
fun EventTile(modifier: Modifier = Modifier,events: Events) {

    ListItem(
        headlineContent = {
            Text(
                text = events.title
            )
        },
        supportingContent = {


        }
    )
}