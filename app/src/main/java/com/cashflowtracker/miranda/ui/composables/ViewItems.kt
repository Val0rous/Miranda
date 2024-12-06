package com.cashflowtracker.miranda.ui.composables

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.cashflowtracker.miranda.R
import com.cashflowtracker.miranda.data.database.BaseTransaction
import com.cashflowtracker.miranda.data.database.Notification
import com.cashflowtracker.miranda.ui.theme.Green400
import com.cashflowtracker.miranda.ui.theme.LocalCustomColors
import com.cashflowtracker.miranda.ui.theme.Red400
import com.cashflowtracker.miranda.ui.theme.Yellow400
import com.cashflowtracker.miranda.ui.viewmodels.AccountsViewModel
import com.cashflowtracker.miranda.utils.AccountType
import com.cashflowtracker.miranda.utils.CategoryClass
import com.cashflowtracker.miranda.utils.Coordinates
import com.cashflowtracker.miranda.utils.Currencies
import com.cashflowtracker.miranda.utils.DefaultCategories
import com.cashflowtracker.miranda.utils.GetStarIcons
import com.cashflowtracker.miranda.utils.Notifications
import com.cashflowtracker.miranda.utils.Repeats
import com.cashflowtracker.miranda.utils.SpecialType
import com.cashflowtracker.miranda.utils.TransactionType
import com.cashflowtracker.miranda.utils.backgroundColorFactory
import com.cashflowtracker.miranda.utils.formatAmount
import com.cashflowtracker.miranda.utils.formatDestination
import com.cashflowtracker.miranda.utils.formatRenewal
import com.cashflowtracker.miranda.utils.formatSource
import com.cashflowtracker.miranda.utils.formatZonedDateTime
import com.cashflowtracker.miranda.utils.textColorFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import java.util.UUID

@Composable
fun TransactionBubblesToFrom(
    transaction: BaseTransaction,
    sourceType: String,
    destinationType: String
) {
    val coroutineScope = rememberCoroutineScope()
    val accountsVm = koinViewModel<AccountsViewModel>()
    var sourceText by remember { mutableStateOf(transaction.source) }
    LaunchedEffect(transaction.source) {
        if ((transaction.type == TransactionType.OUTPUT.name
                    || transaction.type == TransactionType.TRANSFER.name)
            && transaction.source.isNotEmpty()
        ) {
            coroutineScope.launch(Dispatchers.IO) {
                sourceText =
                    accountsVm.actions.getByAccountId(UUID.fromString(transaction.source)).title
            }
        } else {
            sourceText = transaction.source
        }
    }
    var destinationText by remember { mutableStateOf(transaction.destination) }
    LaunchedEffect(transaction.destination) {
        if ((transaction.type == TransactionType.INPUT.name
                    || transaction.type == TransactionType.TRANSFER.name)
            && transaction.destination.isNotEmpty()
        ) {
            coroutineScope.launch(Dispatchers.IO) {
                destinationText =
                    accountsVm.actions.getByAccountId(UUID.fromString(transaction.destination)).title
            }
        } else {
            destinationText = transaction.destination
        }
    }

    Row(
        verticalAlignment = Alignment.Top,
        modifier = Modifier.padding(bottom = 24.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.width(128.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(backgroundColorFactory(transaction.type))
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(
                        when (transaction.type) {
                            TransactionType.OUTPUT.name -> AccountType.getIcon(
                                sourceType
                            )

                            TransactionType.INPUT.name -> {
                                when (transaction.source) {
                                    SpecialType.POCKET.name, SpecialType.EXTRA.name -> SpecialType.getIcon(
                                        transaction.source
                                    )

                                    else -> DefaultCategories.getIcon(
                                        transaction.source
                                    )
                                }
                            }

                            else -> AccountType.getIcon(sourceType)
                        }
                    ),
                    contentDescription = transaction.source,
                    tint = LocalCustomColors.current.icon,
                    modifier = Modifier
                        .size(40.dp)
                        .align(Alignment.Center)
                )
            }
            Text(
                text = formatSource(sourceText, transaction.type),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(top = 8.dp)
            )
            if (transaction.type == TransactionType.INPUT.name) {
                GetStarIcons(transaction.source)
            }
        }

        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.ic_east),
            contentDescription = "To",
            tint = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier
                .size(24.dp)
                .offset(y = 16.dp)
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .width(128.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(backgroundColorFactory(transaction.type))
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(
                        when (transaction.type) {
                            TransactionType.OUTPUT.name -> DefaultCategories.getIcon(
                                transaction.destination
                            )

                            TransactionType.INPUT.name -> AccountType.getIcon(
                                destinationType
                            )

                            else -> AccountType.getIcon(
                                destinationType
                            )
                        }
                    ),
                    contentDescription = transaction.destination,
                    tint = LocalCustomColors.current.icon,
                    modifier = Modifier
                        .size(40.dp)
                        .align(Alignment.Center)
                )
            }
            Text(
                text = formatDestination(destinationText, transaction.type),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(top = 8.dp)
            )
            if (transaction.type == TransactionType.OUTPUT.name) {
                GetStarIcons(transaction.destination)
            }
        }
    }
}

