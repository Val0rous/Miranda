package com.cashflowtracker.miranda.ui.screens

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import com.cashflowtracker.miranda.data.repositories.LoginRepository
import com.cashflowtracker.miranda.data.repositories.LoginRepository.getCurrentUserEmail
import com.cashflowtracker.miranda.data.repositories.LoginRepository.getCurrentUserId
import com.cashflowtracker.miranda.ui.viewmodels.UsersViewModel
import org.koin.androidx.compose.koinViewModel

class EditProfile : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val coroutineScope = rememberCoroutineScope()
            val context = LocalContext.current
            val userId = context.getCurrentUserId()
            val userName = remember { mutableStateOf("") }
            val email = remember { mutableStateOf(context.getCurrentUserEmail()) }
            val newPassword = remember { mutableStateOf("") }
            val isError = remember { mutableStateOf(false) }
            val vm = koinViewModel<UsersViewModel>()

        }
    }
}