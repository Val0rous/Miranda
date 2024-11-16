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
import com.cashflowtracker.miranda.utils.formatDateWithWeekday
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerModal(
    selectedDate: MutableState<String>,
    onDateSelected: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val isoFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = try {
            LocalDate.parse(selectedDate.value, isoFormatter).toEpochDay() * 24 * 60 * 60 * 1000
        } catch (e: Exception) {
            LocalDate.now().toEpochDay() * 24 * 60 * 60 * 1000
        }
    )

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = {
                    val selectedMillis = datePickerState.selectedDateMillis
                    if (selectedMillis != null) {
                        val selectedLocalDate =
                            LocalDate.ofEpochDay(selectedMillis / (24 * 60 * 60 * 1000))
                        val formattedDate = selectedLocalDate.format(isoFormatter)
                        selectedDate.value = formattedDate
                        onDateSelected(formattedDate)
                    }
                    onDismiss()
                }
            ) {
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

    val isoFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    if (selectedDate.value.isEmpty()) {
        selectedDate.value = LocalDate.now().format(isoFormatter)
    }

    val displayDate = formatDateWithWeekday(selectedDate.value)

    // This TextButton triggers the date picker dialog
    TextButton(onClick = { isDatePickerVisible = true }) {
        Text(displayDate, color = MaterialTheme.colorScheme.onBackground)
    }

    if (isDatePickerVisible) {
        DatePickerModal(
            selectedDate = selectedDate,
            onDateSelected = { formattedDate ->
                selectedDate.value = formattedDate
                isDatePickerVisible = false
            },
            onDismiss = { isDatePickerVisible = false }
        )
    }
}