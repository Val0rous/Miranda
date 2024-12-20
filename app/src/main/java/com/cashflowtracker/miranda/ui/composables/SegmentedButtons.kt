@file:OptIn(ExperimentalMaterial3Api::class)

package com.cashflowtracker.miranda.ui.composables

import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.cashflowtracker.miranda.R
import com.cashflowtracker.miranda.data.repositories.ThemeRepository.getSystemDefaultTheme
import com.cashflowtracker.miranda.utils.TransactionType

@Composable
fun SegmentedButtonType(
    transactionType: MutableState<String>,
    isTypeChanged: MutableState<Boolean>,
    modifier: Modifier
) {
    var selectedIndex by remember {
        mutableIntStateOf(
            when (transactionType.value) {
                TransactionType.OUTPUT.name -> 0
                TransactionType.INPUT.name -> 1
                TransactionType.TRANSFER.name -> 2
                else -> {
                    transactionType.value = TransactionType.OUTPUT.name
                    0
                }
            }
        )
    }

    SingleChoiceSegmentedButtonRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        SegmentedButton(
            selected = selectedIndex == 0,
            onClick = {
                selectedIndex = 0
                isTypeChanged.value = transactionType.value != TransactionType.OUTPUT.name
                transactionType.value = TransactionType.OUTPUT.name
            },
            shape = SegmentedButtonDefaults.itemShape(
                index = 0,
                count = 3
            ),
            modifier = modifier,
        ) {
            Row {
                if (selectedIndex != 0) {
                    // Workaround to display the regular icon when the button is not selected
                    SBIcon(inactive = true, icon = R.drawable.ic_upload)
                    Spacer(modifier = Modifier.width(8.dp))
                }
                Text(
                    text = stringResource(R.string.transaction_type_output),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }

        SegmentedButton(
            selected = selectedIndex == 1,
            onClick = {
                selectedIndex = 1
                isTypeChanged.value = transactionType.value != TransactionType.INPUT.name
                transactionType.value = TransactionType.INPUT.name
            },
            shape = SegmentedButtonDefaults.itemShape(
                index = 1,
                count = 3
            ),
            modifier = modifier,
        ) {
            Row {
                if (selectedIndex != 1) {
                    // Workaround to display the regular icon when the button is not selected
                    SBIcon(inactive = true, icon = R.drawable.ic_download)
                    Spacer(modifier = Modifier.width(8.dp))
                }
                Text(
                    text = stringResource(R.string.transaction_type_input),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }

        SegmentedButton(
            selected = selectedIndex == 2,
            onClick = {
                selectedIndex = 2
                isTypeChanged.value = transactionType.value != TransactionType.TRANSFER.name
                transactionType.value = TransactionType.TRANSFER.name
            },
            shape = SegmentedButtonDefaults.itemShape(
                index = 2,
                count = 3
            ),
            modifier = modifier,
        ) {
            Row {
                if (selectedIndex != 2) {
                    // Workaround to display the regular icon when the button is not selected
                    SBIcon(inactive = true, icon = R.drawable.ic_sync)
                    Spacer(modifier = Modifier.width(8.dp))
                }
                Text(
                    text = stringResource(R.string.transaction_type_transfer),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
fun SegmentedButtonTheme(
    modifier: Modifier,
    isDarkTheme: Boolean,  // Receives current theme status
    followSystem: Boolean,
    onThemeChange: (Boolean) -> Unit,
    onSystemChange: (Boolean) -> Unit,
    context: Context,
//    coroutineScope: CoroutineScope
) {
    val currentTheme = when (followSystem to isDarkTheme) {
        (false to false) -> 0  // Light Mode
        (false to true) -> 2   // Dark Mode
        else -> 1  // Follow System Mode
    }
    println("currentTheme: $currentTheme")
    var selectedIndex by remember {
        mutableIntStateOf(currentTheme)
    }
//    var selectedTheme by remember { mutableStateOf(isDarkTheme) }

    SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
        // Light Mode
        SegmentedButton(
            selected = selectedIndex == 0,
            onClick = {
                selectedIndex = 0
                onThemeChange(false)
                onSystemChange(false)
            },
            shape = SegmentedButtonDefaults.itemShape(
                index = 0,
                count = 3
            ),
            modifier = modifier,
        ) {
            Row {
                if (selectedIndex != 0) {
                    // Workaround to display the regular icon when the button is not selected
                    SBIcon(inactive = true, icon = R.drawable.ic_light_mode)
                    Spacer(modifier = Modifier.width(8.dp))
                }
                Text(text = stringResource(R.string.theme_light), maxLines = 1)
            }
        }

        SegmentedButton(
            selected = selectedIndex == 1,
            onClick = {
                selectedIndex = 1
                onThemeChange(context.getSystemDefaultTheme())
                onSystemChange(true)
            },
            shape = SegmentedButtonDefaults.itemShape(
                index = 1,
                count = 3
            ),
            modifier = modifier,
        ) {
            Row {
                if (selectedIndex != 1) {
                    // Workaround to display the regular icon when the button is not selected
                    SBIcon(inactive = true, icon = R.drawable.ic_smartphone)
                    Spacer(modifier = Modifier.width(8.dp))
                }
                Text(text = stringResource(R.string.theme_system), maxLines = 1)
            }
        }

        SegmentedButton(
            selected = selectedIndex == 2,
            onClick = {
                selectedIndex = 2
                onThemeChange(true)
                onSystemChange(false)
            },
            shape = SegmentedButtonDefaults.itemShape(
                index = 2,
                count = 3
            ),
            modifier = modifier,
        ) {
            Row {
                if (selectedIndex != 2) {
                    // Workaround to display the regular icon when the button is not selected
                    SBIcon(inactive = true, icon = R.drawable.ic_dark_mode)
                    Spacer(modifier = Modifier.width(8.dp))
                }
                Text(text = stringResource(R.string.theme_dark), maxLines = 1)
            }
        }
    }
}

@Composable
fun SBIcon(
    inactive: Boolean,
    icon: Int,
    inactiveContent: @Composable () -> Unit = { InactiveIcon(ImageVector.vectorResource(icon)) },
    activeContent: (@Composable () -> Unit)? = null
) {
    if (activeContent == null) {
        AnimatedVisibility(
            visible = inactive,
            exit = ExitTransition.None,
            enter = fadeIn(tween(350.0.toInt())) + scaleIn(
                initialScale = 0f,
                transformOrigin = TransformOrigin(0f, 1f),
                animationSpec = tween(350.0.toInt()),
            ),
        ) {
            inactiveContent()
        }
    } else {
        Crossfade(targetState = inactive, label = "Segmented Button Icon") {
            if (it) inactiveContent() else activeContent()
        }
    }
}

/** Unchecked Segmented Button Icon */
@Composable
fun InactiveIcon(icon: ImageVector) {
    Icon(
        imageVector = icon,
        contentDescription = null,
        modifier = Modifier
            .size(18.0.dp)
    )
}