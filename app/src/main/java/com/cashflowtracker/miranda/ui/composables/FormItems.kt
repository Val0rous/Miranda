package com.cashflowtracker.miranda.ui.composables

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.ActivityResult
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableDoubleState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.cashflowtracker.miranda.R
import com.cashflowtracker.miranda.ui.screens.SelectAccountType
import com.cashflowtracker.miranda.ui.screens.SelectDestination
import com.cashflowtracker.miranda.ui.screens.SelectSource
import com.cashflowtracker.miranda.utils.Coordinates
import com.cashflowtracker.miranda.utils.LocationService
import com.cashflowtracker.miranda.utils.PermissionStatus
import com.cashflowtracker.miranda.utils.StartMonitoringResult
import com.cashflowtracker.miranda.utils.rememberPermission

@Composable
fun DateTimeForm(
    selectedDate: MutableState<String>,
    selectedTime: MutableState<String>
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .offset(y = (0).dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.ic_schedule),
            contentDescription = "Date & Time"
        )
        Spacer(modifier = Modifier.width(4.dp))
        DatePicker(selectedDate = selectedDate)
        Spacer(modifier = Modifier.weight(1f))
        TimePicker(selectedTime = selectedTime)
    }
}

@Composable
fun TimeZoneForm(selectedTimeZone: MutableState<String>) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .offset(y = (0).dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.ic_public),
            contentDescription = "Time Zone"
        )
        Spacer(modifier = Modifier.width(4.dp))
        TimeZonePicker(selectedTimeZone = selectedTimeZone)
    }
}

@Composable
fun RepeatForm(selectedRepeat: MutableState<String>) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.ic_replay),
            contentDescription = "Repeat",
            modifier = Modifier
                .scale(scaleX = -1f, scaleY = 1f)   // Flip horizontally
                .rotate(-45f)
        )
        Spacer(modifier = Modifier.width(4.dp))
        RepeatPicker(selectedRepeat = selectedRepeat)
    }
}

@Composable
fun CreateFirstOccurrenceForm(isCreateFirstOccurrence: MutableState<Boolean>) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 27.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            "Create First Occurrence",
            style = MaterialTheme.typography.titleSmall
        )
        Spacer(modifier = Modifier.weight(1f))
        Checkbox(
            checked = isCreateFirstOccurrence.value,
            onCheckedChange = {
                isCreateFirstOccurrence.value = !isCreateFirstOccurrence.value
            }
        )
    }
}

@Composable
fun NotificationsForm(notifications: SnapshotStateList<String>) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp),
        verticalAlignment = Alignment.Top
    ) {
        Box(modifier = Modifier.padding(top = 12.dp)) {
            Icon(
                ImageVector.vectorResource(R.drawable.ic_notifications),
                contentDescription = "Notifications"
            )
        }
        Spacer(modifier = Modifier.width(4.dp))
        Column {
            NotificationPicker(notifications)
        }
    }
}

@Composable
fun SourceForm(
    source: String,
    sourceIcon: Int?,
    transactionType: MutableState<String>,
    sourceLauncher: ManagedActivityResultLauncher<Intent, ActivityResult>
) {
    val context = LocalContext.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .offset(y = (0).dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.ic_logout),
            contentDescription = "Source"
        )
        Spacer(modifier = Modifier.width(16.dp))

        Box(modifier = Modifier
            .fillMaxWidth()
            .clickable {
                val intent =
                    Intent(
                        context,
                        SelectSource::class.java
                    )
                intent.putExtra("transactionType", transactionType.value)
                sourceLauncher.launch(intent)
            }
        ) {
            OutlinedTextField(
                value = source,
                onValueChange = { },
                label = { Text("Source") },
                readOnly = true,
                modifier = Modifier.fillMaxWidth(),
                trailingIcon = {
                    sourceIcon?.let { iconId ->
                        Icon(
                            imageVector = ImageVector.vectorResource(
                                iconId
                            ),
                            contentDescription = source,
                            modifier = Modifier.padding(end = 12.dp)
                        )
                    }
                },
                enabled = false,
                colors = OutlinedTextFieldDefaults.colors(
                    disabledTextColor = MaterialTheme.colorScheme.onSurface,
                    disabledContainerColor = MaterialTheme.colorScheme.surface,
                    disabledBorderColor = MaterialTheme.colorScheme.outline,
                    disabledTrailingIconColor = MaterialTheme.colorScheme.onSurface,
                    disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            )
        }
    }
}

