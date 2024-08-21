package com.cashflowtracker.miranda

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.cashflowtracker.miranda.data.repositories.LoginRepository.getLoggedUserEmail
import com.cashflowtracker.miranda.data.repositories.ThemeRepository.getSystemDefaultTheme
import com.cashflowtracker.miranda.data.repositories.ThemeRepository.getSystemPreference
import com.cashflowtracker.miranda.data.repositories.ThemeRepository.getThemePreference
import com.cashflowtracker.miranda.ui.screens.AppLayout
import com.cashflowtracker.miranda.ui.screens.Login
import com.cashflowtracker.miranda.ui.viewmodels.UsersViewModel
import com.cashflowtracker.miranda.utils.Routes
import kotlinx.coroutines.flow.firstOrNull
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val context = LocalContext.current
            val coroutineScope = rememberCoroutineScope()
            val navController = rememberNavController()
//            var isDarkTheme by remember { mutableStateOf(context.getThemePreference()) }
//            val followSystem by remember { mutableStateOf(context.getSystemPreference()) }
            var userEmail by remember { mutableStateOf<String?>(null) }

//            LaunchedEffect(Unit) {
//                context.getLoggedUserEmail().collect { email ->
//                    userEmail = email
//                }
//            }


//            LaunchedEffect(Unit) {
//                context.getSystemPreference().collect { isSystem ->
//                    followSystem = isSystem
//                }
//            }
//
//            LaunchedEffect(Unit) {
//                context.getThemePreference().collect { isDark ->
//                    isDarkTheme = isDark
//                }
//            }

//            val effectiveIsDarkTheme = if (followSystem) {
//                context.getSystemDefaultTheme()
//            } else {
//                isDarkTheme
//            }

            val vm = koinViewModel<UsersViewModel>()
            val state by vm.state.collectAsStateWithLifecycle()
            LaunchedEffect(Unit) {
                userEmail = context.getLoggedUserEmail().firstOrNull()
                if (!userEmail.isNullOrEmpty()) {
                    val intent = Intent(
                        this@MainActivity,
                        AppLayout::class.java
                    )
                    intent.putExtra("startDestination", Routes.Home.route)
                    startActivity(intent)
                } else {
                    startActivity(Intent(this@MainActivity, Login::class.java))
                }
            }
        }
    }
}
