package com.cashflowtracker.miranda.ui.composables

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cashflowtracker.miranda.R
import com.cashflowtracker.miranda.ui.theme.LocalCustomColors
import com.cashflowtracker.miranda.utils.Currencies
import com.cashflowtracker.miranda.utils.DefaultCategories
import com.cashflowtracker.miranda.utils.Repeats
import com.cashflowtracker.miranda.utils.SpecialType
import com.cashflowtracker.miranda.utils.TransactionType
import com.cashflowtracker.miranda.utils.formatAmount
import com.cashflowtracker.miranda.utils.formatDate
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
                    "Output" -> LocalCustomColors.current.surfaceTintRed
                    "Input" -> LocalCustomColors.current.surfaceTintGreen
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
                    "Output" -> LocalCustomColors.current.surfaceTintRed
                    "Input" -> LocalCustomColors.current.surfaceTintGreen
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