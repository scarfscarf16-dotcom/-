package com.example.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val IslamicDarkColorScheme = darkColorScheme(
    primary = IslamicGold,
    onPrimary = ObsidianBlack,
    primaryContainer = IslamicGreenMedium,
    onPrimaryContainer = IslamicGoldLight,
    secondary = IslamicGoldLight,
    onSecondary = ObsidianBlack,
    secondaryContainer = SurfaceCard,
    onSecondaryContainer = TextPrimary,
    tertiary = AccentAmber,
    onTertiary = ObsidianBlack,
    background = ObsidianBlack,
    onBackground = TextPrimary,
    surface = SurfaceDark,
    onSurface = TextPrimary,
    surfaceVariant = SurfaceCard,
    onSurfaceVariant = TextSecondary,
    outline = SurfaceCardBorder
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = IslamicDarkColorScheme

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window
            if (window != null) {
                window.statusBarColor = ObsidianBlack.toArgb()
                window.navigationBarColor = ObsidianBlack.toArgb()
                WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
                WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = false
            }
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = QuranTypography,
        content = content
    )
}

@Composable
fun IslamicQuranStudioTheme(
    darkTheme: Boolean = true,
    content: @Composable () -> Unit
) {
    MyApplicationTheme(darkTheme = darkTheme, content = content)
}

