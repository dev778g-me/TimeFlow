package com.dev.timeflow.View.utils.componets

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.composables.icons.lucide.Bell
import com.composables.icons.lucide.Calendar
import com.composables.icons.lucide.Clock
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.PenLine
import com.composables.icons.lucide.Pin
import com.composables.icons.lucide.Trash
import com.dev.timeflow.Data.Model.Events
import com.dev.timeflow.View.utils.toFormattedTime
import com.dev.timeflow.View.utils.toLocalDate
import kotlinx.coroutines.delay
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import kotlin.time.Duration.Companion.milliseconds

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SheetToEditEvent(
    modifier: Modifier = Modifier,
    fromDatePickerState: DatePickerState,
    toDatePickerState: DatePickerState,
    fromTimePickerState: TimePickerState,
    toTimePickerState: TimePickerState,
    onDismiss: () -> Unit,
    onPin: () -> Unit,
    onValueChange: (String) -> Unit,
    onNameValueChange: (String) -> Unit,
    onStartDateChipClick: () -> Unit,
    onEndDateChipClick: () -> Unit,
    onStartTimeChipClick: () -> Unit,
    onEndTimeChipClick: () -> Unit,
    onUpdateEvent: () -> Unit,
    onDeleteEvent: () -> Unit,
    event: Events
) {
    val formatter = DateTimeFormatter.ofPattern("h:mm a")
    var description by remember(event.id) { mutableStateOf(event.description) }
    var name by remember(event.id) { mutableStateOf(event.name) }
    val baseFontSize = MaterialTheme.typography.headlineSmall.fontSize.value
    val minFontSize = 14.sp.value
    val maxLength = 50

    val fontSize = if (name.length > 15) {
        maxOf(
            minFontSize,
            baseFontSize * (1 - (name.length - 15).toFloat() / maxLength)
        )
    } else {
        baseFontSize
    }

    LaunchedEffect(description) {
        delay(500.milliseconds)
        if (description != event.description) {
            onValueChange(description)
        }
    }

    LaunchedEffect(name) {
        delay(500.milliseconds)
        if (name != event.name) {
            onNameValueChange.invoke(name)
        }
    }

    ModalBottomSheet(
        contentWindowInsets = { WindowInsets(0.dp) },
        sheetState = rememberModalBottomSheetState(
            skipPartiallyExpanded = true
        ),
        onDismissRequest = {
            onDismiss.invoke()
        }
    ) {
        Column(
            modifier = modifier.padding(
                start = 20.dp, end = 20.dp, bottom = 28.dp
            )
        ) {
            // ── Header: Title + Delete ──────────────────────────
            Row(
                modifier = modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextField(
                    maxLines = 3,
                    modifier = modifier.weight(1f),
                    colors = TextFieldDefaults.colors(
                        disabledContainerColor = Color.Transparent,
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                    ),
                    value = name,
                    textStyle = MaterialTheme.typography.headlineSmall.copy(
                        fontSize = fontSize.sp,
                        fontWeight = FontWeight.SemiBold
                    ),
                    onValueChange = {
                        name = it
                    },
                    placeholder = {
                        Text(
                            text = "Event name",
                            style = MaterialTheme.typography.headlineSmall.copy(
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        )
                    }
                )
                Spacer(modifier = modifier.width(8.dp))
                IconButton(
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer,
                        contentColor = MaterialTheme.colorScheme.onErrorContainer
                    ),
                    onClick = {
                        onDeleteEvent()
                        onDismiss.invoke()
                    }
                ) {
                    Icon(
                        modifier = modifier.size(ButtonDefaults.IconSize),
                        imageVector = Lucide.Trash,
                        contentDescription = "Delete event"
                    )
                }
            }

            Spacer(modifier = modifier.height(20.dp))

            // ── Section: Details ────────────────────────────────
            SectionLabel(text = "Details")
            Spacer(modifier = modifier.height(8.dp))

            Surface(
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.surfaceContainerHigh,
                tonalElevation = 1.dp
            ) {
                TextField(
                    colors = TextFieldDefaults.colors(
                        disabledContainerColor = Color.Transparent,
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                    ),
                    modifier = modifier.fillMaxWidth(),
                    value = description,
                    onValueChange = {
                        description = it
                    },
                    placeholder = {
                        Text(text = "Add a description")
                    },
                    label = {
                        Text("Description")
                    },
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            onValueChange.invoke(description)
                        }
                    ),
                    leadingIcon = {
                        Icon(
                            modifier = modifier.size(ButtonDefaults.IconSize),
                            imageVector = Lucide.PenLine,
                            contentDescription = null
                        )
                    }
                )
            }

            Spacer(modifier = modifier.height(20.dp))

            // ── Section: Schedule ───────────────────────────────
            SectionLabel(text = "Schedule")
            Spacer(modifier = modifier.height(8.dp))

            Surface(
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.surfaceContainerHigh,
                tonalElevation = 1.dp
            ) {
                Column(modifier = modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
                    // From row
                    ScheduleRow(
                        label = "From",
                        dateText = fromDatePickerState.selectedDateMillis!!.toLocalDate()
                            .format(DateTimeFormatter.ofPattern("MMM d, yyyy")),
                        timeText = LocalTime.of(
                            fromTimePickerState.hour,
                            fromTimePickerState.minute
                        ).format(formatter),
                        onDateClick = onStartDateChipClick,
                        onTimeClick = onStartTimeChipClick
                    )

                    HorizontalDivider(
                        modifier = modifier.padding(vertical = 8.dp),
                        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                    )

                    // To row
                    ScheduleRow(
                        label = "To",
                        dateText = toDatePickerState.selectedDateMillis!!.toLocalDate()
                            .format(DateTimeFormatter.ofPattern("MMM d, yyyy")),
                        timeText = LocalTime.of(
                            toTimePickerState.hour,
                            toTimePickerState.minute
                        ).format(formatter),
                        onDateClick = onEndDateChipClick,
                        onTimeClick = onEndTimeChipClick
                    )
                }
            }

            Spacer(modifier = modifier.height(20.dp))

            // ── Section: Notification ───────────────────────────
            SectionLabel(text = "Notification")
            Spacer(modifier = modifier.height(8.dp))

            Surface(
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.surfaceContainerHigh,
                tonalElevation = 1.dp
            ) {
                Column {
                    // Reminder row
                    Row(
                        modifier = modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            modifier = modifier.size(20.dp),
                            imageVector = Lucide.Bell,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = modifier.width(14.dp))
                        Text(
                            text = "Reminder",
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = modifier.weight(1f)
                        )
                        Text(
                            text = if (event.notification) {
                                event.eventNotificationTime.toFormattedTime()
                            } else {
                                "Not set"
                            },
                            style = MaterialTheme.typography.bodyMedium,
                            color = if (event.notification) {
                                MaterialTheme.colorScheme.primary
                            } else {
                                MaterialTheme.colorScheme.onSurfaceVariant
                            }
                        )
                    }

                    HorizontalDivider(
                        modifier = modifier.padding(horizontal = 16.dp),
                        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                    )

                    // Pin notification row
                    Row(
                        modifier = modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp))
                            .clickable { onPin() }
                            .padding(horizontal = 16.dp, vertical = 14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            modifier = modifier.size(20.dp),
                            imageVector = Lucide.Pin,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = modifier.width(14.dp))
                        Text(
                            text = "Pin Notification",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }

            Spacer(modifier = modifier.height(24.dp))

            // ── Update Button ───────────────────────────────────
            Button(
                shape = RoundedCornerShape(16.dp),
                modifier = modifier.fillMaxWidth(),
                onClick = {
                    onUpdateEvent.invoke()
                    onDismiss.invoke()
                }
            ) {
                Text(
                    modifier = modifier.padding(vertical = 8.dp),
                    text = "Update Event",
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}

/**
 * Section label for grouping related content.
 */
@Composable
private fun SectionLabel(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.labelLarge.copy(
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.primary
        )
    )
}

/**
 * A reusable row for displaying a date chip and time chip side by side.
 */
@Composable
private fun ScheduleRow(
    modifier: Modifier = Modifier,
    label: String,
    dateText: String,
    timeText: String,
    onDateClick: () -> Unit,
    onTimeClick: () -> Unit
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.width(48.dp)
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            AssistChip(
                onClick = onDateClick,
                label = {
                    Text(
                        text = dateText,
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.labelLarge
                    )
                },
                leadingIcon = {
                    Icon(
                        modifier = Modifier.size(AssistChipDefaults.IconSize),
                        imageVector = Lucide.Calendar,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            )
            AssistChip(
                onClick = onTimeClick,
                label = {
                    Text(
                        text = timeText,
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.labelLarge
                    )
                },
                leadingIcon = {
                    Icon(
                        modifier = Modifier.size(AssistChipDefaults.IconSize),
                        imageVector = Lucide.Clock,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            )
        }
    }
}