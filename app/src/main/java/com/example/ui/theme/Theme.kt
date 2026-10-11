package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = GoldPrimary,
    onPrimary = Color(0xFF1A130B),
    primaryContainer = GoldContainer,
    onPrimaryContainer = OnGoldContainer,
    secondary = NileTurquoise,
    onSecondary = Color(0xFF002B25),
    secondaryContainer = Color(0xFF0D443C),
    onSecondaryContainer = NileTurquoiseLight,
    tertiary = TerracottaAccent,
    onTertiary = Color.White,
    background = SandBackgroundDark,
    onBackground = SandTextPrimary,
    surface = SandSurfaceDark,
    onSurface = SandTextPrimary,
    surfaceVariant = SandCardDark,
    onSurfaceVariant = SandTextSecondary,
    outline = SandBorder
)

private val LightColorScheme = lightColorScheme(
    primary = GoldDark,
    onPrimary = Color.White,
    primaryContainer = GoldLight,
    onPrimaryContainer = Color(0xFF382C0E),
    secondary = NileTurquoise,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFBFF3EB),
    onSecondaryContainer = Color(0xFF003830),
    tertiary = TerracottaAccent,
    onTertiary = Color.White,
    background = Color(0xFFFAF7F2),
    onBackground = Color(0xFF1E1A16),
    surface = Color(0xFFFFFFFF),
    onSurface = Color(0xFF1E1A16),
    surfaceVariant = Color(0xFFF2ECE1),
    onSurfaceVariant = Color(0xFF5D5446),
    outline = Color(0xFFD6CABD)
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = true, // Default to rich Egyptian dark/gold ambience
    dynamicColor: Boolean = false, // Preserve authentic Egyptian color identity
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
