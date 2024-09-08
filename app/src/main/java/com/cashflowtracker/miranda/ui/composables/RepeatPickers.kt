package com.cashflowtracker.miranda.ui.composables

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

@Composable
fun RepeatPickerDialog(
    onDismiss: () -> Unit,
    onRepeatSelected: (String) -> Unit,
    selectedRepeat: MutableState<String>
) {
    val repeatOptions = listOf("Every day", "Every week", "Every month", "Every year")
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {},
        dismissButton = {},
        text = {
            Column {
                repeatOptions.forEach { option ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .selectable(
                                selected = (option == selectedRepeat.value),
                                onClick = { onRepeatSelected(option) }
                            ),
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = (option == selectedRepeat.value),
                            onClick = { onRepeatSelected(option) },
                            modifier = Modifier.padding(4.dp),
                            enabled = true,
                            interactionSource = remember { MutableInteractionSource() }
                        )
                        Text(
                            text = option,
                            modifier = Modifier.padding(start = 16.dp),
                        )
                    }
                }
            }
        }
    )
}

@Composable
fun RepeatPicker(selectedRepeat: MutableState<String>) {
    var isRepeatPickerVisible by remember { mutableStateOf(false) }
    if (selectedRepeat.value.isEmpty()) {
        selectedRepeat.value = "Every month"
    }

    TextButton(onClick = { isRepeatPickerVisible = true }) {
        Text(selectedRepeat.value, color = MaterialTheme.colorScheme.onSurface)
    }

    if (isRepeatPickerVisible) {
        RepeatPickerDialog(
            onDismiss = { isRepeatPickerVisible = false },
            onRepeatSelected = { repeat ->
                selectedRepeat.value = repeat
                isRepeatPickerVisible = false
            },
            selectedRepeat = selectedRepeat
        )
    }
}