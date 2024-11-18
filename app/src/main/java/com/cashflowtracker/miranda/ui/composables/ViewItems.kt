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
import androidx.compose.runtime.MutableState
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
import com.cashflowtracker.miranda.data.database.Transaction
import com.cashflowtracker.miranda.ui.theme.Green400
import com.cashflowtracker.miranda.ui.theme.LocalCustomColors
import com.cashflowtracker.miranda.ui.theme.Red400
import com.cashflowtracker.miranda.ui.theme.Yellow400
import com.cashflowtracker.miranda.utils.AccountType
import com.cashflowtracker.miranda.utils.CategoryClass
import com.cashflowtracker.miranda.utils.Coordinates
import com.cashflowtracker.miranda.utils.Currencies
import com.cashflowtracker.miranda.utils.DefaultCategories
import com.cashflowtracker.miranda.utils.Notifications
import com.cashflowtracker.miranda.utils.Repeats
import com.cashflowtracker.miranda.utils.SpecialType
import com.cashflowtracker.miranda.utils.formatAmount
import com.cashflowtracker.miranda.utils.formatZonedDateTime

@Composable
fun TransactionBubblesToFrom(
    transaction: BaseTransaction,
    sourceType: String,
    destinationType: String
) {
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
                    .background(
                        when (transaction.type) {
                            "Output" -> LocalCustomColors.current.surfaceTintRed
                            "Input" -> LocalCustomColors.current.surfaceTintGreen
                            else -> LocalCustomColors.current.surfaceTintBlue
                        }
                    )
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(
                        when (transaction.type) {
                            "Output" -> AccountType.getIcon(
                                sourceType
                            )

                            "Input" -> {
                                when (transaction.source) {
                                    SpecialType.POCKET.category, SpecialType.EXTRA.category -> SpecialType.getIcon(
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
                text = transaction.source,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(top = 8.dp)
            )
            if (transaction.type == "Input") {
                Row(modifier = Modifier.padding(top = 4.dp)) {
                    when (DefaultCategories.getType(transaction.source)) {
                        CategoryClass.NECESSITY -> repeat(1) {
                            Icon(
                                imageVector = ImageVector.vectorResource(
                                    R.drawable.ic_star_filled
                                ),
                                contentDescription = "",
                                tint = Red400,
                                modifier = Modifier
                                    .size(24.dp)
                            )
                        }

                        CategoryClass.CONVENIENCE -> repeat(2) {
                            Icon(
                                imageVector = ImageVector.vectorResource(
                                    R.drawable.ic_star_filled
                                ),
                                contentDescription = "",
                                tint = Yellow400,
                                modifier = Modifier
                                    .size(24.dp)
                            )
                        }

                        CategoryClass.LUXURY -> repeat(3) {
                            Icon(
                                imageVector = ImageVector.vectorResource(
                                    R.drawable.ic_star_filled
                                ),
                                contentDescription = "",
                                tint = Green400,
                                modifier = Modifier
                                    .size(24.dp)
                            )
                        }
                    }
                }
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
                    .background(
                        when (transaction.type) {
                            "Output" -> LocalCustomColors.current.surfaceTintRed
                            "Input" -> LocalCustomColors.current.surfaceTintGreen
                            else -> LocalCustomColors.current.surfaceTintBlue
                        }
                    )
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(
                        when (transaction.type) {
                            "Output" -> DefaultCategories.getIcon(
                                transaction.destination
                            )

                            "Input" -> AccountType.getIcon(
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
                text = transaction.destination,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(top = 8.dp)
            )
            if (transaction.type == "Output") {
                Row(modifier = Modifier.padding(top = 4.dp)) {
                    when (DefaultCategories.getType(transaction.destination)) {
                        CategoryClass.NECESSITY -> repeat(1) {
                            Icon(
                                imageVector = ImageVector.vectorResource(
                                    R.drawable.ic_star_filled
                                ),
                                contentDescription = "",
                                tint = Red400,
                                modifier = Modifier
                                    .size(24.dp)
                            )
                        }

                        CategoryClass.CONVENIENCE -> repeat(2) {
                            Icon(
                                imageVector = ImageVector.vectorResource(
                                    R.drawable.ic_star_filled
                                ),
                                contentDescription = "",
                                tint = Yellow400,
                                modifier = Modifier
                                    .size(24.dp)
                            )
                        }

                        CategoryClass.LUXURY -> repeat(3) {
                            Icon(
                                imageVector = ImageVector.vectorResource(
                                    R.drawable.ic_star_filled
                                ),
                                contentDescription = "",
                                tint = Green400,
                                modifier = Modifier
                                    .size(24.dp)
                            )
                        }
                    }
                }
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
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow
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
                color = when (type) {
                    "Output" -> LocalCustomColors.current.surfaceTintRed
                    "Input" -> LocalCustomColors.current.surfaceTintGreen
                    else -> LocalCustomColors.current.surfaceTintBlue
                },
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
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow
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
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow
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