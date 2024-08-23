package com.cashflowtracker.miranda.ui.screens

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cashflowtracker.miranda.MainActivity
import com.cashflowtracker.miranda.utils.Routes
import com.cashflowtracker.miranda.data.repositories.LoginRepository.getLoggedUserEmail
import com.cashflowtracker.miranda.data.repositories.LoginRepository.saveLoggedUserEmail
import com.cashflowtracker.miranda.data.repositories.LoginRepository.saveLoggedUserId
import com.cashflowtracker.miranda.ui.composables.PasswordTextField
import com.cashflowtracker.miranda.ui.theme.MirandaTheme
import com.cashflowtracker.miranda.ui.viewmodels.UsersViewModel
import com.cashflowtracker.miranda.utils.validateEmail
import com.cashflowtracker.miranda.utils.validatePassword
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.androidx.compose.koinViewModel

class Login : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val coroutineScope = rememberCoroutineScope()
            val context = LocalContext.current

            var errorMessage by remember { mutableStateOf("") }

//            if (!context.getLoggedUserEmail().isNullOrEmpty()) {
//                val intent = Intent(
//                    this@Login,
//                    MainActivity::class.java
//                )
//                intent.putExtra("startDestination", Routes.Home.route)
//                startActivity(intent)
//            }

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

            val vm = koinViewModel<UsersViewModel>()
            val state by vm.state.collectAsStateWithLifecycle()
            val actions = vm.actions

            MirandaTheme {
                Scaffold(
                    topBar = {
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 176.dp)
                        ) {
                            Text(
                                text = "Miranda",
                                style = TextStyle(
                                    fontSize = 64.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    fontFamily = MaterialTheme.typography.displayLarge.fontFamily,
//                                    shadow = Shadow(
//                                        color = MaterialTheme.colorScheme.onSurface,
//                                        offset = Offset(0f, 8f),
//                                        blurRadius = 4f
//                                    )
                                ),
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colorScheme.primary,
                            )
                        }
                    },
                    bottomBar = {
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 32.dp)
                        ) {
                            Text("Don't have an account? ")
                            TextButton(
                                onClick = {
                                    startActivity(Intent(this@Login, Signup::class.java))
                                },
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
                            verticalArrangement = Arrangement.Top,
                            modifier = Modifier
                                .padding(paddingValues)
                                .padding(horizontal = 16.dp)
                                .fillMaxHeight()
                                .offset(y = 112.dp)
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
                            PasswordTextField(
                                password = password,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 16.dp)
                            )
                            if (errorMessage.isNotEmpty()) {
                                Text(
                                    text = errorMessage,
                                    color = MaterialTheme.colorScheme.error,
                                    modifier = Modifier.padding(bottom = 8.dp)
                                )
                            }
                            Button(
                                onClick = {
                                    coroutineScope.launch {
                                        if (actions.login(email.value, password.value)) {
                                            context.saveLoggedUserEmail(email.value)
                                            withContext(Dispatchers.IO) {
                                                val userId = actions.getUserIdByEmail(email.value)
                                                context.saveLoggedUserId(userId)
                                            }
                                            val intent = Intent(
                                                this@Login,
                                                MainActivity::class.java
                                            )
                                            intent.putExtra("startDestination", Routes.Home.route)
                                            startActivity(intent)
                                            finish()
                                        } else {
                                            errorMessage = "Invalid email or password"
//                                            return@launch
                                        }
                                    }
                                },
                                enabled = isFormValid,
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 8.dp)
                                    .height(40.dp)
                            ) {
                                Text(text = "Log in", style = MaterialTheme.typography.labelLarge)
                            }
                        }
                    }
                )
            }
        }
    }
}
