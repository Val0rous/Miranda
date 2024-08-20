package com.cashflowtracker.miranda.ui.screens

import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.cashflowtracker.miranda.Routes
import com.cashflowtracker.miranda.data.database.User
import com.cashflowtracker.miranda.ui.composables.PasswordTextField
import com.cashflowtracker.miranda.ui.theme.MirandaTheme
import com.cashflowtracker.miranda.ui.viewmodels.UsersViewModel
import com.cashflowtracker.miranda.utils.generateSalt
import com.cashflowtracker.miranda.utils.hashPassword
import com.cashflowtracker.miranda.utils.validateEmail
import com.cashflowtracker.miranda.utils.validatePassword
import org.koin.androidx.compose.koinViewModel

class Signup : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        this.actionBar?.hide()
        setContent {
            val navController = rememberNavController()
//            NavHost(
//                navController = navController,
//                startDestination = Routes.Signup.route,
//                modifier = Modifier
//            ) {
//                composable("signup") { Signup(navController) }
//                composable("login") { Login(navController) }
//            }
            val name = remember { mutableStateOf("") }
            val email = remember { mutableStateOf("") }
            val password = remember { mutableStateOf("") }
            val isFormValid by remember {
                derivedStateOf {
                    name.value.isNotEmpty()
                            && email.value.isNotEmpty()
                            && validateEmail(email.value)
                            && password.value.isNotEmpty()
                            && validatePassword(password.value)
                }
            }

            val vm = koinViewModel<UsersViewModel>()
            val state by vm.state.collectAsStateWithLifecycle()
            val actions = vm.actions

            val customPadding = PaddingValues(
                start = 16.dp,
                top = 16.dp,
                end = 16.dp,
                bottom = 16.dp
            )

            MirandaTheme() {
                Scaffold(
                    modifier = Modifier.padding(customPadding),
                    topBar = {
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 128.dp)
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
                            )
                        }
                    },
                    bottomBar = {
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 16.dp)
                        ) {
                            Text("Already have an account? ")
                            TextButton(
                                onClick = {
                                    startActivity(Intent(this@Signup, Login::class.java))
//                                    navController.navigate(Routes.Login.route)
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surface),
                            ) {
                                Text(
                                    text = "Log in",
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
                                .fillMaxHeight()
                                .offset(y = 112.dp)
                        ) {
                            OutlinedTextField(
                                value = name.value,
                                onValueChange = { text -> name.value = text },
                                label = { Text("Name") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 16.dp)
                            )
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
                            Button(
                                onClick = {
                                    val salt = generateSalt()
                                    actions.addUser(
                                        User(
                                            name = name.value,
                                            email = email.value,
                                            password = hashPassword(password.value, salt),
                                            salt = salt,
                                            encryptionKey = null,
                                            currency = null,
                                            country = null
                                        )
                                    )

                                    startActivity(Intent(this@Signup, Login::class.java))
//                                    navController.navigate(Routes.Login.route)
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
        }
    }
}


//@Composable
//fun Signup(
//    navController: NavHostController,
//    state: UsersState,
//    actions: UsersActions,
//    isDarkTheme: Boolean,
//) {
//
//}