package com.cashflowtracker.miranda.ui.screens

import android.app.Activity
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.cashflowtracker.miranda.data.database.Transaction
import com.cashflowtracker.miranda.data.repositories.LoginRepository.getCurrentUserId
import com.cashflowtracker.miranda.ui.composables.AddEditTopAppBar
import com.cashflowtracker.miranda.ui.composables.AmountForm
import com.cashflowtracker.miranda.ui.composables.CommentForm
import com.cashflowtracker.miranda.ui.composables.DateTimeForm
import com.cashflowtracker.miranda.ui.composables.DestinationForm
import com.cashflowtracker.miranda.ui.composables.LocationForm
import com.cashflowtracker.miranda.ui.composables.SegmentedButtonType
import com.cashflowtracker.miranda.ui.composables.SourceForm
import com.cashflowtracker.miranda.ui.composables.TimeZoneForm
import com.cashflowtracker.miranda.ui.composables.getTimeZoneInGMTFormat
import com.cashflowtracker.miranda.ui.theme.MirandaTheme
import com.cashflowtracker.miranda.ui.viewmodels.AccountsViewModel
import com.cashflowtracker.miranda.ui.viewmodels.TransactionsViewModel
import com.cashflowtracker.miranda.utils.CurrencyEnum
import com.cashflowtracker.miranda.utils.DefaultCategories
import com.cashflowtracker.miranda.utils.LocationService
import com.cashflowtracker.miranda.utils.TimeZoneEntry
import com.cashflowtracker.miranda.utils.buildZonedDateTime
import com.cashflowtracker.miranda.utils.calculateBalance
import com.cashflowtracker.miranda.utils.formatZonedDateTime
import com.cashflowtracker.miranda.utils.getSuggestions
import com.cashflowtracker.miranda.utils.revertTransaction
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import java.text.SimpleDateFormat
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import java.util.UUID

class EditTransaction : ComponentActivity() {
    private lateinit var locationService: LocationService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        locationService = LocationService(this)
        setContent {
            val scrollState = rememberScrollState()
            val coroutineScope = rememberCoroutineScope()
            val context = LocalContext.current
            val vm = koinViewModel<TransactionsViewModel>()
            val accountsVm = koinViewModel<AccountsViewModel>()
            var transaction by remember { mutableStateOf<Transaction?>(null) }
            val transactionId = remember {
                mutableStateOf(
                    UUID.fromString(
                        intent.getStringExtra("transactionId") ?: ""
                    )
                )
            }
            val oldTransactionType = remember { mutableStateOf("") }
            val oldSource = remember { mutableStateOf("") }
            val oldDestination = remember { mutableStateOf("") }
            val oldAmount = remember { mutableDoubleStateOf(0.0) }
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
            var source by remember { mutableStateOf("") }
            var sourceIcon by remember { mutableStateOf<Int?>(null) }
            var destination by remember { mutableStateOf("") }
            var destinationIcon by remember { mutableStateOf<Int?>(null) }
            val amount = remember { mutableDoubleStateOf(0.0) }
            val currency = remember { mutableStateOf(CurrencyEnum.EUR) }
            val comment = remember { mutableStateOf("") }
            val allTransactionsFlow =
                remember { vm.actions.getAllByUserIdFlow(context.getCurrentUserId()) }
            val allTransactions by allTransactionsFlow.collectAsState(initial = emptyList())
            val suggestions =
                remember(allTransactions, transactionType.value, source, destination) {
                    getSuggestions(allTransactions, transactionType, source, destination)
                }
            val location = remember { mutableStateOf("") }
            val isError =
                remember { mutableStateOf(false) } // Check if manually entered coordinates are valid

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
                            result.data?.getSerializableExtra("currency", CurrencyEnum::class.java)
                        } else {
                            @Suppress("DEPRECATION")
                            result.data?.getSerializableExtra("currency") as? CurrencyEnum
                        }

