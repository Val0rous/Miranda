package com.cashflowtracker.miranda.ui.screens

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cashflowtracker.miranda.R
import com.cashflowtracker.miranda.data.repositories.UsersRepository
import com.cashflowtracker.miranda.ui.theme.MirandaTheme
import org.koin.android.ext.android.inject

class Profile : ComponentActivity() {

    private val usersRepository: UsersRepository by inject()

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val email = intent.getStringExtra("email")
        Log.d("Profile", "Email passed to Profile activity: $email")  // Log per verificare l'email

        setContent {
            var userName by remember { mutableStateOf("Loading...") }

            LaunchedEffect(email) {
                if (email != null) {
                    try {
                        // Effettua la query per recuperare l'utente dal database in base all'email
                        val user = usersRepository.getByEmail(email)
                        if (user != null) {
                            userName = user.name
                        } else {
                            userName = "User not found"
                            Log.e("Profile", "User with email $email not found in the database.")
                        }
                    } catch (e: Exception) {
                        userName = "Error loading user"
                        Log.e("Profile", "Error loading user with email $email", e)
                    }
                } else {
                    userName = "Email is null"
                    Log.e("Profile", "No email passed to the Profile activity.")
                }
            }

            MirandaTheme {
                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = { Text("Profile") },
                            navigationIcon = {
                                IconButton(onClick = { finish() }) {
                                    Icon(
                                        ImageVector.vectorResource(R.drawable.ic_arrow_back),
                                        contentDescription = "Back"
                                    )
                                }
                            }
                        )
                    },
                    content = { paddingValues ->
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(paddingValues)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Start,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = 16.dp, top = 16.dp, bottom = 8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AccountCircle,
                                    contentDescription = "Profile Picture",
                                    tint = MaterialTheme.colorScheme.onBackground,
                                    modifier = Modifier
                                        .size(96.dp)
                                        .clip(CircleShape)
                                )

                                Column(
                                    horizontalAlignment = Alignment.Start,
                                    modifier = Modifier.padding(start = 16.dp)
                                ) {
                                    Text(
                                        text = userName,
                                        style = MaterialTheme.typography.titleLarge,
                                        modifier = Modifier
                                            .align(Alignment.CenterHorizontally)
                                            .padding(top = 10.dp)
                                    )

                                    AssistChip(
                                        onClick = { /* Azione per modificare il profilo */ },
                                        label = { Text("Edit profile") },
                                        leadingIcon = {
                                            Icon(
                                                imageVector = ImageVector.vectorResource(R.drawable.ic_person),
                                                contentDescription = "Edit Profile",
                                                tint = MaterialTheme.colorScheme.primary,
                                                modifier = Modifier.size(18.dp)
                                            )
                                        },
                                        modifier = Modifier.padding(top = 2.dp),
                                    )
                                }
                            }

                            HorizontalDivider(
                                color = MaterialTheme.colorScheme.surfaceContainerHighest,
                                thickness = 1.dp
                            )

                            var selectedTabIndex by remember { mutableStateOf(0) }
                            TabRow(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                selectedTabIndex = selectedTabIndex,
                            ) {
                                Tab(
                                    selected = selectedTabIndex == 0,
                                    onClick = { selectedTabIndex = 0 },
                                    text = {
                                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                            Icon(
                                                imageVector = ImageVector.vectorResource(R.drawable.ic_trophy),
                                                contentDescription = "Achievements"
                                            )
                                            Spacer(modifier = Modifier.height(2.dp))
                                            Text("Achievements")
                                        }
                                    }
                                )
                                Tab(
                                    selected = selectedTabIndex == 1,
                                    onClick = { selectedTabIndex = 1 },
                                    text = {
                                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                            Icon(
                                                imageVector = ImageVector.vectorResource(R.drawable.ic_query_stats),
                                                contentDescription = "Analytics"
                                            )
                                            Spacer(modifier = Modifier.height(2.dp))
                                            Text("Analytics")
                                        }
                                    }
                                )
                            }

                            Spacer(modifier = Modifier.height(24.dp))

                            when (selectedTabIndex) {
                                0 -> AchievementsList()
                                1 -> AnalyticsList()
                            }
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun AchievementsList() {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        items(6) { index -> // Placeholder per 6 risultati
            AchievementItem(
                title = "Achievement $index",
                description = "Descrizione dell'achievement $index"
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun AnalyticsList() {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        items(6) { index -> // Placeholder per 6 risultati
            AchievementItem(
                title = "Analytics $index",
                description = "Descrizione dell'analytics $index"
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun AchievementItem(title: String, description: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Surface(
            modifier = Modifier.size(40.dp),
            shape = CircleShape,
            color = MaterialTheme.colorScheme.primary
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(
                    text = title.first().toString(),
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontSize = 20.sp
                )
            }
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column {
            Text(text = title, fontWeight = FontWeight.Bold)
            Text(text = description, fontSize = 12.sp)
        }
    }
}
