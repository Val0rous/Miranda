package com.cashflowtracker.miranda.ui.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.cashflowtracker.miranda.R
import com.cashflowtracker.miranda.ui.theme.LocalCustomColors
import com.cashflowtracker.miranda.utils.Currencies
import com.cashflowtracker.miranda.utils.Repeats
import com.cashflowtracker.miranda.utils.TransactionType
import com.cashflowtracker.miranda.utils.formatAmount
import com.cashflowtracker.miranda.utils.formatDate
import com.cashflowtracker.miranda.utils.formatRenewal
import com.cashflowtracker.miranda.utils.formatTime
import com.cashflowtracker.miranda.utils.iconFactory
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

@Composable
fun TransactionListItem(
    type: String,
    dateTime: String,
    source: String,
    destination: String,
    amount: Double,
    currency: Currencies,
    comment: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val zdt = ZonedDateTime.parse(
        dateTime,
        DateTimeFormatter.ISO_ZONED_DATE_TIME
    )
    val date = zdt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
    val time = zdt.format(DateTimeFormatter.ofPattern("HH:mm"))
    ListItem(
        overlineContent = {
            Text(
                text = "${formatDate(date)}  ·  ${formatTime(LocalContext.current, time)}",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        },
        leadingContent = {
            IconWithBackground(
                icon = iconFactory(type, source, destination),
                iconSize = 24.dp,
                iconColor = LocalCustomColors.current.icon,
                backgroundSize = 40.dp,
                backgroundColor = when (type) {
                    TransactionType.OUTPUT.name -> LocalCustomColors.current.surfaceTintRed
                    TransactionType.INPUT.name -> LocalCustomColors.current.surfaceTintGreen
                    else -> LocalCustomColors.current.surfaceTintBlue
                }
            )
        },
        headlineContent = {
            Text(
                text = comment,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(top = 4.dp)
            )
        },
        trailingContent = {
            Text(
                text = formatAmount(amount, currency, type),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
//                                when (transaction.type) {
//                                    "Output" -> CustomColors.current.surfaceTintRed
//                                    "Input" -> CustomColors.current.surfaceTintGreen
//                                    else -> CustomColors.current.surfaceTintBlue
//                                }
            )
        },
        modifier = modifier.clickable { onClick() }
    )
}

@Composable
fun RecurrenceListItem(
    type: String,
    dateTime: String,
    source: String,
    destination: String,
    amount: Double,
    currency: Currencies,
    comment: String,
    repeat: Repeats,
    reoccursOn: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val zdt = ZonedDateTime.parse(
        dateTime,
        DateTimeFormatter.ISO_ZONED_DATE_TIME
    )
    val date = zdt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
    val time = zdt.format(DateTimeFormatter.ofPattern("HH:mm"))
    ListItem(
        overlineContent = {
            Text(
                text = "${formatDate(date)}  ·  ${formatTime(LocalContext.current, time)}",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        },
        leadingContent = {
            IconWithBackground(
                icon = iconFactory(type, source, destination),
                iconSize = 24.dp,
                iconColor = LocalCustomColors.current.icon,
                backgroundSize = 40.dp,
                backgroundColor = when (type) {
                    TransactionType.OUTPUT.name -> LocalCustomColors.current.surfaceTintRed
                    TransactionType.INPUT.name -> LocalCustomColors.current.surfaceTintGreen
                    else -> LocalCustomColors.current.surfaceTintBlue
                }
            )
        },
        headlineContent = {
            Text(
                text = comment,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(top = 4.dp)
            )
        },
        supportingContent = {
//            Card(
//                colors = CardDefaults.cardColors(
//                    containerColor = LocalCustomColors.current.cardSurface
//                ),
//                modifier = Modifier
//                    .padding(top = 4.dp)
//                    .height(21.dp)
//                    .clip(RoundedCornerShape(50))
//            ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.padding(start = 1.dp, top = 3.dp, bottom = 1.dp, end = 6.dp)
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_schedule),
                    contentDescription = "Repeat",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier
                        .size(12.dp)
                )
                Text(
                    text = formatRenewal(reoccursOn).lowercase(),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
//            }
        },
        trailingContent = {
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = formatAmount(amount, currency, type),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
//                                when (transaction.type) {
//                                    "Output" -> CustomColors.current.surfaceTintRed
//                                    "Input" -> CustomColors.current.surfaceTintGreen
//                                    else -> CustomColors.current.surfaceTintBlue
//                                }
                )
                Text(
                    text = repeat.label.lowercase(),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        },
        modifier = modifier.clickable { onClick() }
    )
}