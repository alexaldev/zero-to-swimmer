package com.alexallafi.app.presentation.designsystem

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Colors from themes.xml and colors.xml
val TealPrimary = Color(0xFF00796B)
val TealPrimaryDark = Color(0xFF004D40)
val TealBackground = Color(0xFFF1F8F7)
val TealAccent = Color(0xFF4DB6AC)
val OnTealPrimary = Color(0xFFFFFFFF)
val Black = Color(0xFF212121)

private val LightColorScheme = lightColorScheme(
    primary = TealPrimary,
    onPrimary = OnTealPrimary,
    primaryContainer = TealPrimaryDark,
    secondary = TealAccent,
    onSecondary = OnTealPrimary,
    background = TealBackground,
    surface = Color.White,
    onSurface = Black,
    onBackground = Black,
)

@Composable
fun ZeroToSwimmerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    // Currently only supporting light theme as per themes.xml
    val colorScheme = LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}
