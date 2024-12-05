package com.cashflowtracker.miranda.ui.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.cashflowtracker.miranda.R
import com.cashflowtracker.miranda.utils.AccountType

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun AccountsFilter(
    isVisible: MutableState<Boolean>,
    selections: Map<String, MutableState<Boolean>>
) {
    ModalBottomSheet(
        onDismissRequest = { isVisible.value = false }
    ) {
        Text(
            text = "Filter Accounts",
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(bottom = 16.dp)
        )

        Text(
            text = "Account type",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(start = 16.dp)
        )
        FlowRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp),
            verticalArrangement = Arrangement.spacedBy((-6).dp)
        ) {
            AccountType.entries.sortedBy { it.type.length }.forEach {
                FilterChip(
                    modifier = Modifier.padding(end = 6.dp),
                    onClick = {
                        selections[it.name]!!.value = !selections[it.name]!!.value
                    },
                    label = { Text(it.type) },
                    selected = selections[it.name]!!.value,
                    leadingIcon = {
                        Icon(
                            imageVector = ImageVector.vectorResource(
                                if (selections[it.name]!!.value) {
                                    R.drawable.ic_check
                                } else {
                                    it.icon
                                }
                            ),
                            contentDescription = if (selections[it.name]!!.value) {
                                "Checked ${it.icon}"
                            } else {
                                "${it.icon}"
                            },
                            modifier = Modifier.size(FilterChipDefaults.IconSize)
                        )
                    }
                )
            }
        }
        HorizontalDivider(modifier = Modifier.padding(top = 8.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(16.dp)
        ) {
            OutlinedButton(
                onClick = {
                    selections.values.forEach {
                        it.value = false
                    }
                    isVisible.value = false
                },
                modifier = Modifier.weight(1f)
            ) {
                Text("Reset")
            }
            Button(
                onClick = {
                    isVisible.value = false
                    /*TODO apply*/
                },
                modifier = Modifier.weight(1f)
            ) {
                Text("Apply")
            }
        }
    }
}