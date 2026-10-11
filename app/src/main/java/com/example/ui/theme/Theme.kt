package com.example.ui.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val FxLuxuryColorScheme = darkColorScheme(
    primary = LuxuryGold,
    onPrimary = OledBlack,
    primaryContainer = ObsidianElevated,
    onPrimaryContainer = LuxuryGoldLight,
    secondary = BrushedTitanium,
    onSecondary = OledBlack,
    secondaryContainer = ObsidianBorder,
    onSecondaryContainer = TitaniumLight,
    tertiary = LaserCyan,
    onTertiary = OledBlack,
    background = OledBlack,
    onBackground = TitaniumLight,
    surface = ObsidianBase,
    onSurface = TitaniumLight,
    surfaceVariant = ObsidianElevated,
    onSurfaceVariant = BrushedTitanium,
    outline = ObsidianBorder,
    outlineVariant = GlassBorderHairline,
    error = CrimsonRed,
    onError = TitaniumLight
)

@Composable
fun MyApplicationTheme(
    content: @Composable () -> Unit
) {
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window
            if (window != null) {
                window.statusBarColor = OledBlack.toArgb()
                window.navigationBarColor = OledBlack.toArgb()
                WindowCompat.getInsetsController(window, view).apply {
                    isAppearanceLightStatusBars = false
                    isAppearanceLightNavigationBars = false
                }
            }
        }
    }

    MaterialTheme(
        colorScheme = FxLuxuryColorScheme,
        typography = AppTypography,
        content = content
    )
}
