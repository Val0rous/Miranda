package com.cashflowtracker.miranda.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun QuarterlyReportPlaceholderForGraph() {
    Box(
        modifier = Modifier
            .fillMaxHeight(0.85f) // Increase height for graph area to occupy more space
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
fun QuarterlySelector() {
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
                contentDescription = "Previous Quarter",
                tint = MaterialTheme.colorScheme.primary
            )
        }

        Text(
            text = "Q1 2024",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        IconButton(onClick = { /* Handle right arrow click */ }) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Next Quarter",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.rotate(180f)
            )
        }
    }
}
