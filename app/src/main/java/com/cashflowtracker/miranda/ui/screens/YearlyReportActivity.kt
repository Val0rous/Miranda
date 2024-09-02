package com.cashflowtracker.miranda.ui.screens

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cashflowtracker.miranda.ui.theme.MirandaTheme

class YearlyReportActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MirandaTheme {
                YearlyReportScreen(onBack = { onBackPressed() })
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun YearlyReportScreen(onBack: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Cashflow Chart", color = MaterialTheme.colorScheme.onSurface)
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(it)
            ) {
                YearlyReportTabs() // Custom function to set default tab
                YearlyReportPlaceholderForGraph()
                Spacer(modifier = Modifier.height(16.dp)) // Small spacer for padding
                YearSelector()
            }
        }
    )
}

@Composable
fun YearlyReportTabs() {
    var selectedTabIndex by remember { mutableStateOf(1) } // Default to "Yearly" tab
    val tabTitles = listOf("Overall", "Yearly", "Quarterly", "Monthly")

    TabRow(
        selectedTabIndex = selectedTabIndex,
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface
    ) {
        tabTitles.forEachIndexed { index, title ->
            Tab(
                selected = selectedTabIndex == index,
                onClick = { selectedTabIndex = index },
                text = {
                    Text(
                        text = title,
                        color = if (selectedTabIndex == index) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                        fontSize = 14.sp
                    )
                }
            )
        }
    }
}

@Composable
fun YearlyReportPlaceholderForGraph() {
    Box(
        modifier = Modifier
            .fillMaxHeight(0.85f) // Adjusted height for the graph area to occupy more space
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Graph Placeholder",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontSize = 18.sp
        )
    }
}

@Composable
fun YearSelector() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = { /* Handle left arrow click */ }) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Previous Year",
                tint = MaterialTheme.colorScheme.primary
            )
        }

        Text(
            text = "2024",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        IconButton(onClick = { /* Handle right arrow click */ }) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Next Year",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.rotate(180f)
            )
        }
    }
}
