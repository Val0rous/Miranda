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
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.cashflowtracker.miranda.R
import com.cashflowtracker.miranda.ui.composables.SegmentedButtonType
import com.cashflowtracker.miranda.ui.composables.MapScreen
import com.cashflowtracker.miranda.ui.composables.DatePicker
import com.cashflowtracker.miranda.ui.composables.TimePicker
import com.cashflowtracker.miranda.ui.composables.TimeZonePicker
import com.cashflowtracker.miranda.ui.theme.MirandaTheme

class AddTransaction : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
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
            val amount = remember { mutableStateOf("") }
            val comment = remember { mutableStateOf("") }
            val location = remember { mutableStateOf("") }

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
                                    onClick = { /* Logic to create the transaction */ },
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
                                value = amount.value,
                                onValueChange = { text -> amount.value = text },
                                label = { Text("Amount") },
                                placeholder = { Text("0.00 €") },
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
                            OutlinedTextField(
                                value = location.value,
                                onValueChange = { text -> location.value = text },
                                label = { Text("Location") },
                                leadingIcon = {
                                    Icon(
                                        imageVector = ImageVector.vectorResource(R.drawable.ic_my_location_filled),
                                        contentDescription = "Location"
                                    )
                                },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))

                        MapScreen(44.2625, 12.3487)
                    }
                }
            }
        }
    }
}