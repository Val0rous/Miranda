package com.cashflowtracker.miranda.ui.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.DarkMode
import androidx.compose.material.icons.outlined.LightMode
import androidx.compose.material.icons.outlined.ModeNight
import androidx.compose.material.icons.outlined.WbSunny
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun SegmentedButton(
    modifier: Modifier = Modifier,
    options: List<SegmentedButtonOption>,
    selectedIndex: Int,
    onSelectionChange: (Int) -> Unit
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
    ) {
        options.forEachIndexed { index, option ->
            val isSelected = selectedIndex == index
            val backgroundColor = if (isSelected) Color(0xFFBBDEFB) else Color.White

            Box(
                modifier = Modifier
                    .weight(1f)
                    .background(backgroundColor)
                    .clickable { onSelectionChange(index) }
                    .padding(vertical = 8.dp)
            ) {
                Row(
                    modifier = Modifier.align(Alignment.Center),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    AnimatedVisibility(
                        visible = !isSelected,
                        enter = fadeIn(animationSpec = spring()),
                        exit = fadeOut(animationSpec = spring())
                    ) {
//                        Icon(
//                            painter = painterResource(id = option.icon),
//                            contentDescription = option.label,
//                            tint = Color.Black,
//                            modifier = Modifier.size(24.dp)
//                        )
                        Icon(option.icon, contentDescription = option.label)
                    }
                    AnimatedVisibility(
                        visible = isSelected,
                        enter = fadeIn(animationSpec = spring()),
                        exit = fadeOut(animationSpec = spring())
                    ) {
//                        Icon(
//                            painter = painterResource(id = android.R.drawable.checkbox_on_background),
//                            contentDescription = null,
//                            tint = Color.Black,
//                            modifier = Modifier.size(24.dp)
//                        )
                        Icon(Icons.Outlined.Check, contentDescription = null)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = option.label,
                        color = Color.Black
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SegmentedButtonPreview() {
    var selectedIndex by remember { mutableStateOf(0) }

    val options = listOf(
        SegmentedButtonOption(
            icon = Icons.Outlined.LightMode,
            label = "Light"
        ),
        SegmentedButtonOption(
            icon = Icons.Outlined.DarkMode,
            label = "Dark"
        )
    )

    SegmentedButton(
        options = options,
        selectedIndex = selectedIndex,
        onSelectionChange = { selectedIndex = it }
    )
}

data class SegmentedButtonOption(
    val icon: ImageVector,
    val label: String
)
