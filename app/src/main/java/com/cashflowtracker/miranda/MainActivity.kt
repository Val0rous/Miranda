package com.cashflowtracker.miranda

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cashflowtracker.miranda.ui.screens.Login
import com.cashflowtracker.miranda.ui.screens.Signup
import com.cashflowtracker.miranda.ui.theme.MirandaTheme
import com.cashflowtracker.miranda.ui.viewmodels.UsersViewModel
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var isDarkTheme by remember { mutableStateOf(false) }

            MirandaTheme(darkTheme = isDarkTheme) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    val vm = koinViewModel<UsersViewModel>()
                    val state by vm.state.collectAsStateWithLifecycle()
//                    AppLayout(
//                        isDarkTheme = isDarkTheme,
//                        onThemeChange = { isDarkTheme = it }
//                    )
                    Login(
                        state = state,
                        actions = vm.actions,
                        isDarkTheme = isDarkTheme,
                        onThemeChange = { isDarkTheme = it })
                }
            }
        }
    }
}
