package com.dev.timeflow.View.Widget

import androidx.compose.animation.AnimatedContent
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
import com.composables.icons.lucide.Bell
import com.composables.icons.lucide.Clock
import com.composables.icons.lucide.FlagTriangleRight
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Notebook
import com.composables.icons.lucide.Signature
import com.dev.timeflow.Data.Model.ImportanceChipModel
import com.dev.timeflow.Data.Model.SavingModel



@OptIn(ExperimentalMaterial3ExpressiveApi::class, ExperimentalMaterial3Api::class)
@Composable
fun SheetToAddEventAndTask(
    modifier: Modifier = Modifier,
    onDismiss : () -> Unit,
    onSwitchState : (Boolean) -> Unit,
    onTimeState : (Boolean) -> Unit,
    selectedSavingType : Int,
    isButtonEnabled : Boolean,
    onTaskSave : () -> Unit,
    onEventSave : () -> Unit,
    showCalendar : () -> Unit,
    onselectedImportantChipChange : (Int) -> Unit,
    onTaskNameChange : (String) -> Unit,
    onTaskDescriptionChange : (String) -> Unit,
    onSwitchChange : (Boolean) -> Unit,
    changeSavingType: (Int) -> Unit,
    savingChipList : List<SavingModel>,
    importanceChip : List<ImportanceChipModel>,
    hapticFeedback: HapticFeedback,
    switchState : Boolean,
    showTimeState : Boolean,
    taskName : String,
    taskDescription : String,
    selectedImportantChip : Int

) {
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
                AnimatedContent(
                    targetState = selectedSavingType == 0,
                    transitionSpec = {
                        scaleIn() togetherWith scaleOut()
                    }
                ) {
                    if (it) {
                        FilledTonalIconButton(
                            onClick = {
                                showCalendar.invoke()
                                hapticFeedback.performHapticFeedback(
                                    HapticFeedbackType.LongPress
                                )
                            }
                        ) {
                            Icon(
                                imageVector = Lucide.Clock,
                                modifier = modifier.size(IconButtonDefaults.extraSmallIconSize),
                                contentDescription = null
                            )
                        }
                    }
                }
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
                                            onselectedImportantChipChange.invoke(index)
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
                targetState = switchState,
                transitionSpec = { scaleIn() togetherWith scaleOut() }
            ) {
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
                    if (selectedSavingType == 0) {
                        onEventSave.invoke()
                    }
                    else{
                    onTaskSave.invoke()}

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