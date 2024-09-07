package com.cashflowtracker.miranda.ui.composables

import android.content.Intent
import android.icu.text.SimpleDateFormat
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
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
import com.cashflowtracker.miranda.ui.screens.MapView
import com.cashflowtracker.miranda.ui.screens.Profile
import com.cashflowtracker.miranda.ui.screens.Settings
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeStatsTopAppBar() {
    val context = LocalContext.current
//    val userEmail = context.getCurrentUserEmail() // Recupera l'email dell'utente loggato

    TopAppBar(
        title = { },
        actions = {
            IconButton(
                onClick = {
                    context.startActivity(Intent(context, Settings::class.java))
                }) {
                Icon(
                    ImageVector.vectorResource(R.drawable.ic_settings),
                    contentDescription = "Settings"
                )
            }
            IconButton(
                onClick = {
                    context.startActivity(Intent(context, Profile::class.java))
                },
                colors = IconButtonDefaults.iconButtonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Icon(
                    ImageVector.vectorResource(R.drawable.ic_account_circle_filled),
                    contentDescription = "Profile",
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionsTopAppBar() {
    val context = LocalContext.current

    TopAppBar(
        title = { },
        actions = {
            IconButton(onClick = { /* TODO: Sort */ }) {
                Icon(
                    ImageVector.vectorResource(R.drawable.ic_swap_vert),
                    contentDescription = "Sort"
                )
            }
            IconButton(onClick = { /* TODO: Filter */ }) {
                Icon(
                    ImageVector.vectorResource(R.drawable.ic_filter_list),
                    contentDescription = "Filter"
                )
            }
            IconButton(onClick = {
                context.startActivity(Intent(context, MapView::class.java))
            }) {
                Icon(
                    ImageVector.vectorResource(R.drawable.ic_map),
                    contentDescription = "Map View"
                )
            }
            IconButton(
                onClick = {
                    context.startActivity(Intent(context, Settings::class.java))
                }) {
                Icon(
                    ImageVector.vectorResource(R.drawable.ic_settings),
                    contentDescription = "Settings"
                )
            }
            IconButton(
                onClick = {
                    context.startActivity(Intent(context, Profile::class.java))
                },
                colors = IconButtonDefaults.iconButtonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Icon(
                    ImageVector.vectorResource(R.drawable.ic_account_circle_filled),
                    contentDescription = "Profile",
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecurrentsTopAppBar() {
    val context = LocalContext.current

    TopAppBar(
        title = { },
        actions = {
            IconButton(onClick = { /* TODO: Sort */ }) {
                Icon(
                    ImageVector.vectorResource(R.drawable.ic_swap_vert),
                    contentDescription = "Sort"
                )
            }
            IconButton(onClick = { /* TODO: Filter */ }) {
                Icon(
                    ImageVector.vectorResource(R.drawable.ic_filter_list),
                    contentDescription = "Filter"
                )
            }
            IconButton(
                onClick = {
                    context.startActivity(Intent(context, Settings::class.java))
                }) {
                Icon(
                    ImageVector.vectorResource(R.drawable.ic_settings),
                    contentDescription = "Settings"
                )
            }
            IconButton(
                onClick = {
                    context.startActivity(Intent(context, Profile::class.java))
                },
                colors = IconButtonDefaults.iconButtonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Icon(
                    ImageVector.vectorResource(R.drawable.ic_account_circle_filled),
                    contentDescription = "Profile",
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditTopAppBar(
    buttonText: String,
    isButtonEnabled: Boolean,
    onIconButtonClick: () -> Unit,
    onButtonClick: () -> Unit
) {
    TopAppBar(
        title = { },
        navigationIcon = {
            IconButton(
                onClick = { onIconButtonClick() },
                modifier = Modifier.padding(
                    start = 0.dp,
                    top = 16.dp,
                    bottom = 16.dp
                )
            ) {
                Icon(
                    ImageVector.vectorResource(R.drawable.ic_close),
                    contentDescription = "Close"
                )
            }
        },
        actions = {
            Button(
                onClick = { onButtonClick() },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                modifier = Modifier
                    .padding(top = 16.dp, bottom = 16.dp, end = 16.dp)
                    .height(32.dp),
                contentPadding = PaddingValues(
                    horizontal = 12.dp,
                    vertical = 5.dp
                ),
                enabled = isButtonEnabled
            ) {
                Text(
                    text = buttonText,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.labelLarge,
                    modifier = Modifier.padding(0.dp)
                )
            }
        }
    )
}