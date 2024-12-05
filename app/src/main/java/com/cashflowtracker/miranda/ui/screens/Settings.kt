package com.cashflowtracker.miranda.ui.screens

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.cashflowtracker.miranda.R
import com.cashflowtracker.miranda.data.repositories.LoginRepository.clearLoggedUserEmail
import com.cashflowtracker.miranda.data.repositories.ThemeRepository.getSystemDefaultTheme
import com.cashflowtracker.miranda.data.repositories.ThemeRepository.getSystemPreference
import com.cashflowtracker.miranda.data.repositories.ThemeRepository.getThemePreference
import com.cashflowtracker.miranda.ui.composables.SegmentedButtonTheme
import com.cashflowtracker.miranda.ui.theme.MirandaTheme
import kotlinx.coroutines.launch
import androidx.compose.material3.Scaffold
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cashflowtracker.miranda.data.repositories.LoginRepository.clearLoggedUserId
import com.cashflowtracker.miranda.data.repositories.ThemeRepository.saveSystemPreference
import com.cashflowtracker.miranda.data.repositories.ThemeRepository.saveThemePreference
import com.cashflowtracker.miranda.ui.viewmodels.ThemeViewModel
import org.koin.androidx.compose.koinViewModel


class Settings : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val context = LocalContext.current
            val coroutineScope = rememberCoroutineScope()
            val themeViewModel = koinViewModel<ThemeViewModel>()
            val isDarkTheme by themeViewModel.isDarkTheme.collectAsState()
            val followSystem by themeViewModel.followSystem.collectAsState()

            val customPadding = PaddingValues(
                start = 16.dp,
                top = 16.dp,
                end = 16.dp,
                bottom = 16.dp
            )

            MirandaTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        TopAppBar(
                            title = { Text(stringResource(R.string.settings)) },
                            navigationIcon = {
                                IconButton(onClick = { finish() }) {
                                    Icon(
                                        imageVector = ImageVector.vectorResource(R.drawable.ic_arrow_back),
                                        contentDescription = stringResource(R.string.back)
                                    )
                                }
                            }
                        )
                    }
                ) { paddingValues ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues)
                            .padding(customPadding), // It works just right with two paddings
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        // Theme selection
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                        ) {
                            Text(
                                text = stringResource(R.string.theme),
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                            SegmentedButtonTheme(
                                modifier = Modifier.fillMaxWidth(),
                                isDarkTheme = isDarkTheme,
                                followSystem = followSystem,
                                onThemeChange = { isDark: Boolean ->
                                    themeViewModel.setThemePreference(isDark, context)
                                },
                                onSystemChange = { follow: Boolean ->
                                    themeViewModel.setSystemPreference(follow, context)
                                },
                                context = context,
                            )
                        }

                        // Logout and Delete Account buttons
                        Column(modifier = Modifier.fillMaxWidth()) {
                            Button(
                                onClick = {
                                    coroutineScope.launch {
                                        context.clearLoggedUserEmail()
                                        context.clearLoggedUserId()
//                                    navController.navigate("login")
                                        startActivity(Intent(this@Settings, Login::class.java))
                                        finish()
                                    }
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(50.dp)
                            ) {
                                Text(stringResource(R.string.logout))
                            }
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(
                                onClick = { /* Manage account deletion */ },
                                colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.error),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(50.dp)
                            ) {
                                Text(stringResource(R.string.delete_account))
                            }
                        }
                    }
                }
            }
        }
    }
}