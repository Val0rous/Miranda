package com.cashflowtracker.miranda.utils

import android.graphics.Bitmap
import android.graphics.Paint
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import com.cashflowtracker.miranda.R
import com.cashflowtracker.miranda.ui.theme.CustomColors
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
        "Output" -> {
            DefaultCategories.getIcon(destination)
        }

        "Input" -> {
            when (source) {
                SpecialType.POCKET.category,
                SpecialType.EXTRA.category -> SpecialType.getIcon(source)

                else -> DefaultCategories.getIcon(source)
            }
        }

        "Transfer" -> {
            R.drawable.ic_sync
        }

        else -> {
            R.drawable.ic_default_empty
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
    val backgroundColor = when (type) {
        "Output" -> CustomColors.current.surfaceTintRed
        "Input" -> CustomColors.current.surfaceTintGreen
        else -> CustomColors.current.surfaceTintBlue
    }
    // Load vector image
    val vector = ImageVector.vectorResource(iconResId)
    val painter = rememberVectorPainter(vector)
    // Convert Painter to Bitmap
    val iconBitmap = painter.toImageBitmap(
        density = LocalDensity.current,
        layoutDirection = LayoutDirection.Ltr,
        size = painter.intrinsicSize,
        color = CustomColors.current.icon
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
    val shadowColor = CustomColors.current.icon
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