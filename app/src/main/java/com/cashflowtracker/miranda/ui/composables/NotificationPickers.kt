package com.cashflowtracker.miranda.ui.composables

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.cashflowtracker.miranda.R
import com.cashflowtracker.miranda.utils.Notifications

@Composable
fun NotificationPickerDialog(
    onDismiss: () -> Unit,
    onNotificationSelected: (Notifications) -> Unit,
    selectedNotifications: SnapshotStateList<Notifications>
) {
    val scrollState = rememberScrollState()
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {},
        dismissButton = {},
        text = {
            Column(modifier = Modifier.verticalScroll(scrollState)) {
                Notifications.entries.filterNot { it in selectedNotifications }.forEach { option ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .selectable(
                                selected = (option in selectedNotifications),
                                onClick = { onNotificationSelected(option) }
                            ),
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = (option in selectedNotifications),
                            onClick = { onNotificationSelected(option) },
                            modifier = Modifier.padding(4.dp),
                            enabled = true,
                            interactionSource = remember { MutableInteractionSource() }
                        )
                        Text(
                            text = option.label,
                            modifier = Modifier.padding(start = 16.dp)
                        )
                    }
                }
            }
        }
    )
}

@Composable
fun NotificationPicker(selectedNotifications: SnapshotStateList<Notifications>) {
    var isNotificationPickerVisible by remember { mutableStateOf(false) }
    if (selectedNotifications.isNotEmpty()) {
        selectedNotifications.forEach { notification ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    notification.label,
                    modifier = Modifier.padding(start = 12.dp),
                    style = MaterialTheme.typography.titleSmall
                )    // TODO: Replace with DropdownMenu listing notification options
                Spacer(modifier = Modifier.weight(1f))
                IconButton(
                    onClick = { selectedNotifications.remove(notification) }
                ) {
                    Icon(
                        ImageVector.vectorResource(R.drawable.ic_close),
                        contentDescription = "Delete Notification"
                    )
                }
            }
        }
    }

    // Max 5 notifications for each recurrence
    if (selectedNotifications.size < 5) {
        TextButton(onClick = { isNotificationPickerVisible = true }) {
            Text("Add notification", color = MaterialTheme.colorScheme.outline)
        }
    }

    if (isNotificationPickerVisible) {
        NotificationPickerDialog(
            onDismiss = { isNotificationPickerVisible = false },
            onNotificationSelected = { notification ->
                selectedNotifications.add(notification)
                isNotificationPickerVisible = false
            },
            selectedNotifications = selectedNotifications
        )

    }
}