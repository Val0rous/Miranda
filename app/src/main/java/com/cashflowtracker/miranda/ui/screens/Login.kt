package com.cashflowtracker.miranda.ui.screens

import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.cashflowtracker.miranda.data.database.User
import com.cashflowtracker.miranda.ui.viewmodels.UsersActions
import com.cashflowtracker.miranda.ui.viewmodels.UsersState
import com.cashflowtracker.miranda.utils.generateSalt
import com.cashflowtracker.miranda.utils.hashPassword
import com.cashflowtracker.miranda.utils.validateEmail
import com.cashflowtracker.miranda.utils.validatePassword

@Composable
fun Login(
    /* navController: NavHostController */
    state: UsersState,
    actions: UsersActions,
    isDarkTheme: Boolean,
    onThemeChange: (Boolean) -> Unit
) {
    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val isFormValid by remember {
        derivedStateOf {
            email.value.isNotEmpty()
                    && validateEmail(email.value)
                    && password.value.isNotEmpty()
                    && validatePassword(password.value)
        }
    }

    val customPadding = PaddingValues(
        start = 16.dp,
        top = 16.dp,
        end = 16.dp,
        bottom = 16.dp
    )

    Scaffold(
        modifier = Modifier.padding(customPadding),
        topBar = {
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Miranda",
                    style = TextStyle(
                        fontSize = 64.sp,
                        fontFamily = MaterialTheme.typography.displayLarge.fontFamily,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.SemiBold,
                        shadow = Shadow(
                            color = MaterialTheme.colorScheme.onSurface,
                            offset = Offset(0f, 8f),
                            blurRadius = 4f
                        )
                    ),
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(top = 128.dp, bottom = 0.dp),
                )
            }
        },
        bottomBar = {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Don't have an account? ")
                TextButton(
                    onClick = { },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surface),
                ) {
                    Text(
                        text = "Sign up",
                        style = TextStyle(
                            fontFamily = MaterialTheme.typography.labelLarge.fontFamily,
                            fontWeight = FontWeight.SemiBold
                        ),
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        },
        content = { paddingValues ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxHeight()
                    .offset(y = (-32).dp)
            ) {
                OutlinedTextField(
                    value = email.value,
                    onValueChange = { text -> email.value = text },
                    label = { Text("Email") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                )
                OutlinedTextField(
                    value = password.value,
                    onValueChange = { text -> password.value = text },
                    label = { Text("Password") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                )
                Button(
                    onClick = {
                        if (actions.login(email.value, password.value)) {
                            //Intent(this, AppLayout::class.java)
                            // TODO
                        } else {
                            return@Button
                        }
                    },
                    enabled = isFormValid,
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp)
                ) {
                    Text(text = "Sign up", style = MaterialTheme.typography.labelLarge)
                }
            }
        }
    )
}