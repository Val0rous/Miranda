package com.cashflowtracker.miranda

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.cashflowtracker.miranda.ui.composables.DatePickerModal
import com.cashflowtracker.miranda.ui.composables.DialWithDialog
import com.cashflowtracker.miranda.ui.composables.SegmentedButtonType
import com.cashflowtracker.miranda.ui.composables.TimePickerDialog
import com.cashflowtracker.miranda.ui.composables.TimeZonePickerDialog
import com.cashflowtracker.miranda.ui.composables.getCurrentTimeZone
import com.cashflowtracker.miranda.utils.MapScreen
import java.text.SimpleDateFormat
import java.util.*
import com.cashflowtracker.miranda.ui.composables.DatePicker
import com.cashflowtracker.miranda.ui.composables.TimePicker
import com.cashflowtracker.miranda.ui.composables.TimeZonePicker

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTransaction(navController: NavHostController) {
    val transactionType = remember { mutableStateOf("") }

    // State to manage date selection
    val selectedDate = remember { mutableStateOf("") }
    val selectedTime = remember { mutableStateOf("") }
    val selectedTimeZone = remember { mutableStateOf("") }

    // State to manage selectors
    val source = remember { mutableStateOf("") }
    val destination = remember { mutableStateOf("") }
    val amount = remember { mutableStateOf("") }
    val location = remember { mutableStateOf("1600 Amphitheatre Pkwy") } // Default placeholder

    val sourceExpanded = remember { mutableStateOf(false) }
    val destinationExpanded = remember { mutableStateOf(false) }
    val amountExpanded = remember { mutableStateOf(false) }

    val sourceOptions = listOf("Deutsche Bank", "Savings Account", "Cash")
    val destinationOptions = listOf("Restaurant", "Grocery Store", "Online Shopping")
    val amountOptions = listOf("10.00 €", "20.00 €", "50.00 €")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                },
                actions = {
                    Button(
                        onClick = { /* Logic to create the transaction */ },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                        modifier = Modifier
                            .padding(top = 6.dp, bottom = 6.dp, end = 12.dp)
                    ) {
                        Text(text = "Create", style = MaterialTheme.typography.labelLarge)
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
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
                    .offset(y = (-8).dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_schedule),
                    contentDescription = "Date & Time"
                )
                Spacer(modifier = Modifier.width(16.dp))
                DatePicker(selectedDate = selectedDate)
                Spacer(modifier = Modifier.weight(1f))
                TimePicker(selectedTime = selectedTime)
//                TextButton(onClick = {
//
//                }) {
//                    Text(selectedDate.value, color = MaterialTheme.colorScheme.onBackground)
//                }
            }
            //Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(y = (-8).dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_public),
                    contentDescription = "Time Zone"
                )
                Spacer(modifier = Modifier.width(16.dp))
                TimeZonePicker(selectedTimeZone = selectedTimeZone)
            }
//            TextButton(onClick = { /* Open TimeZone Picker Logic */ }) {
//                Row(verticalAlignment = Alignment.CenterVertically) {
//
//                    Spacer(modifier = Modifier.width(8.dp))
//                    Text(selectedTimeZone.value, color = MaterialTheme.colorScheme.onBackground)
//                }
//            }

            // Source Field with DropdownMenu
            ExposedDropdownMenuBox(
                expanded = sourceExpanded.value,
                onExpandedChange = { sourceExpanded.value = !sourceExpanded.value }
            ) {
                OutlinedTextField(
                    value = source.value,
                    onValueChange = {},
                    label = { Text("Source") },
                    leadingIcon = {
                        Icon(
                            Icons.Default.AccountBalance,
                            contentDescription = "Source"
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = sourceExpanded.value) }
                )
                ExposedDropdownMenu(
                    expanded = sourceExpanded.value,
                    onDismissRequest = { sourceExpanded.value = false }
                ) {
                    sourceOptions.forEach { option ->
                        DropdownMenuItem(
                            onClick = {
                                source.value = option
                                sourceExpanded.value = false
                            },
                            text = { Text(option) }
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))

            // Destination Field with DropdownMenu
            ExposedDropdownMenuBox(
                expanded = destinationExpanded.value,
                onExpandedChange = { destinationExpanded.value = !destinationExpanded.value }
            ) {
                OutlinedTextField(
                    value = destination.value,
                    onValueChange = {},
                    label = { Text("Destination") },
                    leadingIcon = {
                        Icon(
                            Icons.Default.Restaurant,
                            contentDescription = "Destination"
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = destinationExpanded.value) }
                )
                ExposedDropdownMenu(
                    expanded = destinationExpanded.value,
                    onDismissRequest = { destinationExpanded.value = false }
                ) {
                    destinationOptions.forEach { option ->
                        DropdownMenuItem(
                            onClick = {
                                destination.value = option
                                destinationExpanded.value = false
                            },
                            text = { Text(option) }
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))

            // Amount Field with DropdownMenu
            ExposedDropdownMenuBox(
                expanded = amountExpanded.value,
                onExpandedChange = { amountExpanded.value = !amountExpanded.value }
            ) {
                OutlinedTextField(
                    value = amount.value,
                    onValueChange = {},
                    label = { Text("Amount") },
                    leadingIcon = {
                        Icon(
                            Icons.Default.Payments,
                            contentDescription = "Amount"
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = amountExpanded.value) }
                )
                ExposedDropdownMenu(
                    expanded = amountExpanded.value,
                    onDismissRequest = { amountExpanded.value = false }
                ) {
                    amountOptions.forEach { option ->
                        DropdownMenuItem(
                            onClick = {
                                amount.value = option
                                amountExpanded.value = false
                            },
                            text = { Text(option) }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Comment Field
            OutlinedTextField(
                value = "Dinner in a nice location",
                onValueChange = { /* Handle Comment */ },
                label = { Text("Comment") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Location Field with Placeholder Map
            OutlinedTextField(
                value = location.value,
                onValueChange = { /* Handle Location Change */ },
                label = { Text("Location") },
                leadingIcon = { Icon(Icons.Default.Place, contentDescription = "Location") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            MapScreen(44.2625, 12.3487)

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .padding(top = 16.dp)
            ) {
            }
        }
    }
}