package com.smartsplit.app.ui.theme

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary          = Primary,
    onPrimary        = Color.White,
    primaryContainer = PrimaryTint,
    onPrimaryContainer = PrimaryDark,
    background       = Background,
    surface          = Surface,
    onSurface        = TextPrimary,
    onBackground     = TextPrimary,
    surfaceVariant   = Color(0xFFEAEEEE),
    onSurfaceVariant = TextSecondary,
    outline          = Divider,
    error            = ErrorColor
)

private val DarkColorScheme = darkColorScheme(
    primary          = PrimaryLight,
    onPrimary        = Color(0xFF003731),
    primaryContainer = PrimaryDark,
    onPrimaryContainer = PrimaryTint,
    background       = BackgroundDark,
    surface          = SurfaceDark,
    onSurface        = TextPrimaryDark,
    onBackground     = TextPrimaryDark,
    surfaceVariant   = CardDark,
    onSurfaceVariant = TextSecondaryDark,
    outline          = DividerDark,
    error            = Color(0xFFF87171)
)

@Composable
fun SmartSplitTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    MaterialTheme(
        colorScheme = colorScheme,
        typography  = Typography,
        content     = content
    )
}