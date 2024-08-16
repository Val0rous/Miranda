package com.cashflowtracker.miranda

import android.app.DatePickerDialog
import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionAdd(navController: NavHostController) {
    val transactionType = remember { mutableStateOf("Output") }
    val context = LocalContext.current

    // Stato per gestire la selezione della data
    val selectedDate = remember { mutableStateOf("Select Date") }
    val selectedTimeZone = remember { mutableStateOf("Select Time Zone") }

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
        Column(modifier = Modifier.padding(paddingValues).padding(16.dp)) {
            // Buttons: Output, Input, Transfer
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                OutlinedButton(
                    onClick = { transactionType.value = "Output" },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.ArrowOutward, contentDescription = "Output")
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Output", maxLines = 1)
                }
                OutlinedButton(
                    onClick = { transactionType.value = "Input" },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.ArrowDownward, contentDescription = "Input")
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Input", maxLines = 1)
                }
                OutlinedButton(
                    onClick = { transactionType.value = "Transfer" },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.SwapHoriz, contentDescription = "Transfer")
                    Spacer(modifier = Modifier.width(5.dp))
                    Text("Transfer", maxLines = 1)
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            // Date and TimeZone with icons and click actions
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                TextButton(onClick = { showDatePicker(context, selectedDate) }) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.CalendarToday, contentDescription = "Date")
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(selectedDate.value, color = MaterialTheme.colorScheme.onBackground)
                    }
                }
                TextButton(onClick = { /* Open TimeZone Picker Logic */ }) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Map, contentDescription = "Time Zone")
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(selectedTimeZone.value, color = MaterialTheme.colorScheme.onBackground)
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))

            // Source, Destination, and Amount Fields with Icons and Clickable Text
            listOf(
                Pair("Source", Icons.Default.AccountBalance),
                Pair("Destination", Icons.Default.Restaurant),
                Pair("Amount", Icons.Default.AttachMoney)
            ).forEach { (label, icon) ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { /* Open corresponding picker */ }
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(icon, contentDescription = label, tint = Color.Gray)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("", color = MaterialTheme.colorScheme.onBackground) // Empty as data is from DB
                }
                Divider()
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

            // Placeholder for Map
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .padding(top = 16.dp)
            ) {
                // Map placeholder implementation
            }
        }
    }
}

private fun showDatePicker(context: Context, selectedDate: MutableState<String>) {
    val calendar = Calendar.getInstance()
    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            selectedDate.value = "$dayOfMonth/${month + 1}/$year"
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )
    datePickerDialog.show()
}