@Composable
fun TransactionViewer(
    type: String,
    dateTime: String,
    amount: Double,
    currency: String,
    comment: String,
    context: Context
) {
    Spacer(modifier = Modifier.height(8.dp))
    Card(
        colors = CardDefaults.cardColors(
            containerColor = LocalCustomColors.current.cardSurface
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clip(RoundedCornerShape(24.dp))
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 16.dp)
        ) {
            if (dateTime.isNotEmpty()) {
                Text(
                    text = formatZonedDateTime(context, dateTime),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            Text(
                text = formatAmount(
                    amount, Currencies.get(currency), type
                ),
                style = MaterialTheme.typography.headlineMedium,
                color = textColorFactory(type),
                modifier = Modifier.padding(top = 24.dp)
            )

            if (comment.isNotEmpty()) {
                Text(
                    text = comment,
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(top = 24.dp)
                        .padding(horizontal = 24.dp)
                )
            }
        }
    }
    Spacer(modifier = Modifier.height(16.dp))
}

@Composable
fun MapViewer(coordinates: Coordinates, isLocationLoaded: MutableState<Boolean>) {
    MapScreen(
        latitude = coordinates.latitude,
        longitude = coordinates.longitude,
        isLocationLoaded = isLocationLoaded,
        modifier = Modifier.padding(horizontal = 16.dp)
    )
}

@Composable
fun RepeatsPillCard(repeat: Repeats) {
    PillCard(
        icon = R.drawable.ic_replay,
        description = "Repeat",
        text = repeat.label,
        modifier = Modifier
            .padding(end = 16.dp)
            .scale(
                scaleX = -1f,
                scaleY = 1f
            )   // Flip horizontally
            .rotate(-45f)
    )
}

@Composable
fun NotificationsPillCard(notifications: List<Notification>) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = LocalCustomColors.current.cardSurface
        ),
        modifier = Modifier
            .fillMaxWidth()
//            .height(48.dp)
            .heightIn(min = 48.dp, max = (5 * 48 + 4 * 16).dp)
            .padding(horizontal = 16.dp)
            .clip(RoundedCornerShape(24.dp))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.Top
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_notifications),
                contentDescription = "Notifications",
                modifier = Modifier.padding(end = 16.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            LazyColumn() {
                items(notifications) {
                    Text(Notifications.valueOf(it.notificationType).label)
                    if (it != notifications.last()) {
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
        }
    }
    Spacer(modifier = Modifier.height(16.dp))
}

@Composable
fun CreationPillCard(creationDate: String) {
    PillCard(
        icon = R.drawable.ic_history,
        description = "Creation date",
        text = "Since $creationDate"
    )
}

@Composable
fun RenewalPillCard(reoccursOn: String) {
    PillCard(
        icon = R.drawable.ic_schedule,
        description = "Renewal",
        text = formatRenewal(reoccursOn)
    )
}

@Composable
fun IsRecurrencePillCard() {
    PillCard(
        icon = R.drawable.ic_schedule,
        description = "",
        text = "Created with recurrence"
    )
}

@Composable
fun PillCard(
    icon: Int,
    description: String,
    text: String,
    modifier: Modifier = Modifier.padding(end = 16.dp)
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = LocalCustomColors.current.cardSurface
        ),
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .padding(horizontal = 16.dp)
            .clip(RoundedCornerShape(24.dp))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(icon),
                contentDescription = description,
                modifier = modifier
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(text)
        }
    }
    Spacer(modifier = Modifier.height(16.dp))
}