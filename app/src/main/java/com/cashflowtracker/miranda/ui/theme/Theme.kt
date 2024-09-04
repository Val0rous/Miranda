package com.cashflowtracker.miranda.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.media3.effect.Brightness
import com.cashflowtracker.miranda.data.repositories.ThemeRepository.getSystemPreference
import com.cashflowtracker.miranda.data.repositories.ThemeRepository.getThemePreference
import com.cashflowtracker.miranda.ui.viewmodels.ThemeViewModel
import org.koin.androidx.compose.koinViewModel

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40

    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

data class CustomColorScheme(
    val surfaceTintRed: Color,
    val surfaceTintYellow: Color,
    val surfaceTintGreen: Color,
    val surfaceTintBlue: Color,
    val icon: Color,
    val chartAreaBlue: Color,
    val chartLineBlue: Color,
    val chartAreaRed: Color,
    val chartLineRed: Color,
    val chartAreaGreen: Color,
    val chartLineGreen: Color,
    val chartAreaYellow: Color,
    val chartLineYellow: Color,
)

private val DarkCustomColors = CustomColorScheme(
    surfaceTintRed = Dark_SurfaceTintRed,
    surfaceTintYellow = Dark_SurfaceTintYellow,
    surfaceTintGreen = Dark_SurfaceTintGreen,
    surfaceTintBlue = Dark_SurfaceTintBlue,
    icon = Dark_Icon,
    chartAreaBlue = ChartArea_Blue,
    chartLineBlue = Dark_ChartLine_Blue,
    chartAreaRed = ChartArea_Red,
    chartLineRed = Dark_ChartLine_Red,
    chartAreaGreen = ChartArea_Green,
    chartLineGreen = Dark_ChartLine_Green,
    chartAreaYellow = ChartArea_Yellow,
    chartLineYellow = Dark_ChartLine_Yellow,
)

private val LightCustomColors = CustomColorScheme(
    surfaceTintRed = Light_PrimaryFixedRed,
    surfaceTintYellow = Light_PrimaryFixedYellow,
    surfaceTintGreen = Light_PrimaryFixedGreen,
    surfaceTintBlue = Light_PrimaryFixedBlue,
    icon = Light_Icon,
    chartAreaBlue = ChartArea_Blue,
    chartLineBlue = Light_ChartLine_Blue,
    chartAreaRed = ChartArea_Red,
    chartLineRed = Light_ChartLine_Red,
    chartAreaGreen = ChartArea_Green,
    chartLineGreen = Light_ChartLine_Green,
    chartAreaYellow = ChartArea_Yellow,
    chartLineYellow = Light_ChartLine_Yellow,
)

internal val CustomColors = staticCompositionLocalOf<CustomColorScheme> {
    error("No custom colors provided")
}

@Composable
fun MirandaTheme(
    themeViewModel: ThemeViewModel = koinViewModel<ThemeViewModel>(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit,
) {
    val isDarkTheme by themeViewModel.isDarkTheme.collectAsState()
    val followSystem by themeViewModel.followSystem.collectAsState()

    val effectiveIsDarkTheme = if (followSystem) {
        isSystemInDarkTheme()
    } else {
        isDarkTheme
    }
    val colorScheme = when {
        dynamicColor && (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) -> {
            val context = LocalContext.current
            if (effectiveIsDarkTheme) {
                dynamicDarkColorScheme(context)
            } else {
                dynamicLightColorScheme(context)
            }
        }

        effectiveIsDarkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    val customColors = remember(effectiveIsDarkTheme) {
        if (effectiveIsDarkTheme) {
            DarkCustomColors
        } else {
            LightCustomColors
        }
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.surface.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars =
                !effectiveIsDarkTheme

        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        //content = content,
        content = {
            CompositionLocalProvider(
                CustomColors provides customColors,
            ) {
                content()
            }
        }
    )
}
