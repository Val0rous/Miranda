package com.cashflowtracker.miranda.ui.composables

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.cashflowtracker.miranda.R

@Composable
fun PasswordTextField(
    password: MutableState<String>,
    modifier: Modifier
) {
    var passwordVisible by remember { mutableStateOf(false) }
    val visibleIcon = R.drawable.ic_visibility_filled

    OutlinedTextField(
        value = password.value,
        onValueChange = { text -> password.value = text },
        label = { Text("Password") },
        singleLine = true,
        visualTransformation = if (passwordVisible) {
            VisualTransformation.None
        } else {
            PasswordVisualTransformation()
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        trailingIcon = {
            IconButton(
                onClick = {
                    passwordVisible = !passwordVisible
                },
                modifier = Modifier.padding(end = 5.dp)
            ) {
                Icon(
                    imageVector = if (passwordVisible) {
                        ImageVector.vectorResource(R.drawable.ic_visibility_off)
                    } else {
                        ImageVector.vectorResource(R.drawable.ic_visibility_filled)
                    }, contentDescription = "Toggle Visibility"
                )
            }
        },
        modifier = modifier
    )
}