package com.cashflowtracker.miranda.ui.composables

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign

@Composable
fun BalanceText(
    balance: Double,
    isVisible: Boolean,
    style: TextStyle,
    color: Color,
    textAlign: TextAlign? = null
) {
    val effectiveColor = if (balance >= 0) {
        color
    } else {
        MaterialTheme.colorScheme.error
    }
    if (isVisible) {
        Text(
            text = String.format("%.2f €", balance),
            style = style,
            color = effectiveColor,
            textAlign = textAlign
        )
    } else {
        Text(text = "• • • €", style = style, color = color, textAlign = textAlign)
    }
}