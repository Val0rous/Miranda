package com.cashflowtracker.miranda.ui.composables

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.ActivityResult
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.InputChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.cashflowtracker.miranda.R
import com.cashflowtracker.miranda.ui.screens.SelectAccountType
import com.cashflowtracker.miranda.ui.screens.SelectCurrency
import com.cashflowtracker.miranda.ui.screens.SelectDestination
import com.cashflowtracker.miranda.ui.screens.SelectSource
import com.cashflowtracker.miranda.ui.screens.SelectTimeZone
import com.cashflowtracker.miranda.utils.AccountType
import com.cashflowtracker.miranda.utils.Coordinates
import com.cashflowtracker.miranda.utils.Currencies
import com.cashflowtracker.miranda.utils.LocationService
import com.cashflowtracker.miranda.utils.Notifications
import com.cashflowtracker.miranda.utils.PermissionStatus
import com.cashflowtracker.miranda.utils.Repeats
import com.cashflowtracker.miranda.utils.StartMonitoringResult
import com.cashflowtracker.miranda.utils.TimeZoneEntry
import com.cashflowtracker.miranda.utils.formatAmount
import com.cashflowtracker.miranda.utils.formatDestination
import com.cashflowtracker.miranda.utils.formatSource
import com.cashflowtracker.miranda.utils.rememberPermission
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.Locale

@Composable
fun DateTimeForm(
    selectedDate: MutableState<String>,
    selectedTime: MutableState<String>
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .offset(y = (0).dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.ic_schedule),
            contentDescription = stringResource(R.string.date_and_time),
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.width(4.dp))
        DatePicker(selectedDate = selectedDate)
        Spacer(modifier = Modifier.weight(1f))
        TimePicker(selectedTime = selectedTime)
    }
}

@Composable
fun TimeZoneForm(
    selectedTimeZone: MutableState<TimeZoneEntry>,
    timezoneLauncher: ManagedActivityResultLauncher<Intent, ActivityResult>,
    date: String,
    time: String,
) {
    val context = LocalContext.current
    val dateTime = combineDateAndTime(date, time)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                val intent = Intent(context, SelectTimeZone::class.java)
                intent.putExtra("dateTimeMillis", dateTime.time)
                timezoneLauncher.launch(intent)
            }
            .height(50.dp)
            .padding(horizontal = 16.dp)
            .offset(y = (0).dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.ic_public),
            contentDescription = stringResource(R.string.time_zone),
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.width(16.dp))
        TimeZonePicker(selectedTimeZone, date, time)
    }
    HorizontalDivider(modifier = Modifier.padding(top = 8.dp))
}

@Composable
fun RepeatForm(selectedRepeat: MutableState<Repeats>) {
    val isRepeatPickerVisible = remember { mutableStateOf(false) }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                isRepeatPickerVisible.value = true
            }
            .height(50.dp)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.ic_replay),
            contentDescription = stringResource(R.string.repeat),
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier
                .scale(scaleX = -1f, scaleY = 1f)   // Flip horizontally
                .rotate(-45f)
        )
        Spacer(modifier = Modifier.width(16.dp))
        RepeatPicker(isRepeatPickerVisible, selectedRepeat)
    }
}

@Composable
fun CreateFirstOccurrenceForm(isCreateFirstOccurrence: MutableState<Boolean>) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                isCreateFirstOccurrence.value = !isCreateFirstOccurrence.value
            }
            .height(50.dp)
            .padding(horizontal = 16.dp)
            .padding(start = 27.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = stringResource(R.string.create_first_occurrence),
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Normal
            )
        )
        Spacer(modifier = Modifier.weight(1f))
        Switch(
            checked = isCreateFirstOccurrence.value,
            onCheckedChange = {
                isCreateFirstOccurrence.value = !isCreateFirstOccurrence.value
            }
        )
    }
    HorizontalDivider(modifier = Modifier.padding(top = 8.dp))
}

@Composable
fun NotificationsForm(notifications: SnapshotStateList<Notifications>) {
    val isNotificationPickerVisible = remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 0.dp)
            .padding(bottom = 8.dp),
        verticalArrangement = Arrangement.Top
    ) {
        if (notifications.size == 0) {
            NotificationPicker(isNotificationPickerVisible, showIcon = true)
        } else {
            Column(modifier = Modifier.fillMaxWidth()) {
                if (notifications.isNotEmpty()) {
                    notifications.forEachIndexed() { index, notification ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .padding(horizontal = 16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if (index == 0) {
                                NotificationIcon()
                            } else {
                                Spacer(modifier = Modifier.width(40.dp))
                            }
                            Text(
                                notification.label,
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Normal
                                )
                            )    // TODO: Replace with DropdownMenu listing notification options
                            Spacer(modifier = Modifier.weight(1f))
                            IconButton(
                                onClick = { notifications.remove(notification) }
                            ) {
                                Icon(
                                    ImageVector.vectorResource(R.drawable.ic_close),
                                    contentDescription = "Delete Notification"
                                )
                            }
                        }
                    }
                }

                // Max 5 notifications for each recurrence
                if (notifications.size < 5) {
                    NotificationPicker(isNotificationPickerVisible)
                }
            }
        }

        if (isNotificationPickerVisible.value) {
            NotificationPickerDialog(
                onDismiss = { isNotificationPickerVisible.value = false },
                onNotificationSelected = { notification ->
                    notifications.add(notification)
                    isNotificationPickerVisible.value = false
                },
                selectedNotifications = notifications
            )

        }

    }
    HorizontalDivider()
}

