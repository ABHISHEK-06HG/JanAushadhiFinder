package com.mindmatrix.janaushadhifinder.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// SRD §9.1 Colour Palette
val PrimaryBlue = Color(0xFF1976D2)
val AccentGreen = Color(0xFF43A047)
val AlertRed = Color(0xFFE53935)
val BackgroundGray = Color(0xFFF5F5F5)
val SurfaceWhite = Color(0xFFFFFFFF)
val LightBlue = Color(0xFFE3F2FD)
val LightGreen = Color(0xFFE8F5E9)
val DarkBlue = Color(0xFF0D47A1)
val TextPrimary = Color(0xFF212121)
val TextSecondary = Color(0xFF757575)
val DividerColor = Color(0xFFE0E0E0)

private val LightColorScheme = lightColorScheme(
    primary = PrimaryBlue,
    secondary = AccentGreen,
    error = AlertRed,
    background = BackgroundGray,
    surface = SurfaceWhite,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = TextPrimary,
    onSurface = TextPrimary
)

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF90CAF9),
    secondary = Color(0xFFA5D6A7),
    error = Color(0xFFEF9A9A),
    background = Color(0xFF121212),
    surface = Color(0xFF1E1E1E),
    onPrimary = Color.Black,
    onSecondary = Color.Black,
    onBackground = Color.White,
    onSurface = Color.White
)

@Composable
fun JanAushadhiFinderTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}
