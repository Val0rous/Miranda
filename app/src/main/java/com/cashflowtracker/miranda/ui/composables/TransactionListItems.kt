package com.cashflowtracker.miranda.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.unit.dp
import com.cashflowtracker.miranda.R
import com.cashflowtracker.miranda.data.database.Account
import com.cashflowtracker.miranda.ui.theme.LocalCustomColors
import com.cashflowtracker.miranda.ui.theme.Green400
import com.cashflowtracker.miranda.ui.theme.Red400
import com.cashflowtracker.miranda.ui.theme.Yellow400
import com.cashflowtracker.miranda.utils.AccountType
import com.cashflowtracker.miranda.utils.CategoryClass
import com.cashflowtracker.miranda.utils.DefaultCategories
import com.cashflowtracker.miranda.utils.SpecialType

@Composable
fun AccountListItem(account: Account, modifier: Modifier) {
    ListItem(
        headlineContent = {
            Text(
                text = account.title,
                style = MaterialTheme.typography.bodyLarge
            )
        },
        leadingContent = {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surfaceTint)
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(
                        AccountType.getIcon(
                            account.type
                        )
                    ),
                    contentDescription = account.type,
                    tint = MaterialTheme.colorScheme.surface,
                    modifier = Modifier
                        .size(24.dp)
                        .align(Alignment.Center)
                )
            }
        },
        modifier = modifier
    )
    HorizontalDivider()
}

@Composable
fun CategoryListItem(category: DefaultCategories, modifier: Modifier) {
    ListItem(
        headlineContent = {
            Text(
                text = category.category,
                style = MaterialTheme.typography.bodyLarge
            )
        },
        leadingContent = {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    //.background(MaterialTheme.colorScheme.surfaceTint)
                    .background(
                        color = when (category.type) {
                            CategoryClass.NECESSITY -> LocalCustomColors.current.surfaceTintRed
                            CategoryClass.CONVENIENCE -> LocalCustomColors.current.surfaceTintYellow
                            CategoryClass.LUXURY -> LocalCustomColors.current.surfaceTintGreen
                        }
                    )
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(category.icon),
                    contentDescription = category.category,
                    tint = LocalCustomColors.current.icon,
                    modifier = Modifier
                        .size(24.dp)
                        .align(Alignment.Center)
                )
            }
        },
        trailingContent = {
            when (category.type) {
                CategoryClass.NECESSITY -> {
                    Row(horizontalArrangement = Arrangement.End) {
                        repeat(1) {
                            Icon(
                                imageVector = ImageVector.vectorResource(R.drawable.ic_star_filled),
                                contentDescription = "",
                                tint = Red400,
                                modifier = Modifier
                                    .size(24.dp)
                            )
                        }
                    }
                }

                CategoryClass.CONVENIENCE ->
                    Row(horizontalArrangement = Arrangement.End) {
                        repeat(2) {
                            Icon(
                                imageVector = ImageVector.vectorResource(R.drawable.ic_star_filled),
                                contentDescription = "",
                                tint = Yellow400,
                                modifier = Modifier
                                    .size(24.dp)
                            )
                        }
                    }

                CategoryClass.LUXURY ->
                    Row(horizontalArrangement = Arrangement.End) {
                        repeat(3) {
                            Icon(
                                imageVector = ImageVector.vectorResource(R.drawable.ic_star_filled),
                                contentDescription = "",
                                tint = Green400,
                                modifier = Modifier
                                    .size(24.dp)
                            )
                        }
                    }
            }
        },
        modifier = modifier
    )
    HorizontalDivider()
}

@Composable
fun SpecialListItem(item: SpecialType, modifier: Modifier) {
    ListItem(
        headlineContent = {
            Text(
                text = item.category,
                style = MaterialTheme.typography.bodyLarge
            )
        },
        leadingContent = {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surfaceTint)
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(
                        SpecialType.getIcon(
                            item.category
                        )
                    ),
                    contentDescription = item.category,
                    tint = MaterialTheme.colorScheme.surface,
                    modifier = Modifier
                        .size(24.dp)
                        .align(Alignment.Center)
                )
            }
        },
        modifier = modifier
    )
    HorizontalDivider()
}