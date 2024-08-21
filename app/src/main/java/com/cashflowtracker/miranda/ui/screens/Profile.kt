package com.cashflowtracker.miranda.ui.screens

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cashflowtracker.miranda.R
import com.cashflowtracker.miranda.ui.theme.MirandaTheme

class Profile : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val selectedTab = remember { mutableStateOf("Achievements") }

            MirandaTheme() {
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
                                .padding(16.dp)
                        ) {
                            Spacer(modifier = Modifier.height(24.dp))

                            // Immagine del profilo e nome utente
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AccountCircle,
                                    contentDescription = "Profile Image",
                                    tint = MaterialTheme.colorScheme.onBackground,
                                    modifier = Modifier
                                        .size(72.dp)
                                        .clip(CircleShape)
                                )

                                Spacer(modifier = Modifier.width(16.dp))

                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(
                                        text = "Francesco Valentini",
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.align(Alignment.CenterHorizontally)
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))

                                    // Pulsante per modificare il profilo
                                    OutlinedButton(
                                        onClick = { /* Azione per modificare il profilo */ },
                                        modifier = Modifier.padding(top = 8.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Edit,
                                            contentDescription = "Edit Profile",
                                            tint = MaterialTheme.colorScheme.primary,
                                            modifier = Modifier.size(18.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            "Edit profile",
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(24.dp))

                            // Achievements e Analytics
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp)
                            ) {
                                TabButton(
                                    text = "Achievements",
                                    isSelected = selectedTab.value == "Achievements",
                                    onClick = { selectedTab.value = "Achievements" },
                                    modifier = Modifier.weight(1f)
                                )
                                TabButton(
                                    text = "Analytics",
                                    isSelected = selectedTab.value == "Analytics",
                                    onClick = { selectedTab.value = "Analytics" },
                                    modifier = Modifier.weight(1f)
                                )
                            }

                            Spacer(modifier = Modifier.height(24.dp))

                            // Lista di risultati (Achievements o Analytics)
                            if (selectedTab.value == "Achievements") {
                                AchievementsList()
                            } else {
                                AnalyticsList()
                            }
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun TabButton(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    TextButton(
        onClick = onClick,
        modifier = modifier
            .padding(horizontal = 8.dp)
            .wrapContentSize(Alignment.Center)
    ) {
        Text(
            text = text,
            color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
        )
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