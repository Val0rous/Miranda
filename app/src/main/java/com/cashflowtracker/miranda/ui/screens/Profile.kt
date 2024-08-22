package com.cashflowtracker.miranda.ui.screens

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
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
            var selectedTabIndex by remember { mutableIntStateOf(0) }

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
                        ) {
                            // Profile picture and user name
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
                                        text = "Francesco Valentini",
                                        style = MaterialTheme.typography.titleLarge,
                                        modifier = Modifier
                                            .align(Alignment.CenterHorizontally)
                                            .padding(top = 10.dp)
                                    )

                                    // Pulsante per modificare il profilo
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

                            //Spacer(modifier = Modifier.height(24.dp))

                            HorizontalDivider(
                                color = MaterialTheme.colorScheme.surfaceContainerHighest,
                                thickness = 1.dp
                            )
                            // Achievements e Analytics
                            TabRow(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                selectedTabIndex = selectedTabIndex,
                                indicator = { tabPositions ->
                                    Box(
                                        Modifier
                                            .tabIndicatorOffset(tabPositions[selectedTabIndex])
                                            .height(3.dp)
                                            .padding(horizontal = 48.dp)
                                            .background(
                                                color = MaterialTheme.colorScheme.primary,
                                                shape = RoundedCornerShape(
                                                    topStart = 16.dp,
                                                    topEnd = 16.dp
                                                )
                                            )
                                    )
                                }
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

//                                TabButton(
//                                    text = "Achievements",
//                                    isSelected = selectedTabIndex == 0,
//                                    onClick = { selectedTabIndex = 0 },
//                                    modifier = Modifier.weight(1f)
//                                )
//                                TabButton(
//                                    text = "Analytics",
//                                    isSelected = selectedTabIndex == 1,
//                                    onClick = { selectedTabIndex = 1 },
//                                    modifier = Modifier.weight(1f)
//                                )
                            }

                            Spacer(modifier = Modifier.height(24.dp))

                            // Lista di risultati (Achievements o Analytics)
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