@Composable
fun SourceForm(
    source: String,
    sourceIcon: Int?,
    transactionType: String,
    sourceLauncher: ManagedActivityResultLauncher<Intent, ActivityResult>
) {
    val context = LocalContext.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable {
                val intent =
                    Intent(
                        context,
                        SelectSource::class.java
                    )
                intent.putExtra("transactionType", transactionType)
                sourceLauncher.launch(intent)
            }
            .height(50.dp)
            .padding(horizontal = 16.dp)
            .offset(y = (0).dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val label = stringResource(R.string.source)
        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.ic_logout),
            contentDescription = label,
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.width(16.dp))

        Text(
            text = if (source.isNotEmpty()) {
                formatSource(source, transactionType)
            } else {
                label
            },
            color = if (source.isNotEmpty()) {
                MaterialTheme.colorScheme.onSurface
            } else {
                MaterialTheme.colorScheme.onSurfaceVariant
            },
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Normal
            )
        )

        Spacer(modifier = Modifier.weight(1f))

        sourceIcon?.let { iconId ->
            Icon(
                imageVector = ImageVector.vectorResource(
                    iconId
                ),
                contentDescription = source,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(end = 8.dp)
            )
        }

//        Box(
//            contentAlignment = Alignment.Center,
//            modifier = Modifier
//                .fillMaxWidth()
//        ) {
//            TextField(
//                value = formatSource(source, transactionType),
//                onValueChange = { },
//                label = {
//                    if (source.isEmpty()) {
//                        Text(label)
//                    }
//                },
//                readOnly = true,
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(50.dp),
//                trailingIcon = {
//                    sourceIcon?.let { iconId ->
//                        Icon(
//                            imageVector = ImageVector.vectorResource(
//                                iconId
//                            ),
//                            contentDescription = source,
//                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
//                            modifier = Modifier.padding(end = 12.dp)
//                        )
//                    }
//                },
//                textStyle = MaterialTheme.typography.titleMedium.copy(
//                    color = MaterialTheme.colorScheme.onSurface,
//                    fontWeight = FontWeight.Normal,
//                    textAlign = TextAlign.Start
//                ),
//                singleLine = true,
//                enabled = false,
//                colors = OutlinedTextFieldDefaults.colors(
//                    disabledTextColor = MaterialTheme.colorScheme.onSurface,
//                    disabledContainerColor = MaterialTheme.colorScheme.surface,
//                    disabledBorderColor = Color.Transparent,
//                    disabledTrailingIconColor = MaterialTheme.colorScheme.onSurface,
//                    disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
//                )
//            )
//        }
    }
    HorizontalDivider()
//    Row(verticalAlignment = Alignment.CenterVertically) {
//        HorizontalDivider(modifier = Modifier.width(LocalConfiguration.current.screenWidthDp.dp / 2 - (7.5).dp))
//        Box(
//            contentAlignment = Alignment.Center,
//            modifier = Modifier
//                .size(16.dp)
//                .border(
//                    width = (0.5).dp,
//                    color = MaterialTheme.colorScheme.outlineVariant,
//                    shape = CircleShape
//                )
//                .clip(CircleShape)
//                .background(MaterialTheme.colorScheme.surface)
//        ) {
//            Icon(
//                imageVector = ImageVector.vectorResource(R.drawable.ic_arrow_downward),
//                contentDescription = "To",
//                tint = DividerDefaults.color,
//                modifier = Modifier.size(14.dp)
//            )
//        }
//        HorizontalDivider(modifier = Modifier.width(LocalConfiguration.current.screenWidthDp.dp / 2 - (7.5).dp))
//    }
}