@Composable
fun DestinationForm(
    destination: String,
    destinationIcon: Int?,
    transactionType: MutableState<String>,
    destinationLauncher: ManagedActivityResultLauncher<Intent, ActivityResult>
) {
    val context = LocalContext.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .offset(y = (0).dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.ic_login),
            contentDescription = "Destination"
        )
        Spacer(modifier = Modifier.width(16.dp))

        // Type Field
        Box(modifier = Modifier
            .fillMaxWidth()
            .clickable {
                val intent =
                    Intent(
                        context,
                        SelectDestination::class.java
                    )
                intent.putExtra("transactionType", transactionType.value)
                destinationLauncher.launch(intent)
            }
        ) {
            OutlinedTextField(
                value = destination,
                onValueChange = { },
                label = { Text("Destination") },
                readOnly = true,
                modifier = Modifier.fillMaxWidth(),
                trailingIcon = {
                    destinationIcon?.let { iconId ->
                        Icon(
                            imageVector = ImageVector.vectorResource(
                                iconId
                            ),
                            contentDescription = destination,
                            modifier = Modifier.padding(end = 12.dp)
                        )
                    }
                },
                enabled = false,
                colors = OutlinedTextFieldDefaults.colors(
                    disabledTextColor = MaterialTheme.colorScheme.onSurface,
                    disabledContainerColor = MaterialTheme.colorScheme.surface,
                    disabledBorderColor = MaterialTheme.colorScheme.outline,
                    disabledTrailingIconColor = MaterialTheme.colorScheme.onSurface,
                    disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            )
        }
    }
}

@Composable
fun AmountForm(amount: MutableDoubleState) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .offset(y = (0).dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.ic_payments),
            contentDescription = "Amount"
        )
        Spacer(modifier = Modifier.width(16.dp))
        // Amount Field
        OutlinedTextField(
            value = if (amount.doubleValue == 0.0) {
                ""
            } else {
                "%.2f".format(amount.doubleValue)
            },
            onValueChange = { text ->
                amount.doubleValue = text.toDoubleOrNull()?.let {
                    if (it >= 0) {
                        "%.2f".format(it).toDoubleOrNull()
                    } else {
                        0.0
                    }
                } ?: amount.doubleValue
            },
            label = { Text("Amount") },
            placeholder = { Text("0.00 €") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun CommentForm(comment: MutableState<String>) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .offset(y = (0).dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.ic_chat),
            contentDescription = "Comment"
        )
        Spacer(modifier = Modifier.width(16.dp))
        // Comment Field
        OutlinedTextField(
            value = comment.value,
            onValueChange = { text -> comment.value = text },
            label = { Text("Comment") },
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun LocationForm(
    location: MutableState<String>,
    locationService: LocationService,
    isError: MutableState<Boolean>
) {
    val context = LocalContext.current
    val coordinates = remember { mutableStateOf<Coordinates?>(null) }
    val isLocationLoaded = remember { mutableStateOf(false) }

    val snackbarHostState = remember { SnackbarHostState() }
    val showLocationDisabledAlert = remember { mutableStateOf(false) }
    var showPermissionDeniedAlert by remember { mutableStateOf(false) }
    var showPermissionPermanentlyDeniedSnackbar by remember { mutableStateOf(false) }

    val locationPermission = rememberPermission(
        Manifest.permission.ACCESS_FINE_LOCATION
    ) { status ->
        when (status) {
            PermissionStatus.Granted -> {
                val res = locationService.requestCurrentLocation()
                showLocationDisabledAlert.value = res == StartMonitoringResult.GPSDisabled
            }

            PermissionStatus.Denied ->
                showPermissionDeniedAlert = true

            PermissionStatus.PermanentlyDenied ->
                showPermissionPermanentlyDeniedSnackbar = true

            PermissionStatus.Unknown -> {}
        }
    }


    Row(
        modifier = Modifier
            .fillMaxWidth()
            .offset(y = (0).dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.ic_location_on),
            contentDescription = "Location"
        )
        Spacer(modifier = Modifier.width(16.dp))
        // Location Field with Placeholder Map
        LocationTextField(
            location = location,
            locationService = locationService,
            locationPermission = locationPermission,
            showLocationDisabledAlert = showLocationDisabledAlert,
            isLocationLoaded = isLocationLoaded,
            coordinates = coordinates,
            isError = isError,
            modifier = Modifier.fillMaxWidth()
        )
    }
    Spacer(modifier = Modifier.height(16.dp))

    if (coordinates.value != null) {
        MapScreen(
            latitude = coordinates.value?.latitude ?: 0.0,
            longitude = coordinates.value?.longitude ?: 0.0,
            isLocationLoaded = isLocationLoaded
        )
    }

    if (showLocationDisabledAlert.value) {
        AlertDialog(
            title = { Text("Location disabled") },
            text = { Text("Location must be enabled to get your current location in the app.") },
            confirmButton = {
                TextButton(onClick = {
                    locationService.openLocationSettings()
                    showLocationDisabledAlert.value = false
                }) {
                    Text("Enable")
                }
            },
            dismissButton = {
                TextButton(onClick = { showLocationDisabledAlert.value = false }) {
                    Text("Dismiss")
                }
            },
            onDismissRequest = { showLocationDisabledAlert.value = false }
        )
    }

    if (showPermissionDeniedAlert) {
        AlertDialog(
            title = { Text("Location permission denied") },
            text = { Text("Location permission is required to get your current location in the app.") },
            confirmButton = {
                TextButton(onClick = {
                    locationPermission.launchPermissionRequest()
                    showPermissionDeniedAlert = false
                }) {
                    Text("Grant")
                }
            },
            dismissButton = {
                TextButton(onClick = { showPermissionDeniedAlert = false }) {
                    Text("Dismiss")
                }
            },
            onDismissRequest = { showPermissionDeniedAlert = false }
        )
    }

    if (showPermissionPermanentlyDeniedSnackbar) {
        LaunchedEffect(snackbarHostState) {
            val res = snackbarHostState.showSnackbar(
                "Location permission is required.",
                "Go to Settings",
                duration = SnackbarDuration.Long
            )
            if (res == SnackbarResult.ActionPerformed) {
                val intent =
                    Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                        data = Uri.fromParts("package", context.packageName, null)
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    }
                if (intent.resolveActivity(context.packageManager) != null) {
                    context.startActivity(intent)
                }
            }
            showPermissionPermanentlyDeniedSnackbar = false
        }
    }
}

