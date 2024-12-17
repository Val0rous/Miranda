package com.cashflowtracker.miranda.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
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
                    .background(LocalCustomColors.current.surfaceTintBlue)
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(
                        AccountType.getIcon(
                            account.type
                        )
                    ),
                    contentDescription = account.type,
                    tint = LocalCustomColors.current.icon,
                    modifier = Modifier
                        .size(24.dp)
                        .align(Alignment.Center)
                )
            }
        },
        modifier = modifier
    )
//    HorizontalDivider()
}

@Composable
fun CategoryListItem(
    category: DefaultCategories,
    modifier: Modifier = Modifier,
    boxSize: Dp = 40.dp,
    iconSize: Dp = 24.dp,
    starSize: Dp = 24.dp,
    containerColor: Color = MaterialTheme.colorScheme.surface,
    textStyle: TextStyle = MaterialTheme.typography.bodyLarge,
    textPaddingStart: Dp = 0.dp,
    showCategory: Boolean = false,
    chipColor: Color = Color.Transparent
) {
    ListItem(
        headlineContent = {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start,
                modifier = Modifier.padding(start = textPaddingStart)
            ) {
                Text(
                    text = stringResource(category.category),
                    style = textStyle
                )
                if (showCategory) {
                    Text(
                        text = stringResource(category.type.label),
                        style = MaterialTheme.typography.labelMedium,
                        color = LocalCustomColors.current.icon,
                        modifier = Modifier
                            .padding(top = 4.dp)
                            .background(chipColor, RoundedCornerShape(100))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }
        },
        leadingContent = {
            Box(
                modifier = Modifier
                    .size(boxSize)
                    .clip(CircleShape)
                    //.background(MaterialTheme.colorScheme.surfaceTint)
                    .background(
                        color = when (category.type) {
                            CategoryClass.NECESSITY -> LocalCustomColors.current.surfaceTintRed
                            CategoryClass.CONVENIENCE -> LocalCustomColors.current.surfaceTintYellow
                            CategoryClass.LUXURY -> LocalCustomColors.current.surfaceTintGreen
                            else -> LocalCustomColors.current.surfaceTintBlue
                        }
                    )
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(category.icon),
                    contentDescription = stringResource(category.category),
                    tint = LocalCustomColors.current.icon,
                    modifier = Modifier
                        .size(iconSize)
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
                                    .size(starSize)
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
                                    .size(starSize)
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
                                    .size(starSize)
                            )
                        }
                    }

                CategoryClass.UNRANKED -> {}
            }
        },
        modifier = modifier,
        colors = ListItemDefaults.colors(containerColor = containerColor)
    )
//    HorizontalDivider()
}

@Composable
fun SpecialListItem(
    special: SpecialType,
    modifier: Modifier = Modifier,
    boxSize: Dp = 40.dp,
    iconSize: Dp = 24.dp,
    containerColor: Color = MaterialTheme.colorScheme.surface,
    textStyle: TextStyle = MaterialTheme.typography.bodyLarge,
    textPaddingStart: Dp = 0.dp,
    showSpecial: Boolean = false,
    chipColor: Color = Color.Transparent
) {
    ListItem(
        headlineContent = {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start,
                modifier = Modifier.padding(start = textPaddingStart)
            ) {
                Text(
                    text = stringResource(special.category),
                    style = textStyle
                )
                if (showSpecial) {
                    Text(
                        text = stringResource(R.string.category_class_special),
                        style = MaterialTheme.typography.labelMedium,
                        color = LocalCustomColors.current.icon,
                        modifier = Modifier
                            .padding(top = 4.dp)
                            .background(chipColor, RoundedCornerShape(100))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }
        },
        leadingContent = {
            Box(
                modifier = Modifier
                    .size(boxSize)
                    .clip(CircleShape)
                    .background(LocalCustomColors.current.surfaceTintDeepPurple)
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(
                        SpecialType.getIcon(
                            special.name
                        )
                    ),
                    contentDescription = stringResource(special.category),
                    tint = LocalCustomColors.current.icon,
                    modifier = Modifier
                        .size(iconSize)
                        .align(Alignment.Center)
                )
            }
        },
        modifier = modifier,
        colors = ListItemDefaults.colors(containerColor = containerColor)
    )
//    HorizontalDivider()
}