@Composable
fun DestinationForm(
    destination: String,
    destinationIcon: Int?,
    transactionType: String,
    destinationLauncher: ManagedActivityResultLauncher<Intent, ActivityResult>
) {
    val context = LocalContext.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable {
                val intent =
                    Intent(
                        context,
                        SelectDestination::class.java
                    )
                intent.putExtra("transactionType", transactionType)
                destinationLauncher.launch(intent)
            }
            .height(50.dp)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val label = stringResource(R.string.destination)
        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.ic_login),
            contentDescription = label,
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.width(16.dp))

        Text(
            text = if (destination.isNotEmpty()) {
                formatDestination(destination, transactionType)
            } else {
                label
            },
            color = if (destination.isNotEmpty()) {
                MaterialTheme.colorScheme.onSurface
            } else {
                MaterialTheme.colorScheme.onSurfaceVariant
            },
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Normal
            )
        )

        Spacer(modifier = Modifier.weight(1f))

        destinationIcon?.let { iconId ->
            Icon(
                imageVector = ImageVector.vectorResource(
                    iconId
                ),
                contentDescription = destination,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(end = 8.dp)
            )
        }

        // Type Field
//        Box(
//            contentAlignment = Alignment.Center,
//            modifier = Modifier
//                .fillMaxWidth()
//        ) {
//            TextField(
//                value = formatDestination(destination, transactionType),
//                onValueChange = { },
//                label = {
//                    if (destination.isEmpty()) {
//                        Text(label)
//                    }
//                },
//                readOnly = true,
//                modifier = Modifier.fillMaxWidth(),
//                trailingIcon = {
//                    destinationIcon?.let { iconId ->
//                        Icon(
//                            imageVector = ImageVector.vectorResource(
//                                iconId
//                            ),
//                            contentDescription = destination,
//                            modifier = Modifier.padding(end = 12.dp)
//                        )
//                    }
//                },
//                singleLine = true,
//                enabled = false,
//                colors = OutlinedTextFieldDefaults.colors(
//                    disabledTextColor = MaterialTheme.colorScheme.onSurface,
//                    disabledContainerColor = MaterialTheme.colorScheme.surface,
//                    disabledBorderColor = Color.Transparent,
//                    disabledTrailingIconColor = MaterialTheme.colorScheme.onSurface,
//                    disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
//                )
//            )
//        }
    }
    HorizontalDivider()
}

@Composable
fun AmountForm(
    amount: MutableDoubleState,
    currency: MutableState<Currencies>,
    launcher: ManagedActivityResultLauncher<Intent, ActivityResult>
) {
    val context = LocalContext.current
    val locale = Locale.getDefault()
    val numberFormat = NumberFormat.getNumberInstance(locale) as DecimalFormat

    numberFormat.isGroupingUsed = true // Use thousand separators
    numberFormat.minimumFractionDigits = if (currency.value.showDecimals) 2 else 0
    numberFormat.maximumFractionDigits = if (currency.value.showDecimals) 2 else 0

    var rawInput by remember {
        mutableStateOf(
            if (amount.doubleValue != 0.0) {
                numberFormat.format(amount.doubleValue)
            } else {
                ""
            }
        )
    }

    var isFocused by remember { mutableStateOf(false) }

    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .offset(y = (-4).dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val label = stringResource(R.string.amount)
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_payments),
                contentDescription = label,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.width(16.dp))
            // Amount Field
            OutlinedTextField(
                value = if (rawInput.isEmpty() && amount.doubleValue == 0.0) {
                    "" // Show placeholder when input is empty and value is zero
                } else {
                    rawInput + " ${currency.value.symbol}"
                },
                onValueChange = { text ->
                    val showDecimals = currency.value.showDecimals
                    val sanitizedText = text
                        .replace(currency.value.symbol, "")
                        .trim()
                    val currentSeparator = numberFormat.decimalFormatSymbols.decimalSeparator
                    val previousRawInput = rawInput
                    if (previousRawInput.contains(currentSeparator)
                        && !sanitizedText.contains(currentSeparator)
                    ) {
                        rawInput = previousRawInput
                        return@OutlinedTextField
                    }
                    val parsedValue = try {
                        numberFormat.parse(sanitizedText)?.toDouble()
                    } catch (e: Exception) {
                        null
                    }
                    if (parsedValue == null) {
                        // Reset input and value if parsing fails
                        rawInput = ""
                        amount.doubleValue = 0.0
                        return@OutlinedTextField
                    }
                    rawInput =
                        if (parsedValue == 0.0) {
                            ""
                        } else if (showDecimals && (amount.doubleValue != 0.0 || rawInput.isNotEmpty())) {
                            numberFormat.format(parsedValue)
                        } else {
                            numberFormat.format(parsedValue.toInt())
                        }
                    amount.doubleValue = parsedValue
                },
                trailingIcon = {
                    FilledTonalButton(
                        onClick = {
                            val intent = Intent(context, SelectCurrency::class.java)
                            launcher.launch(intent)
                        },
                        content = { Text(currency.value.name) },
                        modifier = Modifier
                            .padding(end = 0.dp)
                            .height(36.dp)
                            .offset(x = 16.dp),
                        contentPadding = PaddingValues(horizontal = 0.dp, vertical = 0.dp)
                    )
                },
                placeholder = {
                    if (!isFocused) {
                        Text(label)
                    } else {
                        if (currency.value.showDecimals) {
                            Text(numberFormat.format(0.0) + " ${currency.value.symbol}")
                        } else {
                            Text("0 ${currency.value.symbol}")
                        }
                    }
                },
//                placeholder = {
//                    if (currency.value.showDecimals) {
//                        Text(numberFormat.format(0.0) + " ${currency.value.symbol}")
//                    } else {
//                        Text("0 ${currency.value.symbol}")
//                    }
//                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier
                    .fillMaxWidth()
                    .onFocusChanged { focusState ->
                        isFocused = focusState.isFocused
                    }
                    .offset(x = (-16).dp, y = (0).dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                    focusedLabelColor = Color.Transparent,
                    unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
        }
        if (currency.value.presets.isNotEmpty()) {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .padding(start = 0.dp)
                    .offset(y = (-8).dp)
            ) {
                item {
                    Spacer(modifier = Modifier.width(32.dp))
                }
                items(currency.value.presets) {
                    val selected =
                        amount.doubleValue % 1.0 == 0.0 && it == amount.doubleValue.toInt()
                    InputChip(
                        onClick = {
                            if (!selected) {
                                amount.doubleValue = it.toDouble()
                                rawInput = numberFormat.format(it.toDouble())
                            } else {
                                amount.doubleValue = 0.0
                                rawInput = ""
                            }
                        },
                        label = {
                            Text(
                                text = it.toString(),
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .wrapContentWidth(Alignment.CenterHorizontally)
                                    .padding(horizontal = 8.dp)
                            )
                        },
                        selected = selected,
                        modifier = Modifier.widthIn(min = 48.dp)
                    )
                }
            }
        } else {
            Spacer(modifier = Modifier.height(5.dp))
        }
    }
    HorizontalDivider(modifier = Modifier.offset(y = (-4).dp))
}

