@file:OptIn(ExperimentalMaterial3Api::class)

package com.cashflowtracker.miranda.ui.composables

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.cashflowtracker.miranda.ui.theme.MirandaTheme

@SuppressLint("UnrememberedMutableState")
@Composable
fun SegmentedButton(modifier: Modifier) {
    MirandaTheme {
        val options = mutableStateListOf("Output", "Input", "Transfer")
        var selectedIndex by remember {
            mutableStateOf(0)
        }

        SingleChoiceSegmentedButtonRow {
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
                    Text(text = option)
                }
            }
        }
    }
}