package com.cashflowtracker.miranda

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@Composable
fun SettingsScreen(navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Sezione per la selezione del tema
        Column {
            Text("Theme", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                horizontalArrangement = Arrangement.SpaceAround,
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedButton(
                    onClick = { /* Gestisci il tema Light */ },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Light")
                }
                Spacer(modifier = Modifier.width(8.dp))
                OutlinedButton(
                    onClick = { /* Gestisci il tema Dark */ },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Dark")
                }
            }
        }

        // Pulsanti "Logout" e "Delete account"
        Column(modifier = Modifier.fillMaxWidth()) {
            Button(
                onClick = { /* Gestisci il Logout */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp) // Imposta un'altezza maggiore
            ) {
                Text("Logout")
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { /* Gestisci l'eliminazione dell'account */ },
                colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.error),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp) // Imposta un'altezza maggiore
            ) {
                Text("Delete account")
            }
        }
    }
}