@Composable
fun AccountTitleForm(
    accountTitle: MutableState<String>,
    isError: MutableState<Boolean>
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .offset(y = (0).dp)
            .padding(bottom = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.ic_match_case),
            contentDescription = "Title"
        )
        Spacer(modifier = Modifier.width(16.dp))
        // Title Field
        OutlinedTextField(
            value = accountTitle.value,
            onValueChange = { text -> accountTitle.value = text },
            label = { Text("Title") },
            isError = isError.value,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun AccountTypeForm(
    accountType: String,
    accountIcon: Int?,
    launcher: ManagedActivityResultLauncher<Intent, ActivityResult>
) {
    val context = LocalContext.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .offset(y = (0).dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.ic_category),
            contentDescription = "Type"
        )
        Spacer(modifier = Modifier.width(16.dp))
        // Type Field
        Box(modifier = Modifier
            .fillMaxWidth()
            .clickable {
                val intent = Intent(context, SelectAccountType::class.java)
                //intent.putExtra("accountType", accountType)
                launcher.launch(intent)
            }
        ) {
            OutlinedTextField(
                value = accountType,
                onValueChange = { },
                label = { Text("Type") },
                readOnly = true,
                modifier = Modifier.fillMaxWidth(),
                trailingIcon = {
                    accountIcon?.let { iconId ->
                        Icon(
                            imageVector = ImageVector.vectorResource(
                                iconId
                            ),
                            contentDescription = accountType,
                            modifier = Modifier.padding(end = 12.dp)
                        )
                    }
                },
                enabled = false,
                colors = OutlinedTextFieldDefaults.colors(
                    disabledTextColor = MaterialTheme.colorScheme.onSurface,
                    disabledContainerColor = MaterialTheme.colorScheme.surface,
                    disabledBorderColor = MaterialTheme.colorScheme.outline,
                    disabledTrailingIconColor = MaterialTheme.colorScheme.onSurface,
                    disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            )
        }
    }
}