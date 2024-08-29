package com.cashflowtracker.miranda.ui.screens

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import android.provider.Settings
import com.cashflowtracker.miranda.R
import com.cashflowtracker.miranda.data.database.Transaction
import com.cashflowtracker.miranda.data.repositories.LoginRepository.getCurrentUserId
import com.cashflowtracker.miranda.ui.composables.SegmentedButtonType
import com.cashflowtracker.miranda.ui.composables.MapScreen
import com.cashflowtracker.miranda.ui.composables.DatePicker
import com.cashflowtracker.miranda.ui.composables.LocationTextField
import com.cashflowtracker.miranda.ui.composables.TimePicker
import com.cashflowtracker.miranda.ui.composables.TimeZonePicker
import com.cashflowtracker.miranda.ui.theme.MirandaTheme
import com.cashflowtracker.miranda.ui.viewmodels.AccountsViewModel
import com.cashflowtracker.miranda.ui.viewmodels.TransactionsViewModel
import com.cashflowtracker.miranda.utils.Coordinates
import com.cashflowtracker.miranda.utils.LocationService
import com.cashflowtracker.miranda.utils.PermissionStatus
import com.cashflowtracker.miranda.utils.StartMonitoringResult
import com.cashflowtracker.miranda.utils.buildZonedDateTime
import com.cashflowtracker.miranda.utils.rememberPermission
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import java.util.UUID

