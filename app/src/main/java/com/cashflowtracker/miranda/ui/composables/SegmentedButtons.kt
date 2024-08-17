@file:OptIn(ExperimentalMaterial3Api::class)

package com.cashflowtracker.miranda.ui.composables

import android.annotation.SuppressLint
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Download
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.icons.outlined.Sync
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SegmentedButtonDefaults.ActiveIcon
import androidx.compose.material3.SegmentedButtonDefaults.IconSize
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.cashflowtracker.miranda.R
import com.cashflowtracker.miranda.ui.theme.MirandaTheme

@SuppressLint("UnrememberedMutableState")
@Composable
fun SegmentedButtons(modifier: Modifier) {
    MirandaTheme {
        val options = mutableStateListOf("Output", "Input", "Transfer")
        var selectedIndex by remember {
            mutableStateOf(0)
        }

        SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
            options.forEachIndexed { index, option ->
                SegmentedButton(
                    selected = selectedIndex == index,
                    onClick = { selectedIndex = index },
                    shape = SegmentedButtonDefaults.itemShape(
                        index = index,
                        count = options.size
                    ),
                    modifier = modifier,
                ) {
                    Row() {
                        if (selectedIndex != index) {
                            // Workaround to display the regular icon when the button is not selected
                            Icon(
                                imageVector = Icons.Outlined.Download,
                                contentDescription = null,
                                modifier = Modifier
                                    .size(18.0.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                        }
                        Text(text = option)
                    }
                }
            }
        }
    }
}

@Composable
fun SegmentedButtonType(modifier: Modifier) {
    var selectedIndex by remember {
        mutableStateOf(0)
    }

    SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
        SegmentedButton(
            selected = selectedIndex == 0,
            onClick = { selectedIndex = 0 },
            shape = SegmentedButtonDefaults.itemShape(
                index = 0,
                count = 3
            ),
            modifier = modifier,
        ) {
            Row() {
                if (selectedIndex != 0) {
                    // Workaround to display the regular icon when the button is not selected
                    SBIcon(inactive = true, icon = R.drawable.ic_upload)
                    Spacer(modifier = Modifier.width(8.dp))
                }
                Text(text = "Output")
            }
        }

        SegmentedButton(
            selected = selectedIndex == 1,
            onClick = { selectedIndex = 1 },
            shape = SegmentedButtonDefaults.itemShape(
                index = 1,
                count = 3
            ),
            modifier = modifier,
        ) {
            Row() {
                if (selectedIndex != 1) {
                    // Workaround to display the regular icon when the button is not selected
                    SBIcon(inactive = true, icon = R.drawable.ic_download)
                    Spacer(modifier = Modifier.width(8.dp))
                }
                Text(text = "Input")
            }
        }

        SegmentedButton(
            selected = selectedIndex == 2,
            onClick = { selectedIndex = 2 },
            shape = SegmentedButtonDefaults.itemShape(
                index = 2,
                count = 3
            ),
            modifier = modifier,
        ) {
            Row() {
                if (selectedIndex != 2) {
                    // Workaround to display the regular icon when the button is not selected
                    SBIcon(inactive = true, icon = R.drawable.ic_sync)
                    Spacer(modifier = Modifier.width(8.dp))
                }
                Text(text = "Transfer")
            }
        }
    }
}

@Composable
fun SegmentedButtonTheme(modifier: Modifier) {
    var selectedIndex by remember {
        mutableStateOf(0)
    }

    SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
        SegmentedButton(
            selected = selectedIndex == 0,
            onClick = { selectedIndex = 0 },
            shape = SegmentedButtonDefaults.itemShape(
                index = 0,
                count = 2
            ),
            modifier = modifier,
        ) {
            Row() {
                if (selectedIndex != 0) {
                    // Workaround to display the regular icon when the button is not selected
                    SBIcon(inactive = true, icon = R.drawable.ic_light_mode)
                    Spacer(modifier = Modifier.width(8.dp))
                }
                Text(text = "Light")
            }
        }

        SegmentedButton(
            selected = selectedIndex == 1,
            onClick = { selectedIndex = 1 },
            shape = SegmentedButtonDefaults.itemShape(
                index = 1,
                count = 2
            ),
            modifier = modifier,
        ) {
            Row() {
                if (selectedIndex != 1) {
                    // Workaround to display the regular icon when the button is not selected
                    SBIcon(inactive = true, icon = R.drawable.ic_dark_mode)
                    Spacer(modifier = Modifier.width(8.dp))
                }
                Text(text = "Dark")
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
        Crossfade(targetState = inactive) {
            if (it) inactiveContent() else activeContent()
        }
    }
}

@Composable
fun InactiveIcon(icon: ImageVector) {
    Icon(
        imageVector = icon,
        contentDescription = null,
        modifier = Modifier
            .size(18.0.dp)
    )
}