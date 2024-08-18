package com.cashflowtracker.miranda.ui.composables

import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerModal(
    onDateSelected: (Long?) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState()

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                onDateSelected(datePickerState.selectedDateMillis)
                onDismiss()
            }) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    ) {
        DatePicker(state = datePickerState)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePicker(selectedDate: MutableState<String>) {
//    val context = LocalContext.current
    var isDatePickerVisible by remember { mutableStateOf(false) }

    // Initialize the selectedDate with today's date in the desired format
    val initialDate = remember {
        val calendar = Calendar.getInstance()
        SimpleDateFormat("EEE, MMM d, yyyy", Locale.getDefault()).format(calendar.time)
    }

    if (selectedDate.value.isEmpty()) {
        print("Selected date is empty")
        selectedDate.value = initialDate
    }

    // This TextButton triggers the date picker dialog
    TextButton(onClick = { isDatePickerVisible = true }) {
        Text(selectedDate.value, color = MaterialTheme.colorScheme.onBackground)
    }

    if (isDatePickerVisible) {
        DatePickerModal(
            onDateSelected = { selectedDateMillis ->
                selectedDateMillis?.let {
                    val calendar = Calendar.getInstance().apply {
                        timeInMillis = it
                    }
//                    val month = calendar.get(Calendar.MONTH) + 1
//                    val day = calendar.get(Calendar.DAY_OF_MONTH)
//                    val year = calendar.get(Calendar.YEAR)
//                    selectedDate.value = "$month/$day/$year"
                    selectedDate.value = SimpleDateFormat(
                        "EEE, MMM d, yyyy",
                        Locale.getDefault()
                    ).format(calendar.time)
                }
                isDatePickerVisible = false
            },
            onDismiss = { isDatePickerVisible = false }
        )
    }
}