package com.dev.timeflow.View.utils.componets

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.rememberBottomSheetState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.dev.timeflow.View.utils.toUtcDate
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun CountDownBottomSheet(
    modifier: Modifier = Modifier,
    onDismiss : () -> Unit,
    onPin : () -> Unit,
    dateforCountdown : LocalDate,
    currentDate : LocalDate,
    countdownName : String,
    onNameChange : (String) -> Unit,
    fromDatePickerState : DatePickerState,
    toDatePickerState : DatePickerState,
    fromTimePickerState : TimePickerState,
    toTimePickerState : TimePickerState,
    onFromDateClick : () -> Unit,
    onToDateClick : () -> Unit,
    onFromTimeClick : () -> Unit,
    onToTimeClick : () -> Unit,
) {

    val sheetState = rememberBottomSheetState(
        initialValue = SheetValue.Hidden,
    )

    val scope  = rememberCoroutineScope ()

    val timeFormatter = remember { DateTimeFormatter.ofPattern("h:mm a") }
    val dateFormatter = remember { DateTimeFormatter.ofPattern("MMM d, yyyy") }

    val fromFormattedTime = remember(fromTimePickerState.hour, fromTimePickerState.minute) {
        LocalTime.of(fromTimePickerState.hour, fromTimePickerState.minute).format(timeFormatter)
    }
    val toFormattedTime = remember(toTimePickerState.hour, toTimePickerState.minute) {
        LocalTime.of(toTimePickerState.hour, toTimePickerState.minute).format(timeFormatter)
    }

    ModalBottomSheet(
        sheetState = rememberModalBottomSheetState(
            skipPartiallyExpanded = true
        ),
        onDismissRequest = {
            onDismiss()
         scope.launch {
             sheetState.hide()
         }
        },

    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 16.dp
                )
                .imePadding()
        ) {

            Text(
                text = "Countdown to ${
                    dateforCountdown.format(dateFormatter)
                }",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(4.dp))

            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = countdownName,
                onValueChange = onNameChange,
                label = {
                    Text("Name for Countdown")
                },
                textStyle = MaterialTheme.typography.bodyLarge,
                maxLines = 1,
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done
                ),
                shape = RoundedCornerShape(16.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                color = MaterialTheme.colorScheme.surfaceContainer
            ) {
                Column {
                    ScheduleRow(
                        label = "From",
                        dateText = fromDatePickerState.selectedDateMillis
                            ?.toUtcDate()
                            ?.format(dateFormatter)
                            ?: "Select date",
                        timeText = fromFormattedTime,
                        onDateClick = onFromDateClick,
                        onTimeClick = onFromTimeClick
                    )
                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        thickness = 2.dp
                    )
                    ScheduleRow(
                        label = "To",
                        dateText = toDatePickerState.selectedDateMillis
                            ?.toUtcDate()
                            ?.format(dateFormatter)
                            ?: "Select date",
                        timeText = toFormattedTime,
                        onDateClick = onToDateClick,
                        onTimeClick = onToTimeClick
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // desc about the actions
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = "Saves your countdown and pins a home screen widget to track the time left. Long-press a day in the calendar to change the range.\n"
               , style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                enabled = countdownName.isNotBlank(),
                onClick = onPin,
                modifier = Modifier.fillMaxWidth(),
                shapes = ButtonDefaults.shapes()
            ) {
                Text(
                    modifier = Modifier.padding(
                        vertical = 4.dp
                    ),
                    text = "Pin Widget",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold,

                )
            }

            Spacer(modifier = Modifier.height(24.dp))

        }

    }

}
