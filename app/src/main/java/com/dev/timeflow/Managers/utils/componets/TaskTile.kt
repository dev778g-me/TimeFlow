package com.dev.timeflow.Managers.utils.componets

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Circle
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.dev.timeflow.Data.Model.Events
import com.dev.timeflow.Managers.utils.toHour
import com.dev.timeflow.Managers.utils.toMidnight
import com.dev.timeflow.Managers.utils.toMinute
import com.dev.timeflow.View.Widget.NewCheckBox
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.serialization.descriptors.PrimitiveKind

@Composable
fun TaskTile(
    modifier: Modifier,
    taskName: String,
    taskDescription: String?,
    taskTime: Long,
    taskIsCompleted: Boolean,
    taskImportance: String,
    taskNotification: Boolean,
    onUpdateTask: (Boolean) -> Unit,
    onClick : () -> Unit
) {

    var animate by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val scale by animateFloatAsState(
        targetValue = if (animate) 1.02f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioLowBouncy,
            stiffness = Spring.StiffnessLow
        )
    )
    Column(
        modifier = modifier
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .clip(
                RoundedCornerShape(16.dp)
            )

            .clickable(
                onClick = {
                    onClick.invoke()
                }
            )
            .padding(
                vertical = 8.dp
            ),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            NewCheckBox(
                isSelected = taskIsCompleted
            ) {
                scope.launch{
                    animate = true
                    delay(200)
                    animate = false
                }
                onUpdateTask.invoke(
                    it
                )

            }

            Text(
                modifier = modifier
                    .weight(1f)
                    .padding(
                        end = 4.dp
                    ),
                text = taskName,
                textDecoration = if (taskIsCompleted) TextDecoration.LineThrough else TextDecoration.None,
                fontWeight =  FontWeight.Normal
            )

            Box(
                modifier = modifier.padding(
                    end = 8.dp
                )
            ){
                Row(
                    modifier = modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(
                            when (taskImportance) {
                                "Low" -> Color(0xFF4CAF50).copy(alpha = 0.3f)
                                "Medium" -> Color(0xFFFFC107).copy(alpha = 0.3f)
                                "High" -> Color(0xFFF44336).copy(alpha = 0.3f)
                                else -> Color.Transparent
                            }
                        )
                    ,
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Circle,
                        tint = when (taskImportance) {
                            "Low" -> Color(0xFF4CAF50)
                            "Medium" -> Color(0xFFFFC107)
                            "High" -> Color(0xFFF44336)
                            else -> Color.Transparent
                        },
                        contentDescription = null,
                        modifier = modifier
                            .size(
                                AssistChipDefaults.IconSize - 4.dp
                            )
                            .padding(
                                start = 6.dp
                            )
                    )
                    Text(
                        modifier = modifier.padding(end = 6.dp),
                        text = taskImportance,
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = when (taskImportance) {
                                "Low" -> FontWeight.Light
                                "Medium" -> FontWeight.Medium
                                "High" -> FontWeight.SemiBold
                                else -> FontWeight.Normal
                            }
                        )
                    )
                }
            }

       }


   }

}

