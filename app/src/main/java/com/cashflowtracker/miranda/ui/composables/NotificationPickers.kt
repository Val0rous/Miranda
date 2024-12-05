package com.cashflowtracker.miranda.ui.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.cashflowtracker.miranda.R
import com.cashflowtracker.miranda.utils.Notifications

@Composable
fun NotificationPickerDialog(
    onDismiss: () -> Unit,
    onNotificationSelected: (Notifications) -> Unit,
    selectedNotifications: SnapshotStateList<Notifications>
) {
    val scrollState = rememberScrollState()
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerHigh),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 36.dp)
                .clip(RoundedCornerShape(28.dp))
        ) {
            Column(
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .verticalScroll(scrollState)
            ) {
                Notifications.entries.filterNot { it in selectedNotifications }.forEach { option ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
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
    }
}

@Composable
fun NotificationIcon() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.padding(top = 0.dp)
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.ic_notifications),
            contentDescription = stringResource(R.string.notifications),
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
    Spacer(modifier = Modifier.width(16.dp))
}

@Composable
fun NotificationPicker(
    isNotificationPickerVisible: MutableState<Boolean>,
    showIcon: Boolean = false
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .clickable { isNotificationPickerVisible.value = true }
            .padding(horizontal = 16.dp)
    ) {
        if (showIcon) {
            NotificationIcon()
        } else {
            Spacer(modifier = Modifier.width(40.dp))
        }
        Text(
            text = "Add notification",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Normal
            )
        )
    }
}