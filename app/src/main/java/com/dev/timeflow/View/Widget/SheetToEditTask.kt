package com.dev.timeflow.View.Widget

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.composables.icons.lucide.Bell
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.PenLine
import com.dev.timeflow.Data.Model.Tasks
import com.dev.timeflow.View.utils.toHour
import com.dev.timeflow.View.utils.toMinute
import kotlinx.coroutines.delay
import kotlin.text.ifEmpty

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SheetToEditTask(
    modifier: Modifier = Modifier,
    onDismiss :() -> Unit,
    onCheckBoxValueChange :(Boolean) -> Unit,
    onValueChange : (String) -> Unit,
    onNameValueChange : (String) -> Unit,
    tasks: Tasks
) {
    var description by remember(tasks.id) { mutableStateOf(tasks.description ?: "") }
    var name by remember(tasks.id) {mutableStateOf(tasks.name) }
    val localHapticFeedback = LocalHapticFeedback.current
    LaunchedEffect(description) {
        delay(500)
        if (description != tasks.description) {
            onValueChange(description)
        }
    }

    LaunchedEffect(name) {

        delay(500)
        if (name != tasks.name) {

            onNameValueChange.invoke(name)
        }
    }

    ModalBottomSheet(
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
               NewCheckBox(
                   isSelected = tasks.isCompleted
               ) {
                   localHapticFeedback.performHapticFeedback(
                       hapticFeedbackType = HapticFeedbackType.Confirm
                   )
                   onCheckBoxValueChange(it)
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
                   value = name,
                   textStyle = MaterialTheme.typography.headlineSmall,
                   onValueChange = {
                       name = it
                   }
               )

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
                    tasks.description?.ifEmpty { "Enter description" }?.let { Text(text = it) }
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
                modifier = modifier.
                padding(top = 8.dp).
                clip(RoundedCornerShape(12.dp)),
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
                   if (tasks.notification){
                       Text(
                           text = "${tasks.taskTime?.toHour()}:${tasks.taskTime?.toMinute()}"
                       )
                   } else {
                       Text(
                           text = "no notification"
                       )
                   }
                })
        }
    }
}