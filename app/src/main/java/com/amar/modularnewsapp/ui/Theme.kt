package com.amar.modularnewsapp.ui

import androidx.ui.graphics.Color
import androidx.ui.material.darkColorPalette
import androidx.ui.material.lightColorPalette


val lightThemeColors = lightColorPalette(
    primary = Color(0x00000),
    primaryVariant = Color.Gray,
    onPrimary = Color.Black,
    secondary = Color(0xffffffff),
    onSecondary = Color.White,
    surface = Color(0xffffffff),
    background = Color.LightGray,
    onBackground = Color.Black,
    onSurface = Color.Black,
    error = Color(0xFFD00036),
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
    surface = Color(0xFF191919),
    background = Color(0xFF555454),
    onBackground = Color.White,
    onSurface = Color.White
)