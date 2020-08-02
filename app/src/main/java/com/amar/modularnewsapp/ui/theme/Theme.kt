package com.amar.modularnewsapp.ui.theme

import androidx.compose.Composable
import androidx.ui.foundation.isSystemInDarkTheme
import androidx.ui.graphics.Color
import androidx.ui.material.ColorPalette
import androidx.ui.material.MaterialTheme
import androidx.ui.material.darkColorPalette
import androidx.ui.material.lightColorPalette


// TODO update color theme
val lightThemeColors = lightColorPalette(
    primary = Color(0x00000),
    primaryVariant = Color.Gray,
    onPrimary = Color.Black,
    secondary = Color(0xffffffff),
    onSecondary = Color.White,
    surface = Color(0xffffffff),
    background = Color.LightGray.copy(alpha = 0.2f),
    onBackground = Color.Black,
    onSurface = Color.Black,
    error = Red800,
    onError = Color.White
)

/**
 * Note: Dark Theme support is not yet available, it will come in 2020. This is just an example of
 * using dark colors.
 */
val darkThemeColors = darkColorPalette(
    primary = Color(0xFFFFFF),
    primaryVariant = Color.Gray,
    onPrimary = Color.Black,
    secondary = Color(0xFF121212),
    onSecondary = Color.White,
    surface = Color(0xFF121212),
    background = Color(0xFF121212),
    onBackground = Color.White,
    onSurface = Color.White,
    error = Red200,
    onError = Color.White
)

@Composable
val ColorPalette.searchBar: Color
    get() = if (isLight) Color.White else Color.DarkGray

@Composable
val ColorPalette.textFieldBackground: Color
    get() = if (isLight) Color.White else Color(0xff424242)

@Composable
fun DistillTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable() () -> Unit
) {
    MaterialTheme(
        colors = if (darkTheme) darkThemeColors else lightThemeColors,
        typography = ThemeTypography,
        shapes = Shapes,
        content = content
    )
}