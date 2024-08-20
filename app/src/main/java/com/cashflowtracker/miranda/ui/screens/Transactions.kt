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
import com.cashflowtracker.miranda.NavigationRoute
import com.cashflowtracker.miranda.R

@Composable
fun Transactions(navController: NavHostController) {
    Box(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "Transactions Screen",
            modifier = Modifier.align(Alignment.Center)
        )

        ExtendedFloatingActionButton(
            onClick = { navController.navigate(NavigationRoute.AddTransaction.route) },
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