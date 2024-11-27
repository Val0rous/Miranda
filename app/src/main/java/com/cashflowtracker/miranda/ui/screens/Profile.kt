package com.cashflowtracker.miranda.ui.screens

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.cashflowtracker.miranda.R
import com.cashflowtracker.miranda.data.repositories.LoginRepository.getCurrentUserEmail
import com.cashflowtracker.miranda.data.repositories.PreferencesRepository.getProfilePicturePathFlow
import com.cashflowtracker.miranda.data.repositories.UsersRepository
import com.cashflowtracker.miranda.ui.theme.MirandaTheme
import com.cashflowtracker.miranda.ui.viewmodels.UsersViewModel
import com.cashflowtracker.miranda.utils.AccountType
import com.cashflowtracker.miranda.utils.getInitials
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.compose.koinViewModel

class Profile : ComponentActivity() {

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        //    private val usersRepository: UsersRepository by inject()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            var userName by remember { mutableStateOf("") }
            var selectedTabIndex by remember { mutableIntStateOf(0) }
            val context: Context = LocalContext.current
            val email = context.getCurrentUserEmail()
            val usersVm = koinViewModel<UsersViewModel>()
            val usersState by usersVm.state.collectAsStateWithLifecycle()
            val profilePicturePathFlow = remember { context.getProfilePicturePathFlow() }
            val profilePicturePath by profilePicturePathFlow.collectAsState(initial = null)

            LaunchedEffect(email) {
                CoroutineScope(Dispatchers.IO).launch {
                    val user = usersVm.actions.getByEmail(email)
                    userName = user.name
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

                                Box(
                                    modifier = Modifier
                                        .size(128.dp)
                                        .clip(CircleShape)
                                        .background(MaterialTheme.colorScheme.primary, CircleShape)
                                        .clickable { }
                                ) {
                                    if (profilePicturePath != null && profilePicturePath!!.isNotEmpty()) {
                                        profilePicturePath?.let { path ->
                                            AsyncImage(
                                                model = ImageRequest.Builder(context)
                                                    .data(Uri.parse(path)).crossfade(true).build(),
                                                contentDescription = "Profile picture"
                                            )
                                        }
                                    } else {
                                        Text(
                                            text = getInitials(userName),
                                            style = MaterialTheme.typography.displayMedium,
                                            color = MaterialTheme.colorScheme.onPrimary,
                                            modifier = Modifier
//                                                .size(64.dp)
                                                .align(Alignment.Center)
                                        )
//                                        Icon(
//                                            imageVector = Icons.Default.AccountCircle,
//                                            contentDescription = "Profile Picture",
//                                            tint = MaterialTheme.colorScheme.onPrimary,
//                                            modifier = Modifier
//                                                .size(96.dp)
//                                                .clip(CircleShape)
//                                                .align(Alignment.Center)
//                                        )
                                    }
                                }

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
                                        onClick = {
                                            startActivity(
                                                Intent(
                                                    this@Profile,
                                                    EditProfile::class.java
                                                )
                                            )
                                        },
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
                                thickness = 1.dp,
                                modifier = Modifier.padding(top = 8.dp)
                            )

                            TabRow(
                                modifier = Modifier.fillMaxWidth(),
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
                            }


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
    ) {
        items(6) { index -> // Placeholder per 6 risultati
            AchievementItem(
                title = "Achievement $index",
                description = "Description of achievement $index"
            )
        }
    }
}

@Composable
fun AnalyticsList() {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
    ) {
        items(6) { index -> // Placeholder per 6 risultati
            AchievementItem(
                title = "Analytics $index",
                description = "Description of analytics $index"
            )
        }
    }
}

@Composable
fun AchievementItem(title: String, description: String) {
    ListItem(
        headlineContent = { Text(text = title, fontWeight = FontWeight.SemiBold) },
        supportingContent = { Text(text = description) },
        leadingContent = {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surfaceTint)
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_trophy_filled),
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.surface,
                    modifier = Modifier
                        .size(24.dp)
                        .align(Alignment.Center)
                )
            }
        },
        trailingContent = {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer)
                    .border(
                        width = 3.dp,
                        color = MaterialTheme.colorScheme.primary,
                        shape = CircleShape
                    )
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_diamond),
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .size(24.dp)
                        .align(Alignment.Center)
                )
            }
        },
        modifier = Modifier.clickable {

        }
    )

}
