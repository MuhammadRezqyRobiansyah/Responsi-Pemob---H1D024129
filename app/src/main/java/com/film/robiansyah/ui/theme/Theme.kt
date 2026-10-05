package com.film.robiansyah.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Skema Tema Neo-Brutalism Dark
private val NeoDarkColorScheme = darkColorScheme(
    primary = NeoLime,
    onPrimary = Color.Black,
    secondary = NeoCyan,
    onSecondary = Color.Black,
    tertiary = NeoCoral,
    onTertiary = Color.White,
    background = NeoBackground,
    surface = NeoSurface,
    surfaceVariant = NeoSurfaceVariant,
    onBackground = NeoTextWhite,
    onSurface = NeoTextWhite,
    onSurfaceVariant = NeoTextMuted,
    outline = NeoBorder
)

// Opsi Terang (Tetap mengutamakan kontras tegas Neo-Brutalism)
private val NeoLightColorScheme = lightColorScheme(
    primary = NeoLime,
    onPrimary = Color.Black,
    secondary = NeoCyan,
    onSecondary = Color.Black,
    tertiary = NeoCoral,
    onTertiary = Color.White,
    background = Color(0xFFF4F4F5),
    surface = Color.White,
    surfaceVariant = Color(0xFFE4E4E7),
    onBackground = Color(0xFF09090B),
    onSurface = Color(0xFF09090B),
    onSurfaceVariant = Color(0xFF52525B),
    outline = Color(0xFF09090B)
)

@Composable
fun CineExploreTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    // Utamakan tema gelap (Neo-Brutalism Dark)
    val colorScheme = if (darkTheme) NeoDarkColorScheme else NeoDarkColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
