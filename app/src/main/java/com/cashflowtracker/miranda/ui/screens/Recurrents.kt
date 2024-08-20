package com.cashflowtracker.miranda.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.cashflowtracker.miranda.Routes
import com.cashflowtracker.miranda.R

@Composable
fun Recurrents(navController: NavHostController) {
    Box(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "Recurrents Screen",
            modifier = Modifier.align(Alignment.Center)
        )

        // FloatingActionButton in basso a destra con l'etichetta "Add Recurrence"
        ExtendedFloatingActionButton(
            onClick = { navController.navigate(Routes.AddRecurrence.route) },
            icon = {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_schedule),
                    contentDescription = "Add Recurrence"
                )
            },
            text = { Text("Add Recurrence") },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        )
    }
}