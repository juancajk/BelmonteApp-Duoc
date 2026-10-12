package com.example.myapplication.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

/**
 * Tema corporativo Belmonte SpA.
 * Responsabilidad de Constanza: Diseno base con Material Design 3.
 * - Scaffold + TopAppBar + FAB + BottomNavigation
 * - Paleta azul marino y dorado corporativo
 * - Soporte dark/light mode
 */

private val BelmonteLightColorScheme = lightColorScheme(
    primary = AzulMarinoPrimary,
    onPrimary = Color.White,
    primaryContainer = AzulMarinoVariant,
    onPrimaryContainer = Color.White,
    secondary = DoradoPrimary,
    onSecondary = TextoSobreDorado,
    secondaryContainer = DoradoLight,
    onSecondaryContainer = TextoPrimario,
    tertiary = DoradoDark,
    onTertiary = Color.White,
    background = BackgroundLight,
    onBackground = TextoPrimario,
    surface = SurfaceLight,
    onSurface = TextoPrimario,
    surfaceVariant = Color(0xFFE7E0EC),
    onSurfaceVariant = TextoSecundario,
    error = Color(0xFFB3261E),
    onError = Color.White
)

private val BelmonteDarkColorScheme = darkColorScheme(
    primary = DoradoPrimary,
    onPrimary = AzulMarinoPrimary,
    primaryContainer = AzulMarinoLight,
    onPrimaryContainer = DoradoLight,
    secondary = DoradoLight,
    onSecondary = AzulMarinoPrimary,
    secondaryContainer = AzulMarinoVariant,
    onSecondaryContainer = DoradoLight,
    tertiary = DoradoLight,
    onTertiary = AzulMarinoPrimary,
    background = BackgroundDark,
    onBackground = Color.White,
    surface = SurfaceDark,
    onSurface = Color.White,
    surfaceVariant = AzulMarinoLight,
    onSurfaceVariant = Color(0xFFCAC4D0),
    error = Color(0xFFF2B8B5),
    onError = Color(0xFF601410)
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) BelmonteDarkColorScheme else BelmonteLightColorScheme

    // Ajustar barra de estado al azul marino corporativo
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = AzulMarinoPrimary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
