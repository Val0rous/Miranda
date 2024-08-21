package com.cashflowtracker.miranda.ui.screens

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.navigation.compose.rememberNavController
import com.cashflowtracker.miranda.R
import com.cashflowtracker.miranda.Routes
import com.cashflowtracker.miranda.ui.theme.MirandaTheme

class Transactions : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MirandaTheme {
                val navController = rememberNavController()
                Box(modifier = Modifier.fillMaxSize()) {
                    Text(
                        text = "Transactions Screen",
                        modifier = Modifier.align(Alignment.Center)
                    )

                    ExtendedFloatingActionButton(
                        onClick = { navController.navigate(Routes.AddTransaction.route) },
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
        }
    }
}
