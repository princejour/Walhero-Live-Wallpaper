package com.walhero.livewallpaper.ui.theme

import androidx.compose.foundation.isSystemInDarkMode
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF0EA5E9),
    primaryContainer = Color(0xFF001A33),
    secondary = Color(0xFFA78BFA),
    secondaryContainer = Color(0xFF2D1B69),
    tertiary = Color(0xFF06B6D4),
    background = Color(0xFF000D1F),
    surface = Color(0xFF001A33),
    onBackground = Color(0xFFE0F2FE),
    onSurface = Color(0xFFE0F2FE),
    error = Color(0xFFFF6B6B),
)

@Composable
fun WalheroLiveWallpaperTheme(
    darkTheme: Boolean = isSystemInDarkMode(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = WalheroTypography,
        content = content
    )
}
