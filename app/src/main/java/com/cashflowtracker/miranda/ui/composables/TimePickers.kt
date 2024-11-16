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
import androidx.compose.ui.platform.LocalContext
import java.text.SimpleDateFormat
import java.time.LocalTime
import java.time.format.DateTimeFormatter
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
                Text("Cancel")
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
    selectedTime: MutableState<String>,
    onConfirm: (TimePickerState) -> Unit,
    onDismiss: () -> Unit,
) {
    val formatter = DateTimeFormatter.ofPattern("HH:mm")
    val initialTime = try {
        LocalTime.parse(selectedTime.value, formatter)
    } catch (e: Exception) {
        val calendar = Calendar.getInstance()
        LocalTime.of(calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE))
    }

    val is24HourClock = android.text.format.DateFormat.is24HourFormat(LocalContext.current)

    val timePickerState = rememberTimePickerState(
        initialHour = initialTime.hour,
        initialMinute = initialTime.minute,
        is24Hour = is24HourClock,
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
    val formatter = DateTimeFormatter.ofPattern("HH:mm")
    val currentTime = remember {
        val calendar = Calendar.getInstance()
        LocalTime.of(calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE))
            .format(formatter)
    }

    if (selectedTime.value.isEmpty()) {
        selectedTime.value = currentTime
    }

    // This TextButton triggers the time picker dialog
    TextButton(onClick = { isTimePickerVisible = true }) {
        Text(text = selectedTime.value, color = MaterialTheme.colorScheme.onBackground)
    }

    if (isTimePickerVisible) {
        DialWithDialog(
            selectedTime = selectedTime,
            onConfirm = { timePickerState ->
                val selectedLocalTime = LocalTime.of(
                    timePickerState.hour,
                    timePickerState.minute
                )
                selectedTime.value = selectedLocalTime.format(formatter)
                isTimePickerVisible = false
            },
            onDismiss = { isTimePickerVisible = false }
        )
    }
}