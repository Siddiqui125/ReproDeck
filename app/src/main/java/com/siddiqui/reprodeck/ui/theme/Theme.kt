package com.siddiqui.reprodeck.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val ReproDeckColors = darkColorScheme(
    primary = Mint,
    onPrimary = Ink,
    secondary = MintSoft,
    onSecondary = Ink,
    background = Ink,
    onBackground = TextPrimary,
    surface = Panel,
    onSurface = TextPrimary,
    surfaceVariant = PanelElevated,
    onSurfaceVariant = TextMuted,
    outline = Border,
    error = Rose
)

@Composable
fun ReproDeckTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = ReproDeckColors,
        typography = MaterialTheme.typography,
        content = content
    )
}
