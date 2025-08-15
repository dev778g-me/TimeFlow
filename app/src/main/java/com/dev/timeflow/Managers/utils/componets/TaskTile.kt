package com.dev.timeflow.Managers.utils.componets

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Circle
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.glance.text.TextAlign
import com.dev.timeflow.Data.Model.Tasks
import com.dev.timeflow.Managers.utils.toLocalDate
import com.dev.timeflow.View.Widget.RoundedCheckBox

@Composable
fun Tasktile(modifier: Modifier = Modifier, tasks: Tasks) {
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
                text = tasks.name
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
                        tint = when(tasks.importance){
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
                        text = tasks.importance,
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            )
        },
        supportingContent = {
            Text(
                text = if (tasks.description.isNullOrEmpty()){
                    tasks.createdAt.toLocalDate().toString()
                } else {
                    "${tasks.description } ${tasks.createdAt.toLocalDate().toString()}"
                }
            )
        },
        leadingContent = {
            RoundedCheckBox(
                isChecked = tasks.isCompleted,
                onCheckedChange = {
                        value ->
//                    taskViewModel.updateTask(
//                        tasks = it.copy(
//                            isCompleted = value
//                        )
//                    )
                }
            )
        }
    )
}