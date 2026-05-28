package com.expanse.autopilot.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = PurpleNeon,
    secondary = EmeraldNeon,
    tertiary = BlueNeon,
    background = DarkBg,
    surface = GlassSurface,
    onBackground = TextWhite,
    onSurface = TextWhite
)

@Composable
fun ExpanseAutoPilotTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography,
        content = content
    )
}
