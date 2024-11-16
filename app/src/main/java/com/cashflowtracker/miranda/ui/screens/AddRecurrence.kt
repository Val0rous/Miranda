package com.cashflowtracker.miranda.ui.screens

import android.app.Activity
import android.os.Build
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
import com.cashflowtracker.miranda.data.database.Notification
import com.cashflowtracker.miranda.data.database.Recurrence
import com.cashflowtracker.miranda.data.database.Transaction
import com.cashflowtracker.miranda.data.repositories.LoginRepository.getCurrentUserId
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
import com.cashflowtracker.miranda.ui.composables.getTimeZoneInGMTFormat
import com.cashflowtracker.miranda.ui.theme.MirandaTheme
import com.cashflowtracker.miranda.ui.viewmodels.AccountsViewModel
import com.cashflowtracker.miranda.ui.viewmodels.NotificationsViewModel
import com.cashflowtracker.miranda.ui.viewmodels.RecurrencesViewModel
import com.cashflowtracker.miranda.ui.viewmodels.TransactionsViewModel
import com.cashflowtracker.miranda.utils.Currencies
import com.cashflowtracker.miranda.utils.LocationService
import com.cashflowtracker.miranda.utils.Notifications
import com.cashflowtracker.miranda.utils.Repeats
import com.cashflowtracker.miranda.utils.TimeZoneEntry
import com.cashflowtracker.miranda.utils.buildZonedDateTime
import com.cashflowtracker.miranda.utils.calculateBalance
import com.cashflowtracker.miranda.utils.getNotificationTime
import com.cashflowtracker.miranda.utils.getRepeatTime
import com.cashflowtracker.miranda.utils.getSuggestions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale
import java.util.TimeZone

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
            val recurrencesVm = koinViewModel<RecurrencesViewModel>()
            val notificationsVm = koinViewModel<NotificationsViewModel>()
            val transactionsVm = koinViewModel<TransactionsViewModel>()
            val accountsVm = koinViewModel<AccountsViewModel>()
            val transactionType = remember { mutableStateOf("") }
            val selectedDate = remember { mutableStateOf("") }
            val selectedTime = remember { mutableStateOf("") }
            val selectedTimeZone = remember {
                val currentTimeZoneId = TimeZone.getDefault().id
                val currentDateTime = Date()
                val inDaylightTime = TimeZone.getDefault().inDaylightTime(currentDateTime)
                val displayName = TimeZone.getDefault()
                    .getDisplayName(inDaylightTime, TimeZone.LONG, Locale.getDefault())
                val gmtFormat = getTimeZoneInGMTFormat(currentTimeZoneId, currentDateTime)
                mutableStateOf(
                    TimeZoneEntry(
                        id = currentTimeZoneId,
                        displayName = displayName,
                        gmtFormat = gmtFormat,
                        country = "Earth"
                    )
                )
            }
            val selectedRepeat = remember { mutableStateOf(Repeats.MONTHLY) }
            val notifications = remember { mutableStateListOf<Notifications>() }
            var source by remember { mutableStateOf("") }
            var sourceIcon by remember { mutableStateOf<Int?>(null) }
            var destination by remember { mutableStateOf("") }
            var destinationIcon by remember { mutableStateOf<Int?>(null) }
            val amount = remember { mutableDoubleStateOf(0.0) }
            val currency = remember { mutableStateOf(Currencies.EUR) }
            val comment = remember { mutableStateOf("") }
            val allRecurrencesFlow =
                remember { recurrencesVm.actions.getAllByUserIdFlow(context.getCurrentUserId()) }
            val allRecurrences by allRecurrencesFlow.collectAsState(initial = emptyList())
            val suggestions = remember(allRecurrences, transactionType.value, source, destination) {
                getSuggestions(allRecurrences, transactionType, source, destination)
            }
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

            val currencyLauncher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.StartActivityForResult()
            ) { result ->
                if (result.resultCode == RESULT_OK) {
                    val selectedCurrency =
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            result.data?.getSerializableExtra("currency", Currencies::class.java)
                        } else {
                            @Suppress("DEPRECATION")
                            result.data?.getSerializableExtra("currency") as? Currencies
                        }

                    currency.value = selectedCurrency ?: Currencies.EUR
                }
            }

            val timezoneLauncher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.StartActivityForResult()
            ) { result ->
                if (result.resultCode == RESULT_OK) {
                    val timezone =
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            result.data?.getSerializableExtra("timezone", TimeZoneEntry::class.java)
                        } else {
                            @Suppress("DEPRECATION")
                            result.data?.getSerializableExtra("timezone") as? TimeZoneEntry
                        }

                    if (timezone != null) {
                        selectedTimeZone.value = timezone
                    }
                }
            }

            val isFormValid by remember {
                derivedStateOf {
                    transactionType.value.isNotEmpty()
                            && selectedDate.value.isNotEmpty()
                            && selectedTime.value.isNotEmpty()
                            && source.isNotEmpty()
                            && destination.isNotEmpty()
                            && !isError.value
                }
            }

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
                            onButtonClick = {
                                coroutineScope.launch(Dispatchers.IO) {
                                    val userId = context.getCurrentUserId()
                                    val zonedDateTime = buildZonedDateTime(
                                        selectedDate.value,
                                        selectedTime.value,
                                        selectedTimeZone.value.id
                                    )
                                    val createdOn =
                                        zonedDateTime?.format(DateTimeFormatter.ISO_ZONED_DATE_TIME)
                                            ?: ""

                                    val repeatsOn =
                                        getRepeatTime(zonedDateTime!!, selectedRepeat.value)

                                    if (isCreateFirstOccurrence.value) {
                                        transactionsVm.actions.addTransaction(
                                            Transaction(
                                                type = transactionType.value,
                                                createdOn = createdOn,
                                                source = source,
                                                destination = destination,
                                                amount = amount.doubleValue,
                                                currency = currency.value.name,
                                                comment = comment.value,
                                                location = location.value,
                                                userId = userId
                                            )
                                        )

                                        calculateBalance(
                                            amount.doubleValue,
                                            currency.value,
                                            transactionType.value,
                                            source,
                                            destination,
                                            accountsVm,
                                            userId
                                        )
                                    }

                                    val recurrence = Recurrence(
                                        type = transactionType.value,
                                        createdOn = createdOn,
                                        source = source,
                                        destination = destination,
                                        amount = amount.doubleValue,
                                        currency = "EUR",
                                        comment = comment.value,
                                        location = location.value,
                                        repeatIntervalMillis = selectedRepeat.value.time,
                                        reoccursOn = repeatsOn,
                                        userId = userId
                                    )

                                    recurrencesVm.actions.addRecurrence(recurrence)
                                    notificationsVm.actions.removeAllByRecurrenceId(recurrence.recurrenceId)
                                    notifications.forEach {
                                        val dateTime = getNotificationTime(
                                            ZonedDateTime.parse(recurrence.reoccursOn),
                                            it
                                        )
//                                            addMillisToTime(ZonedDateTime.parse(recurrence.reoccursOn), -it.time)
                                        notificationsVm.actions.addNotification(
                                            Notification(
                                                recurrenceId = recurrence.recurrenceId,
                                                dateTime = dateTime,
                                                userId = userId
                                            )
                                        )
                                    }
//                                    notifications1 = notifications[0].label,
//                                    notifications2 = notifications[1].label,
//                                    notifications3 = notifications[2].label,
//                                    notifications4 = notifications[3].label,
//                                    notifications5 = notifications[4].label,

                                    finish()
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
                            isTypeChanged = isTypeChanged,
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Date, Time and TimeZone with icons and click actions
                        DateTimeForm(selectedDate, selectedTime)
                        //Spacer(modifier = Modifier.height(8.dp))

                        TimeZoneForm(
                            selectedTimeZone,
                            timezoneLauncher,
                            selectedDate.value,
                            selectedTime.value
                        )

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

                        AmountForm(amount, currency, currencyLauncher)

                        CommentForm(comment, suggestions)

                        LocationForm(location, locationService, isError)
                    }
                }
            }
        }
    }
}