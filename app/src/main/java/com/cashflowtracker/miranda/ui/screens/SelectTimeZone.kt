package com.cashflowtracker.miranda.ui.screens

import android.content.Intent
import android.os.Build
import android.icu.util.TimeZone as IcuTimeZone
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.cashflowtracker.miranda.R
import com.cashflowtracker.miranda.ui.composables.getTimeZoneInGMTFormat
import com.cashflowtracker.miranda.ui.theme.LocalCustomColors
import com.cashflowtracker.miranda.ui.theme.MirandaTheme
import com.cashflowtracker.miranda.utils.TimeZoneEntry
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone


class SelectTimeZone : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val dateTimeMillis =
            intent.getLongExtra("dateTimeMillis", -1L)
        val dateTime = if (dateTimeMillis != -1L) Date(dateTimeMillis) else Date()
        setContent {
            MirandaTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        TopAppBar(
                            title = { Text("Time Zones") },
                            navigationIcon = {
                                IconButton(
                                    onClick = { finish() },
                                    modifier = Modifier.padding(
                                        start = 0.dp,
                                        top = 16.dp,
                                        bottom = 16.dp
                                    )
                                ) {
                                    Icon(
                                        ImageVector.vectorResource(R.drawable.ic_arrow_back),
                                        contentDescription = "Back"
                                    )
                                }
                            },
                            actions = {
                                IconButton(
                                    onClick = { /* Logic to search */ },
                                    modifier = Modifier.padding(
                                        top = 16.dp,
                                        bottom = 16.dp,
                                        end = 16.dp
                                    )
                                ) {
                                    Icon(
                                        ImageVector.vectorResource(R.drawable.ic_search),
                                        contentDescription = "Search"
                                    )
                                }
                            }
                        )
                    }
                ) { paddingValues ->
                    val context = LocalContext.current
                    val is24HourFormat =
                        android.text.format.DateFormat.is24HourFormat(context)
                    val timePattern = if (is24HourFormat) "HH:mm" else "h:mm a"

                    val timeZones = TimeZone.getAvailableIDs()
                        .map {
                            val timezone = TimeZone.getTimeZone(it)
                            val inDaylightTime = timezone.inDaylightTime(dateTime)
                            val displayName = timezone.getDisplayName(
                                inDaylightTime,
                                TimeZone.LONG,
                                Locale.getDefault()
                            )
                            val gmtFormat = getTimeZoneInGMTFormat(it, dateTime)
                            val region = it.substringAfterLast('/').replace('_', ' ')
                            TimeZoneEntry(
                                id = it,
                                displayName = displayName,
                                gmtFormat = gmtFormat,
                                country = region,
                            )
                        }
                        .distinctBy { it.gmtFormat + it.country }
                        .sortedBy { it.gmtFormat }

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues)
                    ) {
                        items(timeZones) {
                            val timeZone = TimeZone.getTimeZone(it.id)
                            val sdf = SimpleDateFormat(timePattern, Locale.getDefault()).apply {
                                this.timeZone = timeZone
                            }
                            val currentTimeInTimeZone = sdf.format(dateTime)
                            val headlineText = "$currentTimeInTimeZone ${it.gmtFormat}"
                            ListItem(
                                overlineContent = {
                                    Text(
                                        text = it.displayName,
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                },
                                headlineContent = {
                                    Text(
                                        text = headlineText,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        modifier = Modifier.padding(vertical = 4.dp)
                                    )
                                },
                                supportingContent = {
                                    Text(
                                        text = it.country,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                },
                                leadingContent = {
                                    Box(
                                        modifier = Modifier
                                            .size(40.dp)
                                            .clip(CircleShape)
                                            .background(LocalCustomColors.current.surfaceTintBlue)
                                    ) {
                                        Icon(
                                            imageVector = ImageVector.vectorResource(R.drawable.ic_public),
                                            contentDescription = "",
                                            tint = LocalCustomColors.current.icon,
                                            modifier = Modifier
                                                .size(24.dp)
                                                .align(Alignment.Center)
                                        )
                                    }
                                },
                                modifier = Modifier.clickable {
                                    val resultIntent = Intent().putExtra("timezone", it)
                                    setResult(RESULT_OK, resultIntent)
                                    finish()
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}