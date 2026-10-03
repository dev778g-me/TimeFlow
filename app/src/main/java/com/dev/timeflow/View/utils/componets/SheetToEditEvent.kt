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
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.PenLine
import com.composables.icons.lucide.Pin
import com.composables.icons.lucide.Trash
import com.dev.timeflow.Data.Model.Events
import com.dev.timeflow.View.utils.toFormattedTime
import com.dev.timeflow.View.utils.toHour
import com.dev.timeflow.View.utils.toLocalDate
import com.dev.timeflow.View.utils.toMinute
import com.dev.timeflow.View.utils.toUtcDate
import kotlinx.coroutines.delay
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SheetToEditEvent(
    modifier: Modifier = Modifier,
    fromDatePickerState: DatePickerState,
    toDatePickerState: DatePickerState,
    fromTimePickerState: TimePickerState,
    toTimePickerState: TimePickerState,
    onDismiss: () -> Unit,
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
        delay(500)
        if (description != event.description) {
            onValueChange(description)
        }
    }

    LaunchedEffect(name) {
        delay(500)
        if (name != event.name) {
            onNameValueChange.invoke(name)
        }
    }

    ModalBottomSheet(
        contentWindowInsets = { WindowInsets(0.dp) },
        sheetState = rememberModalBottomSheetState(
            skipPartiallyExpanded = true
        ),
        onDismissRequest = { onDismiss() }
    ) {
        Column(
            modifier = modifier.padding(
                start = 16.dp, end = 16.dp, bottom = 28.dp
            )
        ) {
            // Title + Delete
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
                    onValueChange = { name = it },
                    placeholder = {
                        Text(
                            text = "Event name",
                            style = MaterialTheme.typography.headlineSmall
                        )
                    }
                )
                IconButton(
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer,
                        contentColor = MaterialTheme.colorScheme.onErrorContainer
                    ),
                    onClick = {
                        onDeleteEvent()
                        onDismiss()
                    }
                ) {
                    Icon(
                        modifier = modifier.size(ButtonDefaults.IconSize),
                        imageVector = Lucide.Trash,
                        contentDescription = "Delete"
                    )
                }
            }

            // Description
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
                onValueChange = { description = it },
                placeholder = { Text(text = "Add a description") },
                label = { Text("Description") },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(
                    onDone = { onValueChange(description) }
                ),
                leadingIcon = {
                    Icon(
                        modifier = modifier.size(ButtonDefaults.IconSize),
                        imageVector = Lucide.PenLine,
                        contentDescription = null
                    )
                }
            )

            HorizontalDivider(
                modifier = modifier.padding(vertical = 12.dp),
                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f)
            )

            // From
            Row(
                modifier = modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "From",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    AssistChip(
                        onClick = { onStartDateChipClick() },
                        label = {
                            Text(
                                text = fromDatePickerState.selectedDateMillis
                                    ?.toUtcDate()
                                    ?.format(DateTimeFormatter.ofPattern("MMM d, yyyy"))
                                    ?: "Select date",
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    )
                    TextButton(onClick = { onStartTimeChipClick() }) {
                        Text(
                            text = LocalTime.of(
                                fromTimePickerState.hour,
                                fromTimePickerState.minute
                            ).format(formatter),
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }

            Spacer(modifier = modifier.height(4.dp))

            // To
            Row(
                modifier = modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "To",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    AssistChip(
                        onClick = { onEndDateChipClick() },
                        label = {
                            Text(
                                text = toDatePickerState.selectedDateMillis
                                    ?.toUtcDate()
                                    ?.format(DateTimeFormatter.ofPattern("MMM d, yyyy"))
                                    ?: "Select date",
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    )
                    TextButton(onClick = { onEndTimeChipClick() }) {
                        Text(
                            text = LocalTime.of(
                                toTimePickerState.hour,
                                toTimePickerState.minute
                            ).format(formatter),
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }

            HorizontalDivider(
                modifier = modifier.padding(vertical = 12.dp),
                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f)
            )

            // Notification
            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    modifier = modifier.size(20.dp),
                    imageVector = Lucide.Bell,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = modifier.width(12.dp))
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
                    color = if (event.notification)
                        MaterialTheme.colorScheme.primary
                    else
                        MaterialTheme.colorScheme.onSurfaceVariant
                )
            }



            Spacer(modifier = modifier.height(16.dp))

            val isFormValid = name.isNotBlank()
                    && fromDatePickerState.selectedDateMillis != null
                    && toDatePickerState.selectedDateMillis != null

            val hasUnsavedChanges = name != event.name
                    || description != event.description
                    || fromDatePickerState.selectedDateMillis?.toUtcDate() != event.eventStartTime.toLocalDate()
                    || toDatePickerState.selectedDateMillis?.toUtcDate() != event.eventEndTime.toLocalDate()
                    || LocalTime.of(fromTimePickerState.hour, fromTimePickerState.minute) != LocalTime.of(event.eventStartTime.toHour(), event.eventStartTime.toMinute())
                    || LocalTime.of(toTimePickerState.hour, toTimePickerState.minute) != LocalTime.of(event.eventEndTime.toHour(), event.eventEndTime.toMinute())

            Button(
                enabled = isFormValid && hasUnsavedChanges,
                shape = RoundedCornerShape(12.dp),
                modifier = modifier.fillMaxWidth(),
                onClick = {
                    onUpdateEvent()
                    onDismiss()
                }
            ) {
                Text(
                    modifier = modifier.padding(vertical = 8.dp),
                    text = "Update Event"
                )
            }
        }
    }
}