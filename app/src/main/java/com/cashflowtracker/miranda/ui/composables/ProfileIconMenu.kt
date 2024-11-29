package com.cashflowtracker.miranda.ui.composables

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.cashflowtracker.miranda.R
import com.cashflowtracker.miranda.data.repositories.PreferencesRepository.getProfilePicturePathFlow
import com.cashflowtracker.miranda.ui.screens.About
import com.cashflowtracker.miranda.ui.screens.Profile
import com.cashflowtracker.miranda.ui.screens.Settings
import com.cashflowtracker.miranda.ui.theme.LocalCustomColors
import com.cashflowtracker.miranda.ui.viewmodels.UsersViewModel
import com.cashflowtracker.miranda.utils.getInitials
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileIconMenu(
    showProfileIconMenu: MutableState<Boolean>,
    userName: String,
    userEmail: String
) {
    val context = LocalContext.current
//    val usersVm = koinViewModel<UsersViewModel>()
    val customColors = LocalCustomColors.current

    Dialog(
        onDismissRequest = { showProfileIconMenu.value = false },
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerHigh),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .clip(RoundedCornerShape(28.dp))
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(start = 4.dp, top = 4.dp, bottom = 0.dp)
            ) {
                IconButton(
                    onClick = {
                        showProfileIconMenu.value = false
                    },
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_close),
                        contentDescription = "Close"
                    )
                }
                Text(
                    text = stringResource(R.string.app_name),
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontSize = 22.sp,
                        fontWeight = FontWeight.W800,
                        letterSpacing = (0.4).sp
                    ),
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .padding(start = 16.dp, end = 56.dp)
                        .weight(1f),
                    textAlign = TextAlign.Center
                )
            }
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerHigh),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp, end = 10.dp, top = 4.dp)
                    .clip(RoundedCornerShape(24.dp))
            ) {
                ListItem(
                    headlineContent = {
                        Text(
                            text = userName,
                            style = MaterialTheme.typography.titleSmall,
                            modifier = Modifier
                                .offset(x = (-10).dp)
                                .padding(bottom = 1.dp)
                        )
                    },
                    supportingContent = {
                        Text(
                            text = userEmail,
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Normal),
                            modifier = Modifier
                                .offset(x = (-10).dp)
                                .padding(top = 1.dp)
                        )
                    },
                    leadingContent = {
                        ProfileButton(
                            modifier = Modifier
                                .offset(x = (-6).dp)
                                .padding(end = 0.dp)
                        )
                    },
                    colors = ListItemDefaults.colors(containerColor = customColors.cardSurface),
                    modifier = Modifier
                        .height(60.dp)
                        .clickable {
                            context
                                .startActivity(Intent(context, Profile::class.java))
                                .also {
                                    showProfileIconMenu.value = false
                                }
                        }
                )

                Spacer(modifier = Modifier.height(2.dp))

                ListItem(
                    headlineContent = {
                        Text(
                            text = "Custom Categories",
                            style = MaterialTheme.typography.titleSmall,
                            modifier = Modifier.offset(x = (-2).dp)
                        )
                    },
                    leadingContent = {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.ic_category),
                            contentDescription = "Custom Categories",
                            modifier = Modifier
                                .offset(x = (-6).dp)
                                .padding(start = 10.dp)
                                .size(22.dp)
                        )
                    },
                    colors = ListItemDefaults.colors(containerColor = customColors.cardSurface),
                    modifier = Modifier
                        .height(52.dp)
                        .clickable {
                            showProfileIconMenu.value = false
                        }
                )

                Spacer(modifier = Modifier.height(2.dp))

                ListItem(
                    headlineContent = {
                        Text(
                            text = "Archive",
                            style = MaterialTheme.typography.titleSmall,
                            modifier = Modifier.offset(x = (-2).dp)
                        )
                    },
                    leadingContent = {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.ic_history),
                            contentDescription = "Archive",
                            modifier = Modifier
                                .offset(x = (-6).dp)
                                .padding(start = 10.dp)
                                .size(22.dp)
                        )
                    },
                    colors = ListItemDefaults.colors(containerColor = customColors.cardSurface),
                    modifier = Modifier
                        .height(52.dp)
                        .clickable {
                            showProfileIconMenu.value = false
                        }
                )
            }
            ListItem(
                headlineContent = {
                    Text(
                        text = "Settings",
                        style = MaterialTheme.typography.titleSmall,
                        modifier = Modifier.offset(x = (-2).dp)
                    )
                },
                leadingContent = {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_settings),
                        contentDescription = "Settings",
                        modifier = Modifier
                            .offset(x = (-4).dp)
                            .padding(start = 18.dp)
                            .size(22.dp)
                    )
                },
                colors = ListItemDefaults.colors(containerColor = MaterialTheme.colorScheme.surfaceContainerHigh),
                modifier = Modifier
                    .height(52.dp)
                    .clickable {
                        context
                            .startActivity(Intent(context, Settings::class.java))
                            .also {
                                showProfileIconMenu.value = false
                            }
                    }
            )
            ListItem(
                headlineContent = {
                    Text(
                        text = "About",
                        style = MaterialTheme.typography.titleSmall,
                        modifier = Modifier.offset(x = (-2).dp)
                    )
                },
                leadingContent = {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_info),
                        contentDescription = "About",
                        modifier = Modifier
                            .offset(x = (-4).dp)
                            .padding(start = 18.dp)
                            .size(22.dp)
                    )
                },
                colors = ListItemDefaults.colors(containerColor = MaterialTheme.colorScheme.surfaceContainerHigh),
                modifier = Modifier
                    .height(52.dp)
                    .clickable {
                        context
                            .startActivity(Intent(context, About::class.java))
                            .also {
                                showProfileIconMenu.value = false
                            }
                    }
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .padding(bottom = 4.dp)
                    .height(40.dp)
            ) {
                Spacer(modifier = Modifier.weight(1f))
                TextButton(
                    onClick = {
                        showProfileIconMenu.value = false
                    },
                    modifier = Modifier
                ) {
                    Text(
                        text = "Privacy Policy",
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Normal
                        ),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
                Text(
                    text = " • ",
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Normal
                    ),
                    color = MaterialTheme.colorScheme.onSurface
                )
                TextButton(
                    onClick = {
                        showProfileIconMenu.value = false
                    },
                    modifier = Modifier
                ) {
                    Text(
                        text = "Terms of Service",
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Normal
                        ),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
            }
        }
    }
}