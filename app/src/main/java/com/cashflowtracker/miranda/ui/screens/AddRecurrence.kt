package com.cashflowtracker.miranda.ui.screens

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.cashflowtracker.miranda.ui.composables.AddEditTopAppBar
import com.cashflowtracker.miranda.ui.composables.AmountForm
import com.cashflowtracker.miranda.ui.composables.CommentForm
import com.cashflowtracker.miranda.ui.composables.CreateFirstOccurrenceForm
import com.cashflowtracker.miranda.ui.composables.SegmentedButtonType
import com.cashflowtracker.miranda.ui.composables.DateTimeForm
import com.cashflowtracker.miranda.ui.composables.DestinationForm
import com.cashflowtracker.miranda.ui.composables.LocationForm
import com.cashflowtracker.miranda.ui.composables.NotificationsForm
import com.cashflowtracker.miranda.ui.composables.RepeatForm
import com.cashflowtracker.miranda.ui.composables.SourceForm
import com.cashflowtracker.miranda.ui.composables.TimeZoneForm
import com.cashflowtracker.miranda.ui.theme.MirandaTheme
import com.cashflowtracker.miranda.ui.viewmodels.AccountsViewModel
import com.cashflowtracker.miranda.ui.viewmodels.TransactionsViewModel
import com.cashflowtracker.miranda.utils.LocationService
import org.koin.androidx.compose.koinViewModel

class AddRecurrence : ComponentActivity() {
    private lateinit var locationService: LocationService

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        locationService = LocationService(this)
        setContent {
            val scrollState = rememberScrollState()
            val coroutineScope = rememberCoroutineScope()
            val context = LocalContext.current
            val transactionType = remember { mutableStateOf("") }
            val selectedDate = remember { mutableStateOf("") }
            val selectedTime = remember { mutableStateOf("") }
            val selectedTimeZone = remember { mutableStateOf("") }
            val selectedRepeat = remember { mutableStateOf("") }
            val notifications = remember { mutableStateListOf<String>() }
            var source by remember { mutableStateOf("") }
            var sourceIcon by remember { mutableStateOf<Int?>(null) }
            var destination by remember { mutableStateOf("") }
            var destinationIcon by remember { mutableStateOf<Int?>(null) }
            val amount = remember { mutableDoubleStateOf(0.0) }
            val comment = remember { mutableStateOf("") }
            val location = remember { mutableStateOf("") }
            val isError = remember { mutableStateOf(false) }
            val isCreateFirstOccurrence = remember { mutableStateOf(false) }

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

            val isTypeChanged = remember { mutableStateOf(false) }
            LaunchedEffect(key1 = isTypeChanged.value) {
                // Clear dependent fields when transactionType changes
                source = ""
                sourceIcon = null
                destination = ""
                destinationIcon = null
                isTypeChanged.value = false
            }

            MirandaTheme {
                Scaffold(
                    topBar = {
                        AddEditTopAppBar(
                            buttonText = "Create",
                            isButtonEnabled = isFormValid,
                            onIconButtonClick = { finish() },
                            onButtonClick = { /* Logic to create the recurrence */ }
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
                            isTypeChanged = isTypeChanged,
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Date, Time and TimeZone with icons and click actions
                        DateTimeForm(selectedDate, selectedTime)
                        //Spacer(modifier = Modifier.height(8.dp))

                        TimeZoneForm(selectedTimeZone)

                        Spacer(modifier = Modifier.height(8.dp))

                        RepeatForm(selectedRepeat)

                        Spacer(modifier = Modifier.height(0.dp))

                        CreateFirstOccurrenceForm(isCreateFirstOccurrence)

                        Spacer(modifier = Modifier.height(8.dp))

                        NotificationsForm(notifications)

                        SourceForm(source, sourceIcon, transactionType, sourceLauncher)
                        Spacer(modifier = Modifier.height(8.dp))

                        DestinationForm(
                            destination,
                            destinationIcon,
                            transactionType,
                            destinationLauncher
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        AmountForm(amount)
                        Spacer(modifier = Modifier.height(16.dp))

                        CommentForm(comment)
                        Spacer(modifier = Modifier.height(16.dp))

                        LocationForm(location, locationService, isError)
                    }
                }
            }
        }
    }
}