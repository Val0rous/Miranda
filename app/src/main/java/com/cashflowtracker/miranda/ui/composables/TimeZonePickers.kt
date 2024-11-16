package com.cashflowtracker.miranda.ui.composables

import android.content.Intent
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.ActivityResult
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.cashflowtracker.miranda.ui.screens.SelectTimeZone
import com.cashflowtracker.miranda.utils.TimeZoneEntry
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import kotlin.math.absoluteValue

fun getCurrentTimeZoneDisplayName(dateTime: Date): String {
    val calendar = Calendar.getInstance()
    val timeZone = calendar.timeZone
    return timeZone.getDisplayName(timeZone.inDaylightTime(dateTime), TimeZone.LONG)
}

fun getTimeZoneInGMTFormat(timeZoneId: String, date: Date): String {
    val timeZone = TimeZone.getTimeZone(timeZoneId)
    val offsetMillis = timeZone.getOffset(date.time)
    val totalMinutes = offsetMillis / 60000
    val sign = if (totalMinutes >= 0) "+" else "-"
    val hours = (totalMinutes.absoluteValue / 60)
    val minutes = (totalMinutes.absoluteValue % 60)
    val hoursString = hours.toString().padStart(2, '0')
    val minutesString = minutes.toString().padStart(2, '0')
    return "GMT$sign$hoursString:$minutesString"
}


fun combineDateAndTime(dateString: String, timeString: String): Date {
    val format = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
    return format.parse("$dateString $timeString")!!
}

@Composable
fun TimeZonePicker(
    selectedTimeZone: MutableState<TimeZoneEntry>,
    launcher: ManagedActivityResultLauncher<Intent, ActivityResult>,
    date: String,
    time: String,
) {
//    var isTimeZonePickerVisible by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val dateTime = combineDateAndTime(date, time)

    val is24HourClock = android.text.format.DateFormat.is24HourFormat(context)
    val timeFormatter = if (is24HourClock) "HH:mm" else "h:mm a"

    // Initialize the selectedTimeZone with the current system time zone
    val timeZoneId = selectedTimeZone.value?.id ?: TimeZone.getDefault().id
    val timeZone = TimeZone.getTimeZone(timeZoneId)
    val inDaylightTime = timeZone.inDaylightTime(dateTime)
    val displayName = timeZone.getDisplayName(inDaylightTime, TimeZone.LONG, Locale.getDefault())
    val gmtFormat = getTimeZoneInGMTFormat(timeZoneId, dateTime)
    val displayText = "$gmtFormat - $displayName"

    // Initialize the selectedTimeZone with the current system time zone in GMT format
//    val currentTimeZoneId = TimeZone.getDefault().id
//    val currentTimeZone = remember { getTimeZoneInGMTFormat(currentTimeZoneId) }

//    if (selectedTimeZone.value == null) {
//        val id = TimeZone.getDefault().id
//        selectedTimeZone.value = TimeZoneEntry(
//            id = id,
//            displayName = getTimeZoneInGMTFormat(id, dateTime),
//            gmtFormat = getCurrentTimeZone(),
//            country = "Earth",
//        )
//    }

    // This TextButton triggers the time zone picker dialog
    TextButton(onClick = {
        val intent = Intent(context, SelectTimeZone::class.java)
        intent.putExtra("dateTimeMillis", dateTime.time)
        launcher.launch(intent)
    }) {
        Text(
            text = displayText,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}