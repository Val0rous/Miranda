package com.cashflowtracker.miranda

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.rounded.Download
import androidx.compose.material.icons.rounded.Upload
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.cashflowtracker.miranda.ui.composables.SegmentedButtonPreview
import com.cashflowtracker.miranda.ui.composables.SegmentedButtonTheme

@Composable
fun SettingsScreen(
    navController: NavHostController,
    isDarkTheme: Boolean,  // Receives current theme status
    onThemeChange: (Boolean) -> Unit
) {
    var selectedTheme by remember { mutableStateOf(isDarkTheme) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Theme selection
        Column {
            Text(
                text = "Theme",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                horizontalArrangement = Arrangement.SpaceAround,
                modifier = Modifier.fillMaxWidth()
            ) {
                ThemeToggleButton(
                    isSelected = !selectedTheme,
                    text = "Light",
                    icon = Icons.Filled.LightMode,
                    onClick = {
                        selectedTheme = false
                        onThemeChange(false) // Update theme in AppLayout
                    }
                )
                Spacer(modifier = Modifier.width(8.dp))
                ThemeToggleButton(
                    isSelected = selectedTheme,
                    text = "Dark",
                    icon = Icons.Filled.DarkMode,
                    onClick = {
                        selectedTheme = true
                        onThemeChange(true) // Update theme in AppLayout
                    }
                )
            }
        }

        SegmentedButtonTheme(modifier = Modifier.fillMaxWidth())

        // Logout and Delete Account buttons
        Column(modifier = Modifier.fillMaxWidth()) {
            Button(
                onClick = { /* Manage logout */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text("Logout")
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { /* Manage account deletion */ },
                colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.error),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text("Delete account")
            }
        }
    }
}


@Composable
fun ThemeToggleButton(
    isSelected: Boolean,
    text: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    OutlinedButton(
        onClick = onClick,
        modifier = Modifier.height(56.dp),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.1f) else MaterialTheme.colorScheme.surface
        )
    ) {
        Icon(icon, contentDescription = text)
        Spacer(modifier = Modifier.width(8.dp))
        Text(text)
        if (isSelected) {
            Spacer(modifier = Modifier.width(8.dp))
            Icon(Icons.Filled.Check, contentDescription = "Selected")
        }
    }
}
