package com.cashflowtracker.miranda.ui.screens

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.cashflowtracker.miranda.R
import com.cashflowtracker.miranda.data.database.Transaction
import com.cashflowtracker.miranda.data.repositories.LoginRepository.getCurrentUserId
import com.cashflowtracker.miranda.ui.composables.AddEditTopAppBar
import com.cashflowtracker.miranda.ui.composables.AmountForm
import com.cashflowtracker.miranda.ui.composables.CommentForm
import com.cashflowtracker.miranda.ui.composables.DatePicker
import com.cashflowtracker.miranda.ui.composables.DateTimeForm
import com.cashflowtracker.miranda.ui.composables.DestinationForm
import com.cashflowtracker.miranda.ui.composables.LocationForm
import com.cashflowtracker.miranda.ui.composables.MapScreen
import com.cashflowtracker.miranda.ui.composables.SegmentedButtonType
import com.cashflowtracker.miranda.ui.composables.SourceForm
import com.cashflowtracker.miranda.ui.composables.TimePicker
import com.cashflowtracker.miranda.ui.composables.TimeZoneForm
import com.cashflowtracker.miranda.ui.composables.TimeZonePicker
import com.cashflowtracker.miranda.ui.theme.MirandaTheme
import com.cashflowtracker.miranda.ui.viewmodels.AccountsViewModel
import com.cashflowtracker.miranda.ui.viewmodels.TransactionsViewModel
import com.cashflowtracker.miranda.utils.DefaultCategories
import com.cashflowtracker.miranda.utils.LocationService
import com.cashflowtracker.miranda.utils.buildZonedDateTime
import com.cashflowtracker.miranda.utils.formatZonedDateTime
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import java.time.format.DateTimeFormatter
import java.util.UUID

class EditTransaction : ComponentActivity() {
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
            val transactionId = remember {
                mutableStateOf(
                    UUID.fromString(
                        intent.getStringExtra("transactionId") ?: ""
                    )
                )
            }
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

            val isFormValid by remember {
                derivedStateOf {
                    transactionType.value.isNotEmpty()
                            && selectedDate.value.isNotEmpty()
                            && selectedTime.value.isNotEmpty()
                            && selectedTimeZone.value.isNotEmpty()
                            && source.isNotEmpty()
                            && destination.isNotEmpty()
                }
            }

            var isLoaded by remember { mutableStateOf(false) }
            val isTypeChanged = remember { mutableStateOf(false) }
            val vm = koinViewModel<TransactionsViewModel>()
            val accountsVm = koinViewModel<AccountsViewModel>()
            var transaction by remember { mutableStateOf<Transaction?>(null) }

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
                        transactionType.value = it.type
                        val dateTimeParts = formatZonedDateTime(it.dateTime).split("\\s+".toRegex())
                            .filter { item -> item.isNotBlank() }
                        selectedDate.value = dateTimeParts[0]
                        selectedTime.value = dateTimeParts[1]
                        selectedTimeZone.value = dateTimeParts[2]
                        source = it.source
                        sourceIcon = DefaultCategories.getIcon(it.source)
                        destination = it.destination
                        destinationIcon = DefaultCategories.getIcon(it.destination)
                        amount.doubleValue = it.amount
                        comment.value = it.comment ?: ""
                        location.value = it.location ?: ""
                        isLoaded = true
                    }
                }
            }

            MirandaTheme {
                if (isLoaded) {
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
                                            selectedTimeZone.value
                                        )
                                        val formattedDateTime =
                                            zonedDateTime?.format(DateTimeFormatter.ISO_ZONED_DATE_TIME)
                                                ?: ""

                                        //TODO: UPDATE transaction
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
    }
}