package com.cashflowtracker.miranda.ui.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.util.Calendar
import java.util.TimeZone
import kotlin.math.absoluteValue

fun getCurrentTimeZone(): String {
    val calendar = Calendar.getInstance()
    val timeZone = calendar.timeZone
    return timeZone.getDisplayName(timeZone.inDaylightTime(calendar.time), TimeZone.LONG)
}

fun getTimeZoneInGMTFormat(timeZoneId: String): String {
    val timeZone = TimeZone.getTimeZone(timeZoneId)
    val calendar = Calendar.getInstance()
    val offset =
        timeZone.getOffset(calendar.timeInMillis) / 3600000 // Convert milliseconds to hours
    val sign = if (offset >= 0) "+" else "-"
    return "GMT$sign${offset.absoluteValue}"
}

@Composable
fun TimeZonePickerDialog(
    onDismiss: () -> Unit,
    onTimeZoneSelected: (String) -> Unit
) {
    val scrollState = rememberScrollState()
    val timeZones = TimeZone.getAvailableIDs()
//    val displayNames = timeZones.map { TimeZone.getTimeZone(it).displayName }
    val uniqueTimeZonesInGMT = timeZones
        .map {
            getTimeZoneInGMTFormat(it) to TimeZone.getTimeZone(it).getDisplayName(
                TimeZone.getTimeZone(it).inDaylightTime(Calendar.getInstance().time),
                TimeZone.LONG
            )
        }
        .distinctBy { it.first }    // Ensure unique GMT offsets
        .sortedBy { it.first }      // Sort by GMT offset

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {},
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        },
        text = {
            Column(modifier = Modifier.verticalScroll(scrollState)) {
                uniqueTimeZonesInGMT.forEach { (gmtFormat, displayName) ->
                    Text(
                        text = "$gmtFormat - $displayName",
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onTimeZoneSelected("$gmtFormat - $displayName")
                                onDismiss()
                            }
                            .padding(8.dp)
                    )
                }
//                displayNames.forEach { timeZoneName ->
//                    Text(
//                        text = timeZoneName,
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .clickable {
//                                onTimeZoneSelected(timeZoneName)
//                                onDismiss()
//                            }
//                            .padding(8.dp)
//                    )
//                }
            }
        }
    )
}

@Composable
fun TimeZonePicker(selectedTimeZone: MutableState<String>) {
    var isTimeZonePickerVisible by remember { mutableStateOf(false) }

    // Initialize the selectedTimeZone with the current system time zone
    val currentTimeZone = remember {
        val currentTimeZoneId = TimeZone.getDefault().id
        "${getTimeZoneInGMTFormat(currentTimeZoneId)} - ${getCurrentTimeZone()}"
    }

    // Initialize the selectedTimeZone with the current system time zone in GMT format
//    val currentTimeZoneId = TimeZone.getDefault().id
//    val currentTimeZone = remember { getTimeZoneInGMTFormat(currentTimeZoneId) }

    if (selectedTimeZone.value.isEmpty()) {
        selectedTimeZone.value = currentTimeZone
    }

    // This TextButton triggers the time zone picker dialog
    TextButton(onClick = { isTimeZonePickerVisible = true }) {
        Text(selectedTimeZone.value, color = MaterialTheme.colorScheme.onSurface)
    }

//    if (isTimeZonePickerVisible) {
//        TimeZonePickerDialog(
//            onDismiss = { isTimeZonePickerVisible = false },
//            onTimeZoneSelected = { timeZoneName ->
//                selectedTimeZone.value = timeZoneName
//            }
//        )
//    }
    if (isTimeZonePickerVisible) {
        TimeZonePickerDialog(
            onDismiss = { isTimeZonePickerVisible = false },
            onTimeZoneSelected = { timeZoneDisplayName ->
                selectedTimeZone.value = timeZoneDisplayName
            }
        )
    }
}