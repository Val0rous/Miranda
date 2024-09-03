package com.cashflowtracker.miranda.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.InputChip
import androidx.compose.material3.InputChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cashflowtracker.miranda.R

@Composable
fun QuarterlyChart() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
//            .background(MaterialTheme.colorScheme.surfaceVariant)
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Graph Placeholder",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 18.sp
            )
        }
        QuarterlySelector()
    }
}

@Composable
fun QuarterlySelector() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceContainerLow),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = { /* Handle left double arrow click */ },
            modifier = Modifier.padding(end = 16.dp)
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_keyboard_double_arrow_left),
                contentDescription = "Previous Year",
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        IconButton(
            onClick = { /* Handle left arrow click */ },
            modifier = Modifier.padding(end = 16.dp)
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_keyboard_arrow_left),
                contentDescription = "Previous Quarter",
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        InputChip(
            modifier = Modifier.padding(vertical = 4.dp),
            selected = false,
            onClick = { /*TODO*/ },
            label = {
                Text(
                    text = "Q1 2024",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
            },
            colors = InputChipDefaults.inputChipColors(containerColor = MaterialTheme.colorScheme.surface)
        )

        IconButton(
            onClick = { /* Handle right arrow click */ },
            modifier = Modifier.padding(start = 16.dp)
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_keyboard_arrow_right),
                contentDescription = "Next Quarter",
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }

        IconButton(
            onClick = { /* Handle right double arrow click */ },
            modifier = Modifier.padding(start = 16.dp)
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_keyboard_double_arrow_right),
                contentDescription = "Next Year",
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
