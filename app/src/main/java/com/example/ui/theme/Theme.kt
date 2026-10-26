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

private val SubhxDarkColorScheme = darkColorScheme(
    primary = SubhxPrimary,
    onPrimary = SubhxText,
    primaryContainer = SubhxPrimaryDark,
    onPrimaryContainer = SubhxText,
    secondary = SubhxSecondary,
    onSecondary = SubhxBackground,
    secondaryContainer = SubhxSecondaryDark,
    onSecondaryContainer = SubhxText,
    background = SubhxBackground,
    onBackground = SubhxText,
    surface = SubhxSurface,
    onSurface = SubhxText,
    surfaceVariant = SubhxSurfaceVariant,
    onSurfaceVariant = SubhxTextMuted,
    outline = SubhxCardBorder,
    error = SubhxDanger,
    onError = SubhxText
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = true, // Default to SUBHXAXM sleek dark branding
    content: @Composable () -> Unit
) {
    val colorScheme = SubhxDarkColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window
            if (window != null) {
                window.statusBarColor = SubhxBackground.toArgb()
                window.navigationBarColor = SubhxBackground.toArgb()
                WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
                WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = false
            }
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
