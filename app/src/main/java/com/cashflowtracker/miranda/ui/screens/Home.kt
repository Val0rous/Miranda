package com.cashflowtracker.miranda.ui.screens

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.navigation.compose.rememberNavController
import com.cashflowtracker.miranda.Navbar
import com.cashflowtracker.miranda.R
import com.cashflowtracker.miranda.ui.composables.ExpandableFloatingActionButton
import com.cashflowtracker.miranda.ui.theme.MirandaTheme

class Home : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            val isFabExpanded = remember { mutableStateOf(false) }

            val modifier = if (isFabExpanded.value) {
                Modifier
                    .background(Color.Black.copy(alpha = 0.5f))
                    .fillMaxSize()
                    .clickable { isFabExpanded.value = false }
            } else {
                Modifier
                    .fillMaxSize()
            }

            MirandaTheme() {
                Scaffold(
                    modifier = modifier,
                    topBar = {
                        TopAppBar(
                            title = { },
                            actions = {
                                IconButton(
                                    onClick = {
                                        startActivity(Intent(this@Home, Settings::class.java))
                                    }) {
                                    Icon(
                                        ImageVector.vectorResource(R.drawable.ic_settings),
                                        contentDescription = "Settings"
                                    )
                                }
                                IconButton(
                                    onClick = {
                                        // TODO: go to profile
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
                    },
                    bottomBar = { Navbar(navController) },
                    floatingActionButton = { ExpandableFloatingActionButton(isFabExpanded) },
                    floatingActionButtonPosition = FabPosition.End
                ) { paddingValues ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues)
                    ) {
                        Box(modifier = Modifier.fillMaxSize()) {
                            Text("Home Screen", modifier = Modifier.align(Alignment.Center))
                        }
                    }
                }
            }
        }
    }
}


//                                FloatingActionButton(
//                                    onClick = {
//                                        navController.navigate(Routes.AddTransaction.route) {
//                                            popUpTo(navController.graph.startDestinationId) {
//                                                saveState = true
//                                            }
//                                            launchSingleTop = true
//                                            restoreState = true
//                                        }
//                                        isMenuExpanded = false
//                                    },
//                                    containerColor = MaterialTheme.colorScheme.primary,
//                                    modifier = Modifier.size(56.dp)
//                                ) {
//                                    Icon(
//                                        imageVector = ImageVector.vectorResource(R.drawable.ic_assignment),
//                                        contentDescription = "Add Transaction"
//                                    )
//                                }