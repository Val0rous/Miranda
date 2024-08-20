package com.cashflowtracker.miranda

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.cashflowtracker.miranda.data.repositories.ThemeRepository.getSystemDefaultTheme
import com.cashflowtracker.miranda.data.repositories.ThemeRepository.getSystemPreference
import com.cashflowtracker.miranda.data.repositories.ThemeRepository.getThemePreference
import com.cashflowtracker.miranda.ui.screens.Login
import com.cashflowtracker.miranda.ui.screens.Signup
import com.cashflowtracker.miranda.ui.theme.MirandaTheme
import com.cashflowtracker.miranda.ui.viewmodels.UsersViewModel
import kotlinx.coroutines.flow.first
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val context = LocalContext.current
            val coroutineScope = rememberCoroutineScope()
            val navController = rememberNavController()
            var isDarkTheme by remember { mutableStateOf(false) }
            var followSystem by remember { mutableStateOf(true) }

            LaunchedEffect(Unit) {
                context.getSystemPreference().collect { isSystem ->
                    followSystem = isSystem
                    println("followSystem: $followSystem")
                }
            }

            LaunchedEffect(Unit) {
                context.getThemePreference().collect { isDark ->
                    isDarkTheme = isDark
                    println("isDarkTheme: $isDarkTheme")
                }
            }

            val effectiveIsDarkTheme = if (followSystem) {
                context.getSystemDefaultTheme()
            } else {
                isDarkTheme
            }

            MirandaTheme(darkTheme = effectiveIsDarkTheme) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    val vm = koinViewModel<UsersViewModel>()
                    val state by vm.state.collectAsStateWithLifecycle()

                    val email = ""  // TODO: get email from datastore

                    val startDestination = if (email.isNullOrEmpty()) {
                        Routes.Login.route
                    } else {
                        Routes.Home.route
                    }

                    AppLayout(
                        navController = navController,
                        startDestination = startDestination,
                        state = state,
                        actions = vm.actions,
                        isDarkTheme = effectiveIsDarkTheme,
                        followSystem = followSystem,
                        context = context,
                        coroutineScope = coroutineScope,
//                        onThemeChange = { isDarkTheme = it }
                    )
                }
            }
        }
    }
}
