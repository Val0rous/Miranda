package com.cashflowtracker.miranda.ui.composables

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.cashflowtracker.miranda.R
import com.cashflowtracker.miranda.data.repositories.LoginRepository.getCurrentUserEmail
import com.cashflowtracker.miranda.data.repositories.PreferencesRepository.getProfilePicturePathFlow
import com.cashflowtracker.miranda.ui.screens.MapView
import com.cashflowtracker.miranda.ui.screens.Settings
import com.cashflowtracker.miranda.ui.viewmodels.UsersViewModel
import com.cashflowtracker.miranda.utils.Routes
import com.cashflowtracker.miranda.utils.getInitials
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun ProfileButton(
    showProfileIconMenu: MutableState<Boolean> = mutableStateOf(false),
    isBigPic: Boolean = false,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {
        if (!isBigPic) {
            showProfileIconMenu.value = true
        } else {
            // Do nothing
        }
    }
) {
    val context = LocalContext.current
    val email = context.getCurrentUserEmail()
    var userName by remember { mutableStateOf("") }
    var userEmail by remember { mutableStateOf("") }
    val usersVm = koinViewModel<UsersViewModel>()
    val profilePicturePathFlow = remember { context.getProfilePicturePathFlow() }
    val profilePicturePath by profilePicturePathFlow.collectAsState(initial = null)

    val size = if (!isBigPic) 40.dp else 128.dp

    LaunchedEffect(email) {
        CoroutineScope(Dispatchers.IO).launch {
            val user = usersVm.actions.getByEmail(email)
            userName = user.name
            userEmail = user.email
        }
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.primary, CircleShape)
            .clickable { onClick() }
    ) {
        if (profilePicturePath != null && profilePicturePath!!.isNotEmpty()) {
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(Uri.parse(profilePicturePath)).crossfade(true)
                    .build(),
                contentDescription = "Profile picture",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(size)
                    .clip(CircleShape)
            )
        } else {
            Text(
                text = getInitials(userName),
                style = if (!isBigPic) {
                    MaterialTheme.typography.titleMedium
                } else {
                    MaterialTheme.typography.displayMedium
                },
                color = MaterialTheme.colorScheme.onPrimary,
            )
        }
    }

    if (showProfileIconMenu.value) {
        ProfileIconMenu(showProfileIconMenu, userName, userEmail)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainTopAppBar(currentRoute: String?, showProfileIconMenu: MutableState<Boolean>) {
    val context = LocalContext.current

    TopAppBar(
        title = {
            val text = when (currentRoute) {
                Routes.Home.route -> "Miranda"
                Routes.Transactions.route -> "Transactions"
                Routes.Recurrents.route -> "Recurrents"
                Routes.Stats.route -> "Stats"
                else -> ""
            }
            Text(
                text = text,
                style = if (currentRoute == Routes.Home.route) {
                    MaterialTheme.typography.titleLarge.copy(
                        fontSize = 22.sp,
                        fontWeight = FontWeight.SemiBold,
                        letterSpacing = (0.4).sp
                    )
                } else {
                    MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Normal)
                },
                color = if (currentRoute == Routes.Home.route) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.onSurfaceVariant
                }
            )
        },
        actions = {
            if (currentRoute == Routes.Transactions.route
                || currentRoute == Routes.Recurrents.route
            ) {
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
            }
            if (currentRoute == Routes.Transactions.route) {
                IconButton(
                    onClick = {
                        context.startActivity(Intent(context, MapView::class.java))
                    }
                ) {
                    Icon(
                        ImageVector.vectorResource(R.drawable.ic_map),
                        contentDescription = "Map View"
                    )
                }
            }
//            IconButton(
//                onClick = {
//                    context.startActivity(Intent(context, Settings::class.java))
//                }) {
//                Icon(
//                    ImageVector.vectorResource(R.drawable.ic_settings),
//                    contentDescription = "Settings"
//                )
//            }
            ProfileButton(
                showProfileIconMenu,
                modifier = Modifier.padding(start = 10.dp, end = 12.dp)
            )
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