package com.cashflowtracker.miranda.ui.screens

import android.app.Activity
import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.cashflowtracker.miranda.R
import com.cashflowtracker.miranda.data.database.Account
import com.cashflowtracker.miranda.data.repositories.LoginRepository.getCurrentUserEmail
import com.cashflowtracker.miranda.data.repositories.LoginRepository.getCurrentUserId
import com.cashflowtracker.miranda.ui.composables.AccountTitleForm
import com.cashflowtracker.miranda.ui.composables.AccountTypeForm
import com.cashflowtracker.miranda.ui.composables.AddEditTopAppBar
import com.cashflowtracker.miranda.ui.theme.MirandaTheme
import com.cashflowtracker.miranda.ui.viewmodels.AccountsViewModel
import com.cashflowtracker.miranda.ui.viewmodels.UsersViewModel
import com.cashflowtracker.miranda.utils.AccountType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.androidx.compose.koinViewModel
import java.util.Date
import java.util.Locale
import java.util.UUID

class EditAccount : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val scrollState = rememberScrollState()
            val coroutineScope = rememberCoroutineScope()
            val context = LocalContext.current
            val accountId = remember {
                mutableStateOf(
                    UUID.fromString(intent.getStringExtra("accountId") ?: "")
                )
            }
            val accountTitle = remember { mutableStateOf("") }
            var accountType by remember { mutableStateOf("") }
            var accountIcon by remember { mutableStateOf<Int?>(null) }

            // Launcher to choose account type from intent
            val launcher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.StartActivityForResult()
            ) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    accountType = result.data?.getStringExtra("accountType") ?: ""
                    accountIcon = result.data?.getStringExtra("accountIcon")?.toInt()
                }
            }
            val isFormValid by remember {
                derivedStateOf {
                    accountType.isNotEmpty()
                            && accountTitle.value.isNotEmpty()
                }
            }
            val isError = remember { mutableStateOf(false) }
            val vm = koinViewModel<AccountsViewModel>()
//            val state by vm.state.collectAsStateWithLifecycle()
            val actions = vm.actions
            val usersVm = koinViewModel<UsersViewModel>()
            val email = context.getCurrentUserEmail()
            val userId = context.getCurrentUserId()
            var isLoaded by remember { mutableStateOf(false) }

            var account by remember { mutableStateOf<Account?>(null) }
            LaunchedEffect(Unit) {
                coroutineScope.launch(Dispatchers.IO) {
                    account = vm.actions.getByAccountId(accountId.value, userId).also {
                        accountTitle.value = it.title
                        accountType = it.type
                        accountIcon = AccountType.getIcon(it.type)
                        isLoaded = true
                    }
                }
            }

            MirandaTheme {
                if (isLoaded) {
                    Scaffold(
                        topBar = {
                            AddEditTopAppBar(
                                buttonText = "Save",
                                isButtonEnabled = isFormValid,
                                onIconButtonClick = { finish() },
                                onButtonClick = {
                                    coroutineScope.launch {
                                        val existingAccount = withContext(Dispatchers.IO) {
                                            vm.actions.getByTitleOrNull(
                                                accountTitle.value,
                                                userId
                                            )
                                        }
                                        if (existingAccount != null) {
                                            isError.value = true
                                            return@launch
                                        } else {
                                            actions.updateAccount(
                                                Account(
                                                    accountId = account!!.accountId,
                                                    title = accountTitle.value,
                                                    type = accountType,
                                                    balance = account!!.balance,
                                                    creationDate = account!!.creationDate,
                                                    userId = account!!.userId,
                                                    isFavorite = account!!.isFavorite,
                                                )
                                            )
                                            finish()
                                        }
                                    }
                                }
                            )
                        }
                    ) { paddingValues ->
                        Column(
                            modifier = Modifier
                                .padding(paddingValues)
                                .padding(16.dp)
                                .verticalScroll(scrollState)
                        ) {
                            AccountTitleForm(accountTitle, isError)
                            AccountTypeForm(accountType, accountIcon, launcher)
                        }
                    }
                }
            }
        }
    }
}