package com.cashflowtracker.miranda.ui.composables

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Composable
fun TimePickerDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    content: @Composable () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        dismissButton = {
            TextButton(onClick = { onDismiss() }) {
                Text("Dismiss")
            }
        },
        confirmButton = {
            TextButton(onClick = { onConfirm() }) {
                Text("OK")
            }
        },
        text = { content() }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DialWithDialog(
    onConfirm: (TimePickerState) -> Unit,
    onDismiss: () -> Unit,
) {
    val currentTime = Calendar.getInstance()

    val timePickerState = rememberTimePickerState(
        initialHour = currentTime.get(Calendar.HOUR_OF_DAY),
        initialMinute = currentTime.get(Calendar.MINUTE),
        is24Hour = true,
    )

    TimePickerDialog(
        onDismiss = { onDismiss() },
        onConfirm = { onConfirm(timePickerState) }
    ) {
        androidx.compose.material3.TimePicker(
            state = timePickerState,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePicker(selectedTime: MutableState<String>) {
    var isTimePickerVisible by remember { mutableStateOf(false) }

    // Initialize the selectedTime with the current time in 24-hour format
    val currentTime = remember {
        val calendar = Calendar.getInstance()
        SimpleDateFormat("HH:mm", Locale.getDefault()).format(calendar.time)
    }

    if (selectedTime.value.isEmpty()) {
        selectedTime.value = currentTime
    }

    // This TextButton triggers the time picker dialog
    TextButton(onClick = { isTimePickerVisible = true }) {
        Text(selectedTime.value, color = MaterialTheme.colorScheme.onBackground)
    }

    if (isTimePickerVisible) {
        DialWithDialog(
            onConfirm = { timePickerState ->
                val calendar = Calendar.getInstance().apply {
                    set(Calendar.HOUR_OF_DAY, timePickerState.hour)
                    set(Calendar.MINUTE, timePickerState.minute)
                }
                selectedTime.value =
                    SimpleDateFormat("HH:mm", Locale.getDefault()).format(calendar.time)
                isTimePickerVisible = false
            },
            onDismiss = { isTimePickerVisible = false }
        )
    }
}