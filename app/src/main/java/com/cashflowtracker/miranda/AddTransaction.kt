package com.cashflowtracker.miranda

import android.app.DatePickerDialog
import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.rounded.Upload
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.cashflowtracker.miranda.ui.composables.SegmentedButtons
import com.cashflowtracker.miranda.ui.composables.SegmentedButtonType
import com.cashflowtracker.miranda.utils.MapScreen
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTransaction(navController: NavHostController) {
    val transactionType = remember { mutableStateOf("Output") }
    val context = LocalContext.current

    // Stato per gestire la selezione della data
    val selectedDate = remember { mutableStateOf("Select Date") }
    val selectedTimeZone = remember { mutableStateOf("Select Time Zone") }

    // Stato per gestire i selettori
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
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        Text("Create")
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
            SegmentedButtonType(modifier = Modifier.fillMaxWidth())
//            SegmentedButtonsBeta {
//                var selectedIndex = -1
//                SegmentedButtonItem(
//                    selected = selectedIndex == 0,
//                    onClick = { selectedIndex = 0 },
//                    label = { "Alpha" },
//                    icon = { R.drawable.ic_assignment_add }
//                )
//                SegmentedButtonItem(
//                    selected = selectedIndex == 1,
//                    onClick = { selectedIndex = 1 },
//                    label = { "Bravo" },
//                    icon = { Icons.Rounded.Upload }
//                )
//                SegmentedButtonItem(
//                    selected = selectedIndex == 2,
//                    onClick = { selectedIndex = 2 },
//                    label = { "Charlie" },
//                    icon = { R.drawable.ic_assignment_add }
//                )
//            }

            // Buttons: Output, Input, Transfer
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                OutlinedButton(
                    onClick = { transactionType.value = "Output" },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.Upload, contentDescription = "Output")
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Output", maxLines = 1)
                }
                OutlinedButton(
                    onClick = { transactionType.value = "Input" },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.Download, contentDescription = "Input")
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Input", maxLines = 1)
                }
                OutlinedButton(
                    onClick = { transactionType.value = "Transfer" },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.SwapVert, contentDescription = "Transfer")
                    Spacer(modifier = Modifier.width(5.dp))
                    Text("Transfer", maxLines = 1)
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            // Date and TimeZone with icons and click actions
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TextButton(onClick = { showDatePicker(context, selectedDate) }) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Schedule, contentDescription = "Date")
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(selectedDate.value, color = MaterialTheme.colorScheme.onBackground)
                    }
                }
                TextButton(onClick = { /* Open TimeZone Picker Logic */ }) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Public, contentDescription = "Time Zone")
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(selectedTimeZone.value, color = MaterialTheme.colorScheme.onBackground)
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))

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

private fun showDatePicker(context: Context, selectedDate: MutableState<String>) {
    val calendar = Calendar.getInstance()
    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            selectedDate.value = "${month + 1}/$dayOfMonth/$year"
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )
    datePickerDialog.show()
}

