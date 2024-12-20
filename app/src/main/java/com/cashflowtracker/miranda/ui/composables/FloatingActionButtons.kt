package com.cashflowtracker.miranda.ui.composables

import android.content.Intent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.shrinkHorizontally
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
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
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
                val label = stringResource(R.string.recurrence)
                Box { Text(label) }
                Spacer(modifier = Modifier.width(24.dp))
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
                    containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                    contentColor = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .size(40.dp)
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_schedule),
                        contentDescription = label
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
                Box { Text(stringResource(R.string.transaction)) }
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
                    MaterialTheme.colorScheme.secondaryContainer
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
                        stringResource(R.string.add)
                    } else {
                        stringResource(R.string.add_transaction)
                    },
                    tint = if (expanded.value) {
                        MaterialTheme.colorScheme.onPrimary
                    } else {
                        MaterialTheme.colorScheme.onSecondaryContainer
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
fun ExtendedFAB(
    icon: Int,
    label: String,
    activity: Class<*>,
    lazyListState: LazyListState,
    isFabExpanded: MutableState<Boolean>
) {
    val context = LocalContext.current

    var previousIndex by remember { mutableIntStateOf(0) }
    var previousScrollOffset by remember { mutableIntStateOf(0) }
    var accumulatedScroll by remember { mutableIntStateOf(0) }
    val scrollLeeway = 250

    // Update FAB expansion state based on scroll direction
    LaunchedEffect(lazyListState) {
        snapshotFlow { lazyListState.firstVisibleItemIndex to lazyListState.firstVisibleItemScrollOffset }
            .collect { (index, scrollOffset) ->
                val delta = (index - previousIndex) * 1000 + (scrollOffset - previousScrollOffset)
                accumulatedScroll += delta

                if (accumulatedScroll > scrollLeeway) {
                    isFabExpanded.value = false // Scrolling up, hide text
                    accumulatedScroll = 0
                } else if (accumulatedScroll < -scrollLeeway) {
                    isFabExpanded.value = true // Scrolling down, show text
                    accumulatedScroll = 0
                }

                previousIndex = index
                previousScrollOffset = scrollOffset
            }
    }

    FloatingActionButton(
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
        containerColor = MaterialTheme.colorScheme.secondaryContainer,
        modifier = Modifier.height(56.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(icon),
                contentDescription = label,
                tint = MaterialTheme.colorScheme.onSecondaryContainer,
            )
            AnimatedVisibility(
                visible = isFabExpanded.value,
                enter = fadeIn(animationSpec = tween(durationMillis = 300)) + expandHorizontally(
                    animationSpec = tween(durationMillis = 300)
                ),
                exit = fadeOut(animationSpec = tween(durationMillis = 250)) + shrinkHorizontally(
                    animationSpec = tween(durationMillis = 250)
                )
            ) {
                Text(
                    text = label,
                    modifier = Modifier.padding(start = 12.dp, end = 3.dp)
                )
            }
        }
    }
}