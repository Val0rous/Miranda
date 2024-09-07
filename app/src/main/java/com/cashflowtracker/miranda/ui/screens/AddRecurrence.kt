package com.cashflowtracker.miranda.ui.screens

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.cashflowtracker.miranda.R
import com.cashflowtracker.miranda.ui.composables.SegmentedButtonType
import com.cashflowtracker.miranda.ui.composables.MapScreen
import com.cashflowtracker.miranda.ui.composables.DatePicker
import com.cashflowtracker.miranda.ui.composables.TimePicker
import com.cashflowtracker.miranda.ui.composables.TimeZonePicker
import com.cashflowtracker.miranda.ui.theme.MirandaTheme

class AddRecurrence : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
//            val navController = rememberNavController()
            val scrollState = rememberScrollState()
            val transactionType = remember { mutableStateOf("") }

            // State to manage date selection
            val selectedDate = remember { mutableStateOf("") }
            val selectedTime = remember { mutableStateOf("") }
            val selectedTimeZone = remember { mutableStateOf("") }

            // State to manage selectors
            val source = remember { mutableStateOf("") }
            val destination = remember { mutableStateOf("") }
            val amount = remember { mutableStateOf("") }
            val comment = remember { mutableStateOf("") }
            val location = remember { mutableStateOf("") }

            val isSourceExpanded = remember { mutableStateOf(false) }
            val isDestinationExpanded = remember { mutableStateOf(false) }
            val isCreateFirstOccurrence = remember { mutableStateOf(false) }

            val sourceOptions = listOf("Deutsche Bank", "N26", "Wallet")
            val destinationOptions = listOf("Restaurant", "Food", "Clothing")
            val amountOptions = listOf("10 €", "20 €", "50 €")

            val isTypeChanged = remember { mutableStateOf(false) }

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
                                    onClick = { /* Logic to create the recurrence */ },
                                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
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
                            isTypeChanged = isTypeChanged,
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

                        Spacer(modifier = Modifier.height(8.dp))

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
                            TextButton(onClick = { /* TODO: Handle repeat button click */ }) {
                                Text("Every month") // TODO: Replace with DropdownMenu listing repeat options
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Spacer(modifier = Modifier.width(28.dp))
                            Text("Create First Occurrence")
                            Checkbox(
                                checked = isCreateFirstOccurrence.value,
                                onCheckedChange = {
                                    isCreateFirstOccurrence.value = !isCreateFirstOccurrence.value
                                })
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                ImageVector.vectorResource(R.drawable.ic_notifications),
                                contentDescription = "Notifications"
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("1 day before")    // TODO: Replace with DropdownMenu listing notification options
                            Spacer(modifier = Modifier.weight(1f))
                            Icon(
                                ImageVector.vectorResource(R.drawable.ic_close),
                                contentDescription = "Delete Notification"
                            )
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
                            // Source Field with DropdownMenu
                            ExposedDropdownMenuBox(
                                expanded = isSourceExpanded.value,
                                onExpandedChange = {
                                    isSourceExpanded.value = !isSourceExpanded.value
                                }
                            ) {
                                OutlinedTextField(
                                    value = source.value,
                                    onValueChange = {},
                                    label = { Text("Source") },
                                    leadingIcon = {
                                        Icon(
                                            imageVector = ImageVector.vectorResource(R.drawable.ic_account_balance),
                                            contentDescription = "Source"
                                        )
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .menuAnchor(),
                                    readOnly = true,
                                    trailingIcon = {
                                        ExposedDropdownMenuDefaults.TrailingIcon(
                                            expanded = isSourceExpanded.value
                                        )
                                    }
                                )
                                ExposedDropdownMenu(
                                    expanded = isSourceExpanded.value,
                                    onDismissRequest = { isSourceExpanded.value = false }
                                ) {
                                    sourceOptions.forEach { option ->
                                        DropdownMenuItem(
                                            onClick = {
                                                source.value = option
                                                isSourceExpanded.value = false
                                            },
                                            text = { Text(option) }
                                        )
                                    }
                                }
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
                            // Destination Field with DropdownMenu
                            ExposedDropdownMenuBox(
                                expanded = isDestinationExpanded.value,
                                onExpandedChange = {
                                    isDestinationExpanded.value = !isDestinationExpanded.value
                                }
                            ) {
                                OutlinedTextField(
                                    value = destination.value,
                                    onValueChange = {},
                                    label = { Text("Destination") },
                                    leadingIcon = {
                                        Icon(
                                            imageVector = ImageVector.vectorResource(R.drawable.ic_restaurant),
                                            contentDescription = "Destination"
                                        )
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .menuAnchor(),
                                    readOnly = true,
                                    trailingIcon = {
                                        ExposedDropdownMenuDefaults.TrailingIcon(
                                            expanded = isDestinationExpanded.value
                                        )
                                    }
                                )
                                ExposedDropdownMenu(
                                    expanded = isDestinationExpanded.value,
                                    onDismissRequest = { isDestinationExpanded.value = false },

                                    ) {
                                    destinationOptions.forEach { option ->
                                        DropdownMenuItem(
                                            onClick = {
                                                destination.value = option
                                                isDestinationExpanded.value = false
                                            },
                                            text = { Text(option) }
                                        )
                                    }
                                }
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

                        val isLocationLoaded = remember { mutableStateOf(false) }
                        MapScreen(44.2625, 12.3487, isLocationLoaded)
                    }
                }
            }
        }
    }
}