                    currency.value = selectedCurrency ?: CurrencyEnum.EUR
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
                }
            }

            var isLoaded by remember { mutableStateOf(false) }
            val isTypeChanged = remember { mutableStateOf(false) }

            LaunchedEffect(key1 = isTypeChanged.value) {
                // Clear dependent fields when transactionType changes
                source = ""
                sourceIcon = null
                destination = ""
                destinationIcon = null
                isTypeChanged.value = false
            }

            LaunchedEffect(Unit) {
                coroutineScope.launch(Dispatchers.IO) {
                    transaction = vm.actions.getByTransactionId(transactionId.value).also {
                        oldTransactionType.value = it.type
                        transactionType.value = it.type

                        try {
                            val zonedDateTime = ZonedDateTime.parse(
                                it.createdOn,
                                DateTimeFormatter.ISO_ZONED_DATE_TIME
                            )

                            // Format date
                            selectedDate.value = zonedDateTime.toLocalDate()
                                .format(
                                    DateTimeFormatter.ofPattern(
                                        "yyyy-MM-dd",
                                        Locale.getDefault()
                                    )
                                )


                            // Format time
                            selectedTime.value = zonedDateTime.toLocalTime()
                                .format(DateTimeFormatter.ofPattern("HH:mm", Locale.getDefault()))

                            // Set time zone
                            val zoneId = zonedDateTime.zone
                            val offset = zonedDateTime.offset
                            val gmtOffset = "GMT${offset.id}" // e.g., "GMT+01:00"

                            selectedTimeZone.value = TimeZoneEntry(
                                id = zoneId.id,
                                displayName = zoneId.id,
                                gmtFormat = gmtOffset,
                                country = "" // You can set this if needed
                            )
                        } catch (e: Exception) {
                            println("Error parsing ZonedDateTime: ${e.message}")
                            // Handle the error by setting default values or showing an error message
                            selectedDate.value = ""
                            selectedTime.value = ""
                            selectedTimeZone.value = TimeZoneEntry(
                                id = "",
                                displayName = "",
                                gmtFormat = "",
                                country = ""
                            )
                        }

//                        val dateTimeParts =
//                            formatZonedDateTime(it.createdOn).split("\\s+".toRegex())
//                                .filter { item -> item.isNotBlank() }
//                        selectedDate.value = SimpleDateFormat(
//                            "yyyy-MM-dd",
//                            Locale.getDefault()
//                        ).parse(dateTimeParts[0])?.let { item ->
//                            SimpleDateFormat("EEE, MMM d, yyyy", Locale.getDefault()).format(item)
//                        } ?: ""
//                        selectedTime.value = dateTimeParts[1]
//                        selectedTimeZone.value = TimeZoneEntry(
//                            id = "",
//                            displayName = "",
//                            gmtFormat = dateTimeParts[2],
//                            country = ""
//                        )
                        oldSource.value = it.source
                        source = it.source
                        sourceIcon = DefaultCategories.getIcon(it.source)
                        oldDestination.value = it.destination
                        destination = it.destination
                        destinationIcon = DefaultCategories.getIcon(it.destination)
                        oldAmount.doubleValue = it.amount
                        amount.doubleValue = it.amount
                        comment.value = it.comment
                        location.value = it.location ?: ""
                        isLoaded = true
                    }
                }
            }

            MirandaTheme {
                Scaffold(
                    topBar = {
                        AddEditTopAppBar(
                            buttonText = "Save",
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
                                    val formattedDateTime =
                                        zonedDateTime?.format(DateTimeFormatter.ISO_ZONED_DATE_TIME)
                                            ?: ""

                                    revertTransaction(
                                        oldAmount.doubleValue,
                                        oldTransactionType.value,
                                        oldSource.value,
                                        oldDestination.value,
                                        accountsVm,
                                        userId
                                    )

                                    vm.actions.updateTransaction(
                                        Transaction(
                                            transactionId = transaction!!.transactionId,
                                            type = transactionType.value,
                                            createdOn = formattedDateTime,
                                            source = source,
                                            destination = destination,
                                            amount = amount.doubleValue,
                                            currency = "EUR",
                                            comment = comment.value,
                                            location = location.value,
                                            userId = transaction!!.userId
                                        )
                                    )
//
                                    calculateBalance(
                                        amount.doubleValue,
                                        transactionType.value,
                                        source,
                                        destination,
                                        accountsVm,
                                        userId
                                    )

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
                        if (isLoaded) {
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
}