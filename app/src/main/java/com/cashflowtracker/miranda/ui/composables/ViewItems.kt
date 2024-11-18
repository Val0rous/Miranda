package com.cashflowtracker.miranda.ui.composables

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.cashflowtracker.miranda.R
import com.cashflowtracker.miranda.data.database.Notification
import com.cashflowtracker.miranda.utils.Notifications
import com.cashflowtracker.miranda.utils.Repeats

@Composable
fun RepeatsPillCard(repeat: Repeats) {
    PillCard(
        icon = R.drawable.ic_replay,
        description = "Repeat",
        text = repeat.label,
        modifier = Modifier
            .padding(end = 16.dp)
            .scale(
                scaleX = -1f,
                scaleY = 1f
            )   // Flip horizontally
            .rotate(-45f)
    )
}

@Composable
fun NotificationsPillCard(notifications: List<Notification>) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow
        ),
        modifier = Modifier
            .fillMaxWidth()
//            .height(48.dp)
            .heightIn(min = 48.dp, max = (5 * 48 + 4 * 16).dp)
            .padding(horizontal = 16.dp)
            .clip(RoundedCornerShape(24.dp))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.Top
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_notifications),
                contentDescription = "Notifications",
                modifier = Modifier.padding(end = 16.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            LazyColumn() {
                items(notifications) {
                    Text(Notifications.valueOf(it.notificationType).label)
                    if (it != notifications.last()) {
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
        }
    }
    Spacer(modifier = Modifier.height(16.dp))
}

@Composable
fun CreationPillCard(creationDate: String) {
    PillCard(
        icon = R.drawable.ic_history,
        description = "Creation date",
        text = "Since $creationDate"
    )
}

@Composable
fun IsRecurrencePillCard() {
    PillCard(
        icon = R.drawable.ic_schedule,
        description = "",
        text = "Created with recurrence"
    )
}

@Composable
fun PillCard(
    icon: Int,
    description: String,
    text: String,
    modifier: Modifier = Modifier.padding(end = 16.dp)
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow
        ),
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .padding(horizontal = 16.dp)
            .clip(RoundedCornerShape(24.dp))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(icon),
                contentDescription = description,
                modifier = modifier
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(text)
        }
    }
    Spacer(modifier = Modifier.height(16.dp))
}