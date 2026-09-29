package com.dev.timeflow.View.utils.componets

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonGroupDefaults
import androidx.compose.material3.AssistChip
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FilledTonalIconToggleButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.ToggleButton
import androidx.compose.material3.ToggleButtonDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.composables.icons.lucide.Bell
import com.composables.icons.lucide.Calendar
import com.composables.icons.lucide.Check
import com.composables.icons.lucide.Clock
import com.composables.icons.lucide.FlagTriangleRight
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.PenLine
import com.composables.icons.lucide.Timer
import com.dev.timeflow.Data.Model.ImportanceChipModel
import com.dev.timeflow.Data.Model.SavingModel
import com.dev.timeflow.View.utils.toUtcDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter


@OptIn(ExperimentalMaterial3ExpressiveApi::class, ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun SheetToAddEventAndTask(
    modifier: Modifier = Modifier,
    onDismiss : () -> Unit,
    onSwitchState : (Boolean) -> Unit,
    onTimeState : (Boolean) -> Unit,
    onPermissionState : (Boolean) -> Unit,
    selectedSavingType : Int,
    isButtonEnabled : Boolean,
    timerState : TimePickerState,
    fromTimePickerState : TimePickerState,
    toTimePickerState: TimePickerState,
    fromDatePickerState: DatePickerState,
    toDatePickerState: DatePickerState,
    onFromTimePicker: () -> Unit,
    onToTimePicker : () -> Unit,
    onTaskSave : () -> Unit,
    onEventSave : () -> Unit,
    onFromTileClick : () -> Unit,
    onToTileClick : () -> Unit,
    onSelectedImportantChipChange : (Int) -> Unit,
    onTaskNameChange : (String) -> Unit,
    onTaskDescriptionChange : (String) -> Unit,
    changeSavingType: (Int) -> Unit,
    savingChipList : List<SavingModel>,
    importanceChip : List<ImportanceChipModel>,
    hapticFeedback: HapticFeedback,
    switchState : Boolean,
    showTimeState : Boolean,
    taskName : String,
    taskDescription : String,
    selectedImportantChip : Int,

) {
    val localContext = LocalContext.current
    val motionScheme = MaterialTheme.motionScheme
    val timeFormatter = remember { DateTimeFormatter.ofPattern("h:mm a") }
    val dateFormatter = remember { DateTimeFormatter.ofPattern("MMM d, yyyy") }
    val fromFormattedTime = remember(fromTimePickerState.hour, fromTimePickerState.minute) {
        LocalTime.of(fromTimePickerState.hour, fromTimePickerState.minute).format(timeFormatter)
    }
    val toFormattedTime = remember(toTimePickerState.hour, toTimePickerState.minute) {
        LocalTime.of(toTimePickerState.hour, toTimePickerState.minute).format(timeFormatter)
    }
    val isEvent = selectedSavingType == 0

    ModalBottomSheet(
        sheetState = rememberModalBottomSheetState(
            skipPartiallyExpanded = true
        ),
        onDismissRequest = {
            onDismiss.invoke()
        },
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
        containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
        tonalElevation = 0.dp,
        dragHandle = {
            BottomSheetDefaults.DragHandle(
                color = MaterialTheme.colorScheme.outlineVariant
            )
        },
        contentWindowInsets = {
            WindowInsets.safeDrawing.only(
                WindowInsetsSides.Bottom + WindowInsetsSides.Horizontal
            )
        }
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .widthIn(max = 640.dp)
                .align(Alignment.CenterHorizontally)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
                .imePadding(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {


            SheetHeader(
                title = if (isEvent) "New event" else "New task",
                isReminderOn = switchState,
                onReminderToggle = { enabled ->
                    onSwitchState.invoke(enabled)
                    if (enabled) {
                        // for a13 and a13 + devices
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            val hasPermission = ContextCompat.checkSelfPermission(
                                localContext,
                                Manifest.permission.POST_NOTIFICATIONS
                            ) == PackageManager.PERMISSION_GRANTED
                            // if permission is denied ‹umm i can show a dialog to navigate to the app info page
                            if (!hasPermission) {
                                onSwitchState.invoke(false)
                                onTimeState.invoke(false)
                                Toast.makeText(
                                    localContext,
                                    "Please grant notification permission to enable reminders",
                                    Toast.LENGTH_LONG
                                ).show()
                                onPermissionState.invoke(true)

                            } else {
                                // if permission is granted we are showwing the picker
                                onTimeState.invoke(
                                    true
                                )
                            }

                        } else {
                            // for devices below a13 <<<
                            onTimeState.invoke(true)
                        }
                    } else {
                        // picker will never show cux we are passsing ffalse
                        onTimeState.invoke(false)
                    }


                    hapticFeedback.performHapticFeedback(
                        hapticFeedbackType = HapticFeedbackType.Confirm
                    )
                }
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(
                    ButtonGroupDefaults.ConnectedSpaceBetween
                )
            ) {
                savingChipList.forEachIndexed { index, type ->
                    val isSelected = index == selectedSavingType
                    ToggleButton(
                        modifier = Modifier
                            .weight(1f)
                            .semantics { role = Role.RadioButton },
                        checked = isSelected,
                        onCheckedChange = {
                            hapticFeedback.performHapticFeedback(
                                hapticFeedbackType = HapticFeedbackType.Confirm
                            )
                            changeSavingType.invoke(index)
                        },
                        shapes = when (index) {
                            0 -> ButtonGroupDefaults.connectedLeadingButtonShapes()
                            savingChipList.lastIndex -> ButtonGroupDefaults.connectedTrailingButtonShapes()
                            else -> ButtonGroupDefaults.connectedMiddleButtonShapes()
                        },
                        colors = ToggleButtonDefaults.toggleButtonColors(
                            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                            contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            checkedContainerColor = MaterialTheme.colorScheme.primary,
                            checkedContentColor = MaterialTheme.colorScheme.onPrimary
                        )
                    ) {
                        Icon(
                            modifier = Modifier.size(ToggleButtonDefaults.IconSize),
                            imageVector = type.icon,
                            contentDescription = null
                        )
                        Spacer(modifier = Modifier.width(ToggleButtonDefaults.IconSpacing))
                        Text(
                            text = type.title,
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = taskName,
                    onValueChange = {
                        onTaskNameChange.invoke(it)
                    },
                    textStyle = MaterialTheme.typography.bodyLarge,
                    placeholder = {
                        Text(
                            text = if (isEvent) "Event name" else "Task name",
                            style = MaterialTheme.typography.bodyLarge,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    },
                    maxLines = 1,
                    shape = RoundedCornerShape(
                        topStart = 20.dp,
                        topEnd = 20.dp,
                        bottomStart = 6.dp,
                        bottomEnd = 6.dp
                    ),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent
                    ),
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next
                    )
                )
                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = taskDescription,
                    onValueChange = {
                        onTaskDescriptionChange.invoke(it)
                    },
                    textStyle = MaterialTheme.typography.bodyMedium,
                    placeholder = {
                        Text(
                            text = "Add a description",
                            style = MaterialTheme.typography.bodyMedium,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    },
                    leadingIcon = {
                        Icon(
                            modifier = Modifier.size(18.dp),
                            imageVector = Lucide.PenLine,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    },
                    maxLines = 1,
                    shape = RoundedCornerShape(
                        topStart = 6.dp,
                        topEnd = 6.dp,
                        bottomStart = 20.dp,
                        bottomEnd = 20.dp
                    ),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent
                    ),
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {

                        }
                    )
                )
            }

            AnimatedContent(
                targetState = isEvent,
                transitionSpec = {
                    (fadeIn(
                        animationSpec = motionScheme.defaultEffectsSpec()
                    ) + scaleIn(
                        initialScale = 0.94f,
                        animationSpec = motionScheme.defaultSpatialSpec()
                    )) togetherWith
                            (fadeOut(
                                animationSpec = motionScheme.fastEffectsSpec()
                            ) + scaleOut(
                                targetScale = 0.94f,
                                animationSpec = motionScheme.fastSpatialSpec()
                            ))
                },
                label = "event_task_content"
            ) { showEventSchedule ->
                if (showEventSchedule) {
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        color = MaterialTheme.colorScheme.surfaceContainer
                    ) {
                        Column {
                            ScheduleRow(
                                label = "Start",
                                dateText = fromDatePickerState.selectedDateMillis
                                    ?.toUtcDate()
                                    ?.format(dateFormatter)
                                    ?: "Select date",
                                timeText = fromFormattedTime,
                                onDateClick = { onFromTileClick.invoke() },
                                onTimeClick = { onFromTimePicker.invoke() }
                            )
                            HorizontalDivider(
                                modifier = Modifier.padding(horizontal = 16.dp),
                                thickness = 2.dp
                               // color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f)
                            )
                            ScheduleRow(
                                label = "End",
                                dateText = toDatePickerState.selectedDateMillis
                                    ?.toUtcDate()
                                    ?.format(dateFormatter)
                                    ?: "Select date",
                                timeText = toFormattedTime,
                                onDateClick = { onToTileClick.invoke() },
                                onTimeClick = { onToTimePicker.invoke() }
                            )
                        }
                    }
                } else {
                    // Task: Priority picker
                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            modifier = Modifier.padding(horizontal = 4.dp),
                            text = "Priority",
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        FlowRow(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            importanceChip.forEachIndexed { index, chip ->
                                val isSelected = selectedImportantChip == index
                                ToggleButton(
                                    checked = isSelected,
                                    onCheckedChange = {
                                        onSelectedImportantChipChange.invoke(index)
                                        hapticFeedback.performHapticFeedback(
                                            hapticFeedbackType = HapticFeedbackType.Confirm
                                        )
                                    },
                                    shapes = ToggleButtonDefaults.shapes(),
                                    colors = ToggleButtonDefaults.toggleButtonColors(
                                        containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                                        contentColor = MaterialTheme.colorScheme.onSurface,
                                        checkedContainerColor = MaterialTheme.colorScheme.tertiaryContainer,
                                        checkedContentColor = MaterialTheme.colorScheme.onTertiaryContainer
                                    )
                                ) {
                                    Icon(
                                        modifier = Modifier.size(ToggleButtonDefaults.IconSize),
                                        imageVector = Lucide.FlagTriangleRight,
                                        contentDescription = null,
                                        tint = if (isSelected) {
                                            MaterialTheme.colorScheme.onTertiaryContainer
                                        } else {
                                            chip.color
                                        }
                                    )
                                    Spacer(modifier = Modifier.width(ToggleButtonDefaults.IconSpacing))
                                    Text(
                                        text = chip.label,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                        style = MaterialTheme.typography.labelLarge
                                    )
                                }
                            }
                        }
                    }
                }
            }

            AnimatedVisibility(
                visible = switchState && !isEvent,
                enter = scaleIn(
                    initialScale = 0.92f,
                    animationSpec = motionScheme.defaultSpatialSpec()
                ) + fadeIn(
                    animationSpec = motionScheme.defaultEffectsSpec()
                ),
                exit = scaleOut(
                    targetScale = 0.92f,
                    animationSpec = motionScheme.fastSpatialSpec()
                ) + fadeOut(
                    animationSpec = motionScheme.fastEffectsSpec()
                )
            ) {
                val reminderTime = remember(timerState.hour, timerState.minute) {
                    LocalTime.of(timerState.hour, timerState.minute)
                        .format(DateTimeFormatter.ofPattern("hh : mm a"))
                }
                FilledTonalButton(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        onTimeState.invoke(!showTimeState)
                    },
                    shapes = ButtonDefaults.shapes(),
                    colors = ButtonDefaults.filledTonalButtonColors(
                        containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                        contentColor = MaterialTheme.colorScheme.onTertiaryContainer
                    )
                ) {
                    Icon(
                        modifier = Modifier.size(ButtonDefaults.IconSize),
                        imageVector = Lucide.Timer,
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.width(ButtonDefaults.IconSpacing))
                    Text(
                        text = reminderTime,
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }


            Button(
                enabled = isButtonEnabled,
                modifier = Modifier.fillMaxWidth(),
                shapes = ButtonDefaults.shapes(),
                onClick = {
                    if (isEvent) {
                        onEventSave.invoke()
                    } else {
                        onTaskSave.invoke()
                    }
                    onDismiss.invoke()
                },
                contentPadding = PaddingValues(
                    vertical = 16.dp,
                    horizontal = 12.dp
                )
            ) {
                Icon(
                    imageVector = Lucide.Check,
                    contentDescription = null
                )
                Spacer(modifier = Modifier.width(ButtonDefaults.IconSpacing))
                Text(
                    fontWeight = FontWeight.SemiBold,
                    text = if (isEvent) "Save event" else "Save task",
                    style = MaterialTheme.typography.titleMedium
                )
            }

            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}


@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun SheetHeader(
    title: String,
    isReminderOn: Boolean,
    onReminderToggle: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier.weight(1f),
            text = title,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
        FilledTonalIconToggleButton(
            modifier = Modifier.size(IconButtonDefaults.smallContainerSize()),
            checked = isReminderOn,
            onCheckedChange = onReminderToggle,
            shapes = IconButtonDefaults.toggleableShapes(),
            colors = IconButtonDefaults.filledTonalIconToggleButtonColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                checkedContainerColor = MaterialTheme.colorScheme.primary,
                checkedContentColor = MaterialTheme.colorScheme.onPrimary
            )
        ) {
            Icon(
                modifier = Modifier.size(20.dp),
                imageVector = Lucide.Bell,
                contentDescription = "Reminder"
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun ScheduleRow(
    label: String,
    dateText: String,
    timeText: String,
    onDateClick: () -> Unit,
    onTimeClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(4.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            AssistChip (
                onClick = onDateClick,
                label = {
                    Text(
                        text = dateText,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                leadingIcon = {
                    Icon(
                        modifier = Modifier.size(18.dp),
                        imageVector = Lucide.Calendar,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
               // shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.width(8.dp))

            SuggestionChip(
                onClick = onTimeClick,
                label = { Text(
                    text = timeText,
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )},
                colors = SuggestionChipDefaults.suggestionChipColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    labelColor = MaterialTheme.colorScheme.onSecondaryContainer,
                    iconContentColor = MaterialTheme.colorScheme.onSecondaryContainer
                ),
                icon = {
                    Icon(
                        modifier = Modifier.size(16.dp),
                        imageVector = Lucide.Clock,
                        contentDescription = null
                    )
                },
               // border = BorderStroke(0.dp, color = Color.Transparent)
            )

        }
    }
}
