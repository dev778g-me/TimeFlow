package com.dev.timeflow.View.Widget

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.ToggleButton
import androidx.compose.material3.ToggleButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.glance.LocalContext
import com.composables.icons.lucide.Bell
import com.composables.icons.lucide.Clock
import com.composables.icons.lucide.FlagTriangleRight
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Notebook
import com.composables.icons.lucide.Signature
import com.dev.timeflow.Data.Model.ImportanceChipModel
import com.dev.timeflow.Data.Model.SavingModel
import java.time.LocalTime


@OptIn(ExperimentalMaterial3ExpressiveApi::class, ExperimentalMaterial3Api::class)
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
    onTaskSave : () -> Unit,
    onEventSave : () -> Unit,
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
    context : Context
) {
    val localContext = androidx.compose.ui.platform.LocalContext.current
    ModalBottomSheet(
        onDismissRequest = {
            onDismiss.invoke()
        }
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 24.dp
                ),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Row {
                savingChipList.forEachIndexed {
                        index , type ->
                    FilterChip(
                        modifier = modifier.padding(
                            end = 4.dp
                        ),
                        selected = index == selectedSavingType,
                        onClick = {
                            hapticFeedback.performHapticFeedback(
                                hapticFeedbackType = HapticFeedbackType.Confirm
                            )
                            changeSavingType.invoke(index)
                        },
                        label = {
                            Text(
                                text = type.title
                            )
                        },
                        leadingIcon = {
                            Icon(
                                modifier = modifier.size(FilterChipDefaults.IconSize),
                                imageVector = type.icon,
                                contentDescription = null
                            )
                        }
                    )
                }
            }
            Row {

                Spacer(
                    modifier = modifier.width(4.dp)
                )
                ToggleButton(
                    colors = ToggleButtonDefaults.toggleButtonColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    ),
                    checked = switchState,
                    onCheckedChange = {
                        onSwitchState.invoke(
                            it
                        )
                        if (it) {
                            // for a13 and a13 + devices
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                val hasPermission = ContextCompat.checkSelfPermission(
                                    context,
                                    Manifest.permission.POST_NOTIFICATIONS
                                ) == PackageManager.PERMISSION_GRANTED
                                // if permission is denied <umm i can show a dialog to navigate to the app info page
                                if (!hasPermission) {
                                    onSwitchState.invoke(false)
                                    onTimeState.invoke(false)
                                    Toast.makeText(
                                        context,
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
                ) {
                    Icon(
                        imageVector = Lucide.Bell,
                        contentDescription = null
                    )
                }
            }
        }
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
        ) {
            TextField(
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent
                ),
                modifier = modifier
                    .fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                leadingIcon = {
                    Icon(
                        imageVector = Lucide.Signature,
                        contentDescription = null
                    )
                },
                placeholder = {
                    Text(
                        text = if(selectedSavingType ==0) "Event name" else "Task name"
                    )
                },
                value = taskName,
                onValueChange = {
                    onTaskNameChange.invoke(it)
                },
                maxLines = 1,
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next
                )
            )
            Spacer(
                modifier = modifier.height(
                    8.dp
                )
            )
            TextField(
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent
                ),
                modifier = modifier.fillMaxWidth(),
                value = taskDescription,
                onValueChange = {
                    onTaskDescriptionChange.invoke(it)
                },
                leadingIcon = {
                    Icon(
                        imageVector = Lucide.Notebook,
                        contentDescription = null,
                    )
                },

                placeholder = {
                    Text(
                        text = "Description(optional)"
                    )
                },
                maxLines = 1,
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {

                    }
                )
            )

            Spacer(
                modifier = modifier.height(8.dp)
            )

            AnimatedContent(
                targetState = selectedSavingType == 1
            ) {
                if (it) {
                    Column {
                        Text(
                            text = "Priority",
                            style = MaterialTheme.typography.labelLarge.copy(
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.primary
                            )
                        )
                        Row (
                            modifier = modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Start
                        ){
                            importanceChip.forEachIndexed {
                                    index , chip ->
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    FilterChip(
                                        modifier = modifier.padding(
                                            end = 4.dp
                                        ),
                                        selected = selectedImportantChip == index,
                                        onClick = {
                                            onSelectedImportantChipChange.invoke(index)
                                            hapticFeedback.performHapticFeedback(
                                                hapticFeedbackType = HapticFeedbackType.Confirm
                                            )
                                        },
                                        label = {
                                            Text(
                                                text = chip.label
                                            )
                                        },
                                        leadingIcon = {
                                            Icon(
                                                modifier = modifier.size(
                                                    FilterChipDefaults.IconSize
                                                ),
                                                imageVector = Lucide.FlagTriangleRight,
                                                contentDescription = null,
                                                tint = chip.color
                                            )
                                        }
                                    )

                                }
                            }
                        }
                    }
                }
            }
            AnimatedContent(
                targetState = switchState, transitionSpec = {
                    scaleIn() togetherWith scaleOut()
                }) {
                if (it) {
                 Button(
                     modifier = modifier.fillMaxWidth(),
                     onClick = {
                        onTimeState.invoke(
                            !showTimeState
                        )
                     }
                 ) {
                     Text(
                         text = "Select Time"
                     )
                 }
                }
            }
            Button(
                enabled = isButtonEnabled,
                modifier = modifier
                    .fillMaxWidth()
                    .padding(
                        vertical = 8.dp
                    ),
                shape = RoundedCornerShape(12.dp),
                onClick = {
                    if (switchState){
                        if (timerState.hour <= LocalTime.now().hour &&
                            timerState.minute <= LocalTime.now().minute){
                            Toast.makeText(localContext,"the timeless", Toast.LENGTH_LONG).show()
                        }
                    }
                    if (selectedSavingType == 0) {
                        onEventSave.invoke()
                    } else {
                        onTaskSave.invoke()
                    }
                    onDismiss.invoke()
                }
            ) {
                Text(
                    modifier = modifier.padding(
                        vertical = 8.dp
                    ),
                    text = "Save"
                )
            }
        }
    }
}