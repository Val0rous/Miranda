package com.cashflowtracker.miranda.ui.composables

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign

@Composable
fun BalanceText(
    text: String,
    isVisible: Boolean,
    style: TextStyle = MaterialTheme.typography.titleMedium,
    textAlign: TextAlign
) {
    if (isVisible) {
        Text(text = text, style = style, textAlign = textAlign)
    } else {
        Text(text = "• • • €", style = style, textAlign = textAlign)
    }
}