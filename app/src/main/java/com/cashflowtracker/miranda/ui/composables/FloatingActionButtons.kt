package com.cashflowtracker.miranda.ui.composables

import android.content.Intent
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
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityOptionsCompat
import com.cashflowtracker.miranda.R
import com.cashflowtracker.miranda.ui.screens.AddRecurrence
import com.cashflowtracker.miranda.ui.screens.AddTransaction

@Composable
fun ExpandableFAB(expanded: MutableState<Boolean>) {
    //var expanded by remember { mutableStateOf(false) }
    val context = LocalContext.current
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
                modifier = Modifier.clickable {
                    expanded.value = false
                    val options = ActivityOptionsCompat.makeCustomAnimation(
                        context,
                        R.anim.slide_up_from_bottom,
                        R.anim.fade_out
                    )
                    context.startActivity(
                        Intent(context, AddRecurrence::class.java),
                        options.toBundle()
                    )
                }
            ) {
//                Spacer(modifier = Modifier.weight(1f))
                Box { Text("Recurrence") }
                Spacer(modifier = Modifier.width(28.dp))
                FloatingActionButton(
                    onClick = {
                        expanded.value = false
                        val options = ActivityOptionsCompat.makeCustomAnimation(
                            context,
                            R.anim.slide_up_from_bottom,
                            R.anim.fade_out
                        )
                        context.startActivity(
                            Intent(context, AddRecurrence::class.java),
                            options.toBundle()
                        )
                    },
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
            modifier = Modifier.clickable {
                expanded.value = false
                val options = ActivityOptionsCompat.makeCustomAnimation(
                    context,
                    R.anim.slide_up_from_bottom,
                    R.anim.fade_out
                )
                context.startActivity(
                    Intent(context, AddTransaction::class.java),
                    options.toBundle()
                )
            }
        ) {
            if (expanded.value) {
//                Spacer(modifier = Modifier.weight(1f))
                Box { Text("Transaction") }
                Spacer(modifier = Modifier.width(16.dp))
            }
            FloatingActionButton(
                onClick = {
                    if (!expanded.value) {
                        expanded.value = true
                    } else {
                        expanded.value = false
                        val options = ActivityOptionsCompat.makeCustomAnimation(
                            context,
                            R.anim.slide_up_from_bottom,
                            R.anim.fade_out
                        )
                        context.startActivity(
                            Intent(context, AddTransaction::class.java),
                            options.toBundle()
                        )
                    }
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

/** Creates an Extended Floating Action Button with an icon and label that launches an activity using an Intent
 *  @param icon The icon to be displayed in the FAB
 *  @param label The label to be displayed in the FAB
 *  @param activity The activity to be launched when the FAB is clicked
 */
@Composable
fun ExtendedFAB(icon: Int, label: String, activity: Class<*>) {
    val context = LocalContext.current
    ExtendedFloatingActionButton(
        onClick = {
            val options = ActivityOptionsCompat.makeCustomAnimation(
                context,
                R.anim.slide_up_from_bottom,
                R.anim.fade_out
            )
            context.startActivity(
                Intent(context, activity), options.toBundle()
            )
        },
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        modifier = Modifier.height(56.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = ImageVector.vectorResource(icon),
                contentDescription = label,
                tint = MaterialTheme.colorScheme.onPrimaryContainer,
            )
            Spacer(modifier = Modifier.size(12.dp))
            Text(label)
        }
    }
}