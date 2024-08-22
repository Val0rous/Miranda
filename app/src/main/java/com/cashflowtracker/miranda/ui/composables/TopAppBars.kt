package com.cashflowtracker.miranda.ui.composables

import android.content.Intent
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import com.cashflowtracker.miranda.R
import com.cashflowtracker.miranda.data.repositories.LoginRepository.getLoggedUserEmail
import com.cashflowtracker.miranda.ui.screens.Profile
import com.cashflowtracker.miranda.ui.screens.Settings

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeStatsTopAppBar() {
    val context = LocalContext.current
    val userEmail = context.getLoggedUserEmail() // Recupera l'email dell'utente loggato

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
                    val intent = Intent(context, Profile::class.java)
                    intent.putExtra("email", userEmail) // Passa l'email all'activity Profile
                    context.startActivity(intent)
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
    val userEmail = context.getLoggedUserEmail() // Recupera l'email dell'utente loggato

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
            IconButton(onClick = { /* TODO: Map View */ }) {
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
                    val intent = Intent(context, Profile::class.java)
                    intent.putExtra("email", userEmail) // Passa l'email all'activity Profile
                    context.startActivity(intent)
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
    val userEmail = context.getLoggedUserEmail() // Recupera l'email dell'utente loggato

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
                    val intent = Intent(context, Profile::class.java)
                    intent.putExtra("email", userEmail) // Passa l'email all'activity Profile
                    context.startActivity(intent)
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
