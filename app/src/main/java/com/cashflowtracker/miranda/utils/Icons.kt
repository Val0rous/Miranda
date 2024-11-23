package com.cashflowtracker.miranda.utils

import android.graphics.Bitmap
import android.graphics.Paint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.ImageBitmapConfig
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.drawscope.CanvasDrawScope
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.cashflowtracker.miranda.R
import com.cashflowtracker.miranda.ui.theme.Green400
import com.cashflowtracker.miranda.ui.theme.LocalCustomColors
import com.cashflowtracker.miranda.ui.theme.Red400
import com.cashflowtracker.miranda.ui.theme.Yellow400
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import kotlin.math.min
import kotlin.math.roundToInt

/** Gets single icon for transaction
 * @param type - transaction type
 * @param source - transaction source
 * @param destination - transaction destination
 * @return - icon as Int
 */
fun iconFactory(type: String, source: String, destination: String): Int {
    return when (type) {
        TransactionType.OUTPUT.name -> {
            DefaultCategories.getIcon(destination)
        }

        TransactionType.INPUT.name -> {
            when (source) {
                SpecialType.POCKET.name,
                SpecialType.EXTRA.name -> SpecialType.getIcon(source)

                else -> DefaultCategories.getIcon(source)
            }
        }

        TransactionType.TRANSFER.name -> {
            R.drawable.ic_sync
        }

        else -> {
            R.drawable.ic_default_empty
        }
    }
}

@Composable
fun backgroundColorFactory(type: String): Color {
    return when (type) {
        TransactionType.OUTPUT.name -> LocalCustomColors.current.surfaceTintRed
        TransactionType.INPUT.name -> LocalCustomColors.current.surfaceTintGreen
        else -> LocalCustomColors.current.surfaceTintBlue
    }
}

@Composable
fun textColorFactory(type: String): Color {
    return when (type) {
        TransactionType.OUTPUT.name -> LocalCustomColors.current.textRed
        TransactionType.INPUT.name -> LocalCustomColors.current.textGreen
        else -> LocalCustomColors.current.textBlue
    }
}

@Composable
fun GetStarIcons(category: String) {
    Row(modifier = Modifier.padding(top = 4.dp)) {
        when (DefaultCategories.getType(category)) {
            CategoryClass.NECESSITY -> repeat(1) {
                Icon(
                    imageVector = ImageVector.vectorResource(
                        R.drawable.ic_star_filled
                    ),
                    contentDescription = "",
                    tint = Red400,
                    modifier = Modifier
                        .size(24.dp)
                )
            }

            CategoryClass.CONVENIENCE -> repeat(2) {
                Icon(
                    imageVector = ImageVector.vectorResource(
                        R.drawable.ic_star_filled
                    ),
                    contentDescription = "",
                    tint = Yellow400,
                    modifier = Modifier
                        .size(24.dp)
                )
            }

            CategoryClass.LUXURY -> repeat(3) {
                Icon(
                    imageVector = ImageVector.vectorResource(
                        R.drawable.ic_star_filled
                    ),
                    contentDescription = "",
                    tint = Green400,
                    modifier = Modifier
                        .size(24.dp)
                )
            }

            CategoryClass.UNRANKED -> {}
        }
    }
}

@Composable
fun createRoundedMarkerIcon(
    type: String,
    source: String,
    destination: String
): BitmapDescriptor {
    val context = LocalContext.current
    val iconResId = iconFactory(type, source, destination)
    val backgroundColor = backgroundColorFactory(type)
    // Load vector image
    val vector = ImageVector.vectorResource(iconResId)
    val painter = rememberVectorPainter(vector)
    // Convert Painter to Bitmap
    val iconBitmap = painter.toImageBitmap(
        density = LocalDensity.current,
        layoutDirection = LayoutDirection.Ltr,
        size = painter.intrinsicSize,
        color = LocalCustomColors.current.icon
    ).asAndroidBitmap()
//    val bitmap = Bitmap.createBitmap(
//        painter.intrinsicSize.width.toInt(),
//        painter.intrinsicSize.height.toInt(),
//        Bitmap.Config.ARGB_8888
//    )

    val padding = 48
    val offset = -8
    // Create a new bitmap to serve as background
    val bitmap = Bitmap.createBitmap(
        iconBitmap.width + padding,
        iconBitmap.height + padding,
        Bitmap.Config.ARGB_8888
    )
    // Draw shadow first
    val canvas = android.graphics.Canvas(bitmap)
    val shadowColor = LocalCustomColors.current.icon
    val shadowRadius = 2f
    val shadowPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = shadowColor.toArgb()
        setShadowLayer(shadowRadius, 0f, 0f, shadowColor.toArgb())
    }
//    paint.color = backgroundColor.toArgb()
    val centerX = bitmap.width / 2f
    val centerY = bitmap.height / 2f
    val radius = min(centerX, centerY)
    // Draw shadow circle
    canvas.drawCircle(centerX, centerY, radius + shadowRadius + offset / 2, shadowPaint)
    // Draw background circle
    val backgroundPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = backgroundColor.toArgb()
    }
    canvas.drawCircle(centerX, centerY, radius + offset, backgroundPaint)
    // Draw the border circle
    val borderColor = Color.White
    val borderPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = borderColor.toArgb()
        style = Paint.Style.STROKE
        strokeWidth = 8f
    }
    canvas.drawCircle(centerX, centerY, radius + offset, borderPaint)
    // Draw icon on top of circles
    canvas.drawBitmap(
        iconBitmap,
        centerX - iconBitmap.width / 2f,
        centerY - iconBitmap.height / 2f,
        null
    )

    return BitmapDescriptorFactory.fromBitmap(bitmap)
}

fun Painter.toImageBitmap(
    density: Density,
    layoutDirection: LayoutDirection,
    size: Size = intrinsicSize,
    config: ImageBitmapConfig = ImageBitmapConfig.Argb8888,
    color: Color
): ImageBitmap {
    val imageBitmap = ImageBitmap(
        width = size.width.roundToInt(),
        height = size.height.roundToInt(),
        config = config
    )
    val canvas = Canvas(imageBitmap)
    CanvasDrawScope().draw(
        density = density,
        layoutDirection = layoutDirection,
        canvas = canvas,
        size = size
    ) {
        draw(
            size = this.size,
            colorFilter = ColorFilter.tint(color)
        )
    }
    return imageBitmap
}

@Composable
fun StarWithBorder(
    modifier: Modifier = Modifier,
    starLineColor: Color,
    starAreaColor: Color,
    starSize: Dp,
) {

    Box(modifier = modifier.size(starSize)) {
        Icon(
            painter = painterResource(R.drawable.ic_star_filled_300),
            contentDescription = "",
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.Center),
            tint = starAreaColor
        )
        Icon(
            painter = painterResource(R.drawable.ic_star_300),
            contentDescription = "",
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.Center),
            tint = starLineColor
        )
    }
}