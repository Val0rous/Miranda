package com.cashflowtracker.miranda

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.outlined.Assignment
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@Composable
fun Home(navController: NavHostController) {
    var isMenuExpanded by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        Text(text = "Home Screen", modifier = Modifier.align(Alignment.Center))

        // FAB principale e FAB per Transactions e Recurrents
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            if (isMenuExpanded) {
                // Mostra le due opzioni quando il menu è espanso
                Column(
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    FloatingActionButton(
                        onClick = {
                            navController.navigate(NavigationRoute.AddTransaction.route) {
                                popUpTo(navController.graph.startDestinationId) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                            isMenuExpanded = false
                        },
                    ) {
                        Icon(Icons.Outlined.Assignment, contentDescription = "Transactions")
                    }
                    FloatingActionButton(
                        onClick = {
                            navController.navigate(NavigationRoute.AddRecurrence.route) {
                                popUpTo(navController.graph.startDestinationId) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                            isMenuExpanded = false
                        },
                    ) {
                        Icon(Icons.Outlined.Schedule, contentDescription = "Recurrents")
                    }
                }
            } else {
                // Mostra il FAB principale quando il menu non è espanso
                FloatingActionButton(
                    onClick = { isMenuExpanded = true },
                ) {
                    Icon(Icons.Filled.Add, contentDescription = "Add")
                }
            }
        }
    }
}

@Composable
fun Transactions(navController: NavHostController) {
    Box(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "Transactions Screen",
            modifier = Modifier.align(Alignment.Center)
        )

        ExtendedFloatingActionButton(
            onClick = {
                navController.navigate(NavigationRoute.AddTransaction.route)  // Modifica qui per navigare a TransactionAdd
            },
            icon = {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_assignment),
                    contentDescription = "Add Transaction"
                )
            },
            text = { Text("Add Transaction") },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        )
    }
}


@Composable
fun Recurrents(navController: NavHostController) {
    Box(modifier = Modifier.fillMaxSize()) {
        // Contenuto della schermata delle transazioni
        Text(
            text = "Recurrents Screen",
            modifier = Modifier.align(Alignment.Center)
        )

        // FloatingActionButton in basso a destra con l'etichetta "Add Reccurence"
        ExtendedFloatingActionButton(
            onClick = { /* Azione da eseguire quando il FAB viene premuto */ },
            icon = {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_schedule),
                    contentDescription = "Add"
                )
            },
            text = { Text("Add Recurrence") },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        )
    }
}

@Composable
fun Stats(navController: NavHostController) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = "Stats Screen")
    }
}
