package com.dev.timeflow.View.utils.componets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AssistChip
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.composables.icons.lucide.Bell
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.PenLine
import com.composables.icons.lucide.Trash
import com.dev.timeflow.Data.Model.Events
import com.dev.timeflow.View.utils.toHour
import com.dev.timeflow.View.utils.toLocalDate
import com.dev.timeflow.View.utils.toMinute
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
    onDismiss :() -> Unit,
    onValueChange : (String) -> Unit,
    onNameValueChange : (String) -> Unit,
    onStartDateChipClick : () -> Unit,
    onEndDateChipClick : () -> Unit,
    onStartTimeChipClick : () -> Unit,
    onEndTimeChipClick : () -> Unit,
    onUpdateEvent : () -> Unit,
    onDeleteEvent : () -> Unit,
    event: Events
) {
    val formatter = DateTimeFormatter.ofPattern("hh:mm a")
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
        sheetState = rememberModalBottomSheetState (
            skipPartiallyExpanded = true
        ),
        onDismissRequest = {
            onDismiss.invoke()
        }
    ) {
        Column(
            modifier = modifier.padding(
                start = 16.dp, end = 16.dp, bottom = 26.dp

            )
        ) {
            Row(modifier = modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {

                TextField(
                    maxLines = 5,
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
                        fontSize = fontSize.sp
                    ),
                    onValueChange = {
                        name = it
                    }
                )
//                Spacer(
//                    modifier = modifier.weight(1f)
//                )
                IconButton(
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = MaterialTheme.colorScheme.error,
                        contentColor = MaterialTheme.colorScheme.onError
                    ),
                    onClick = {
                        onDeleteEvent()
                        onDismiss.invoke()
                    }
                ) {
                    Icon(
                        modifier = modifier.size(
                            ButtonDefaults.IconSize
                        ),
                        imageVector = Lucide.Trash,
                        contentDescription = null
                    )
                }
            }
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
                    event.description.ifEmpty { "Enter description" }.let { Text(text = it) }
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
                        contentDescription = ""
                    )
                }

            )

            ListItem(
                modifier = modifier
                    .padding(vertical = 8.dp)
                    .clip(RoundedCornerShape(12.dp)),
                tonalElevation = 10.dp,
                overlineContent = {
                    Text("notification time")
                },
                leadingContent = {
                    Icon(
                        modifier = modifier.size(ButtonDefaults.IconSize),
                        imageVector = Lucide.Bell,
                        contentDescription = ""
                    )
                }, headlineContent = {
                    if (event.notification){
                        Text(
                            text = "${event.eventNotificationTime?.toHour()}:${event.eventNotificationTime?.toMinute()}"
                        )
                    } else {
                        Text(
                            text = "no notification"
                        )
                    }
                })
            ListItem(
                colors = ListItemDefaults.colors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainer
                ),
                modifier = modifier.clip(RoundedCornerShape(12.dp)),
                overlineContent = {
                    Text(
                        text = "From"
                    )
                },
                headlineContent = {
                    Row(
                        modifier = modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        AssistChip(onClick = {
                            onStartDateChipClick.invoke()
                        }, label = {
                            Text(
                                text = fromDatePickerState.selectedDateMillis!!.toLocalDate()
                                    .format(
                                        DateTimeFormatter.ofPattern("MMMM d yyyy")
                                    ), color = MaterialTheme.colorScheme.primary
                            )
                        })

                        TextButton(
                            onClick = {
                                onStartTimeChipClick.invoke()
                            }) {
                            Text(
                                text = LocalTime.of(
                                    fromTimePickerState.hour,
                                    fromTimePickerState.minute
                                ).format(formatter),
                                color = MaterialTheme.colorScheme.primary
                            )
                        }

                    }
                },

                )
            ListItem(
                colors = ListItemDefaults.colors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainer
                ),
                modifier = modifier
                    .padding(
                        vertical = 8.dp
                    )
                    .clip(RoundedCornerShape(12.dp)),
                overlineContent = {
                    Text(
                        text = "To"
                    )
                },
                headlineContent = {
                    Row(
                        modifier = modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        AssistChip(
                            onClick = {
                                onEndDateChipClick.invoke()
                            },
                            label = {
                                Text(
                                    text = toDatePickerState.selectedDateMillis!!.toLocalDate()
                                        .format(
                                            DateTimeFormatter.ofPattern("MMMM d yyyy")
                                        ), color = MaterialTheme.colorScheme.primary
                                )
                            }
                        )

                        TextButton(
                            onClick = {
                                onEndTimeChipClick.invoke()
                            }
                        ) {
                            Text(
                                text = LocalTime.of(
                                    toTimePickerState.hour,
                                    toTimePickerState.minute
                                ).format(formatter),
                                color = MaterialTheme.colorScheme.primary
                            )
                        }

                    }
                },

                )

            OutlinedButton(
                shape = RoundedCornerShape(12.dp),
                modifier = modifier.fillMaxWidth(),
                onClick = {
                    onUpdateEvent.invoke()
                    onDismiss.invoke()
                }
            ) {
                Text(
                    modifier = modifier.padding(
                        vertical = 8.dp
                    ),
                    text = "Update Event"
                )
            }
        }
    }
}