@Composable
fun CommentForm(comment: MutableState<String>, suggestions: List<String>) {
//    var isFocused by remember { mutableStateOf(false) }

    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .offset(y = (0).dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val label = stringResource(R.string.comment)
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_chat),
                contentDescription = label,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.width(16.dp))
            // Comment Field
            OutlinedTextField(
                value = comment.value,
                onValueChange = { text -> comment.value = text },
                placeholder = { Text(label) },
                modifier = Modifier
                    .fillMaxWidth()
//                    .onFocusChanged { focusState ->
//                        isFocused = focusState.isFocused
//                    }
                    .offset(x = (-16).dp, y = (0).dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                    focusedLabelColor = Color.Transparent,
                    unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
        }
        if (suggestions.isNotEmpty()) {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.offset(y = (-4).dp)
            ) {
                item {
                    Spacer(modifier = Modifier.width(32.dp))
                }
                items(suggestions) {
                    val selected = comment.value == it
                    InputChip(
                        onClick = {
                            if (!selected) {
                                comment.value = it
                            } else {
                                comment.value = ""
                            }
                        },
                        label = {
                            Text(
                                text = it,
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .wrapContentWidth(Alignment.CenterHorizontally)
                                    .padding(horizontal = 8.dp)
                            )
                        },
                        selected = selected,
                        modifier = Modifier
                            .widthIn(min = 48.dp)
                            .padding(bottom = 2.dp)
                    )
                }
            }
        } else {
            Spacer(modifier = Modifier.height(5.dp))
        }
    }
    HorizontalDivider(modifier = Modifier.offset(y = (-2).dp))
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
            .padding(horizontal = 16.dp)
            .offset(y = (0).dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.ic_location_on),
            contentDescription = stringResource(R.string.location),
            tint = MaterialTheme.colorScheme.onSurfaceVariant
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

    if (coordinates.value != null) {
        MapScreen(
            latitude = coordinates.value?.latitude ?: 0.0,
            longitude = coordinates.value?.longitude ?: 0.0,
            isLocationLoaded = isLocationLoaded,
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .padding(top = 6.dp, bottom = 1.dp)
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
        val title = stringResource(R.string.title)
        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.ic_match_case),
            contentDescription = title,
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.width(16.dp))
        // Title Field
        OutlinedTextField(
            value = accountTitle.value,
            onValueChange = { text -> accountTitle.value = text },
            label = { Text(title) },
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
        val type = stringResource(R.string.type)
        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.ic_category),
            contentDescription = type,
            tint = MaterialTheme.colorScheme.onSurfaceVariant
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
                value = AccountType.getType(accountType),
                onValueChange = { },
                label = { Text(type) },
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