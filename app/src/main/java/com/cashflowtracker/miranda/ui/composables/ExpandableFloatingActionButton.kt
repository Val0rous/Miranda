package com.cashflowtracker.miranda.ui.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.cashflowtracker.miranda.R

@Composable
fun ExpandableFloatingActionButton(expanded: MutableState<Boolean>) {
    //var expanded by remember { mutableStateOf(false) }
    Column(
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        AnimatedVisibility(
            visible = expanded.value,
            enter = fadeIn(animationSpec = tween(durationMillis = 300))
                    + slideInVertically(
                animationSpec = tween(durationMillis = 200), initialOffsetY = { it })
                    + expandVertically(animationSpec = tween(durationMillis = 200)),
            exit = fadeOut(animationSpec = tween(durationMillis = 300))
                    + slideOutVertically(
                animationSpec = tween(durationMillis = 200), targetOffsetY = { it })
                    + shrinkVertically(animationSpec = tween(durationMillis = 200)),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End,
                modifier = Modifier.clickable { }
            ) {
//                Spacer(modifier = Modifier.weight(1f))
                Box { Text("Recurrence") }
                Spacer(modifier = Modifier.width(28.dp))
                FloatingActionButton(
                    onClick = { /*TODO*/ expanded.value = false },
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .size(40.dp)
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_schedule),
                        contentDescription = "Recurrence"
                    )
                }
            }
            //Spacer(modifier = Modifier.height(8.dp))
        }
        val transition =
            updateTransition(targetState = expanded.value, label = "ExpandableFabTransition")
        val rotation by transition.animateFloat(label = "ExpandableFabRotation") {
            if (it) 0f else -90f
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End,
            modifier = Modifier.clickable { }
        ) {
            if (expanded.value) {
//                Spacer(modifier = Modifier.weight(1f))
                Box { Text("Transaction") }
                Spacer(modifier = Modifier.width(16.dp))
            }
            FloatingActionButton(
                onClick = {
                    expanded.value = !expanded.value
                },
                containerColor = if (expanded.value) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.primaryContainer
                },
                modifier = Modifier.size(56.dp)
            ) {

                Icon(
                    imageVector =
                    if (!expanded.value) {
                        ImageVector.vectorResource(id = R.drawable.ic_add)
                    } else {
                        ImageVector.vectorResource(id = R.drawable.ic_assignment_filled)
                    },
                    contentDescription = if (!expanded.value) {
                        "Add"
                    } else {
                        "Add Assignment"
                    },
                    tint = if (expanded.value) {
                        MaterialTheme.colorScheme.onPrimary
                    } else {
                        MaterialTheme.colorScheme.onPrimaryContainer
                    },
                    modifier = Modifier.rotate(rotation)
                )
            }
        }
    }
}