class AddTransaction : ComponentActivity() {
    private lateinit var locationService: LocationService

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        locationService = LocationService(this)
        setContent {
//            val navController = rememberNavController()
            val scrollState = rememberScrollState()
            val coroutineScope = rememberCoroutineScope()
            val context = LocalContext.current
            val transactionType = remember { mutableStateOf("") }
            val selectedDate = remember { mutableStateOf("") }
            val selectedTime = remember { mutableStateOf("") }
            val selectedTimeZone = remember { mutableStateOf("") }
            var source by remember { mutableStateOf("") }
            var sourceIcon by remember { mutableStateOf<Int?>(null) }
            var destination by remember { mutableStateOf("") }
            var destinationIcon by remember { mutableStateOf<Int?>(null) }
            val amount = remember { mutableDoubleStateOf(0.0) }
            val comment = remember { mutableStateOf("") }
            val location = remember { mutableStateOf("") }
            val coordinates = remember { mutableStateOf<Coordinates?>(null) }
            val isError =
                remember { mutableStateOf(false) }   // Check if manually entered coordinates are valid

            val sourceLauncher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.StartActivityForResult()
            ) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    source = result.data?.getStringExtra("sourceTitle") ?: ""
                    sourceIcon = result.data?.getStringExtra("sourceIcon")?.toInt()
                }
            }

            val destinationLauncher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.StartActivityForResult()
            ) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    destination = result.data?.getStringExtra("destinationTitle") ?: ""
                    destinationIcon = result.data?.getStringExtra("destinationIcon")?.toInt()
                }
            }
            val isFormValid by remember {
                derivedStateOf {
                    transactionType.value.isNotEmpty()
                            && selectedDate.value.isNotEmpty()
                            && selectedTime.value.isNotEmpty()
                            && selectedTimeZone.value.isNotEmpty()
                            && source.isNotEmpty()
                            && destination.isNotEmpty()
                            && !isError.value
                }
            }
            val vm = koinViewModel<TransactionsViewModel>()
            val accountsVm = koinViewModel<AccountsViewModel>()

            LaunchedEffect(key1 = transactionType.value) {
                // Clear dependent fields when transactionType changes
                source = ""
                sourceIcon = null
                destination = ""
                destinationIcon = null
            }

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

            MirandaTheme {
                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = { Text("") },
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
                                        ImageVector.vectorResource(R.drawable.ic_close),
                                        contentDescription = "Close"
                                    )
                                }
                            },
                            actions = {
                                Button(
                                    onClick = {
                                        coroutineScope.launch(Dispatchers.IO) {
                                            val userId = context.getCurrentUserId()
                                            val zonedDateTime = buildZonedDateTime(
                                                selectedDate.value,
                                                selectedTime.value,
                                                selectedTimeZone.value
                                            )
                                            val formattedDateTime =
                                                zonedDateTime?.format(DateTimeFormatter.ISO_ZONED_DATE_TIME)
                                                    ?: ""

                                            vm.actions.addTransaction(
                                                Transaction(
                                                    type = transactionType.value,
                                                    dateTime = formattedDateTime,
                                                    source = source,
                                                    destination = destination,
                                                    amount = amount.doubleValue,
                                                    currency = "EUR",
                                                    comment = comment.value,
                                                    location = location.value,
                                                    userId = userId
                                                )
                                            )

                                            if (amount.doubleValue != 0.0) {
                                                when (transactionType.value) {
                                                    "Output" -> {
                                                        val sourceAccountId =
                                                            accountsVm.actions.getByTitleOrNull(
                                                                source,
                                                                userId
                                                            )?.accountId
                                                        if (sourceAccountId != null) {
                                                            accountsVm.actions.updateBalance(
                                                                sourceAccountId,
                                                                -amount.doubleValue
                                                            )
                                                        }
                                                    }

                                                    "Input" -> {
                                                        val destinationAccountId =
                                                            accountsVm.actions.getByTitleOrNull(
                                                                destination,
                                                                userId
                                                            )?.accountId
                                                        if (destinationAccountId != null) {
                                                            accountsVm.actions.updateBalance(
                                                                destinationAccountId,
                                                                amount.doubleValue
                                                            )
                                                        }
                                                    }

                                                    "Transfer" -> {
                                                        val sourceAccountId =
                                                            accountsVm.actions.getByTitleOrNull(
                                                                source,
                                                                userId
                                                            )?.accountId
                                                        val destinationAccountId =
                                                            accountsVm.actions.getByTitleOrNull(
                                                                destination,
                                                                userId
                                                            )?.accountId
                                                        if (sourceAccountId != null && destinationAccountId != null) {
                                                            accountsVm.actions.updateBalance(
                                                                sourceAccountId,
                                                                -amount.doubleValue
                                                            )
                                                            accountsVm.actions.updateBalance(
                                                                destinationAccountId,
                                                                amount.doubleValue
                                                            )
                                                        }
                                                    }
                                                }
                                            }
                                            finish()
                                        }
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                                    enabled = isFormValid,
                                    modifier = Modifier
                                        .padding(top = 16.dp, bottom = 16.dp, end = 16.dp)
                                        .height(32.dp),
                                    contentPadding = PaddingValues(
                                        horizontal = 12.dp,
                                        vertical = 5.dp
                                    )
                                ) {
                                    Text(
                                        text = "Create",
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                        style = MaterialTheme.typography.labelLarge,
                                        modifier = Modifier.padding(0.dp)
                                    )
                                }
                            }
                        )
                    }
                ) { paddingValues ->
                    Column(
                        modifier = Modifier
                            .padding(paddingValues)
                            .padding(16.dp)
                            .verticalScroll(scrollState)
                    ) {
                        SegmentedButtonType(
                            transactionType = transactionType,
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Date, Time and TimeZone with icons and click actions
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
                        //Spacer(modifier = Modifier.height(8.dp))

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
                                            this@AddTransaction,
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
                        Spacer(modifier = Modifier.height(8.dp))

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
                                            this@AddTransaction,
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
                        Spacer(modifier = Modifier.height(8.dp))

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
                        Spacer(modifier = Modifier.height(16.dp))

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
                        Spacer(modifier = Modifier.height(16.dp))

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
                    }
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
        }
    }

    override fun onPause() {
        super.onPause()
        locationService.pauseLocationRequest()
    }

    override fun onResume() {
        super.onResume()
        locationService.resumeLocationRequest()
    }
}