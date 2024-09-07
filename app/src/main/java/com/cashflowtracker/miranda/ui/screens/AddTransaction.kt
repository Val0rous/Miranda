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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.cashflowtracker.miranda.R
import com.cashflowtracker.miranda.data.database.Transaction
import com.cashflowtracker.miranda.data.repositories.LoginRepository.getCurrentUserId
import com.cashflowtracker.miranda.ui.composables.AmountForm
import com.cashflowtracker.miranda.ui.composables.CommentForm
import com.cashflowtracker.miranda.ui.composables.SegmentedButtonType
import com.cashflowtracker.miranda.ui.composables.MapScreen
import com.cashflowtracker.miranda.ui.composables.DateTimeForm
import com.cashflowtracker.miranda.ui.composables.DestinationForm
import com.cashflowtracker.miranda.ui.composables.LocationForm
import com.cashflowtracker.miranda.ui.composables.SourceForm
import com.cashflowtracker.miranda.ui.composables.TimeZoneForm
import com.cashflowtracker.miranda.ui.theme.MirandaTheme
import com.cashflowtracker.miranda.ui.viewmodels.AccountsViewModel
import com.cashflowtracker.miranda.ui.viewmodels.TransactionsViewModel
import com.cashflowtracker.miranda.utils.Coordinates
import com.cashflowtracker.miranda.utils.LocationService
import com.cashflowtracker.miranda.utils.buildZonedDateTime
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import java.time.format.DateTimeFormatter

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
                        DateTimeForm(selectedDate, selectedTime)
                        //Spacer(modifier = Modifier.height(8.dp))

                        TimeZoneForm(selectedTimeZone)

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

    override fun onPause() {
        super.onPause()
        locationService.pauseLocationRequest()
    }

    override fun onResume() {
        super.onResume()
        locationService.resumeLocationRequest()
    }
}