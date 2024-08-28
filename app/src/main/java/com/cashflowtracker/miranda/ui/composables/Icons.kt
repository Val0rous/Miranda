package com.cashflowtracker.miranda.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.Dp

@Composable
fun IconWithBackground(
    icon: Int, iconSize: Dp, iconColor: Color,
    backgroundSize: Dp, backgroundColor: Color,
    contentDescription: String = ""
) {
    Box(
        modifier = Modifier
            .size(backgroundSize)
            .clip(CircleShape)
            .background(backgroundColor)
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(icon),
            contentDescription = contentDescription,
            tint = iconColor,
            modifier = Modifier
                .size(iconSize)
                .align(Alignment.Center)
        )
    }
}