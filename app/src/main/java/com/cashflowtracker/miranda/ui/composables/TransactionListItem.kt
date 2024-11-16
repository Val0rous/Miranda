package com.cashflowtracker.miranda.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.cashflowtracker.miranda.R
import com.cashflowtracker.miranda.data.database.Transaction
import com.cashflowtracker.miranda.ui.theme.LocalCustomColors
import com.cashflowtracker.miranda.utils.DefaultCategories
import com.cashflowtracker.miranda.utils.SpecialType
import com.cashflowtracker.miranda.utils.TransactionType
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

@Composable
fun TransactionListItem(
    type: String,
    createdOn: String,
    source: String,
    destination: String,
    amount: Double,
    comment: String,
    onClick: () -> Unit
) {
    ListItem(
        overlineContent = {
            Text(
                text = ZonedDateTime.parse(
                    createdOn,
                    DateTimeFormatter.ISO_ZONED_DATE_TIME
                ).format(DateTimeFormatter.ofPattern("MMM dd, yyyy  ·  HH:mm")),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        },
        leadingContent = {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(
                        when (type) {
                            TransactionType.OUTPUT.type -> LocalCustomColors.current.surfaceTintRed
                            TransactionType.INPUT.type -> LocalCustomColors.current.surfaceTintGreen
                            else -> LocalCustomColors.current.surfaceTintBlue
                        }
                    )
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(
                        when (type) {
                            TransactionType.OUTPUT.type -> DefaultCategories.getIcon(destination)

                            TransactionType.INPUT.type -> {
                                when (source) {
                                    SpecialType.POCKET.category, SpecialType.EXTRA.category -> SpecialType.getIcon(
                                        source
                                    )

                                    else -> DefaultCategories.getIcon(source)
                                }
                            }

                            else -> R.drawable.ic_sync
                        }
                    ),
                    contentDescription = type,
                    tint = LocalCustomColors.current.icon,
                    modifier = Modifier
                        .size(24.dp)
                        .align(Alignment.Center)
                )
            }
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
                text = when (type) {
                    TransactionType.OUTPUT.type -> if (amount != 0.0) "-%.2f €" else "%.2f €"
                    TransactionType.INPUT.type -> if (amount != 0.0) "+%.2f €" else "%.2f €"
                    else -> "%.2f €"
                }.format(amount),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
//                                when (transaction.type) {
//                                    "Output" -> CustomColors.current.surfaceTintRed
//                                    "Input" -> CustomColors.current.surfaceTintGreen
//                                    else -> CustomColors.current.surfaceTintBlue
//                                }
            )
        },
        modifier = Modifier.clickable { onClick() }
    )
}