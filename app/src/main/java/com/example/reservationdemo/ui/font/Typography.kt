package com.example.reservationdemo.ui.font

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle

val AppTypography = Typography(
    displayLarge = TextStyle(fontFamily = QuicksandFont),
    displayMedium = TextStyle(fontFamily = QuicksandFont),
    displaySmall = TextStyle(fontFamily = QuicksandFont),
    headlineLarge = TextStyle(fontFamily = QuicksandFont),
    headlineMedium = TextStyle(fontFamily = QuicksandFont),
    headlineSmall = TextStyle(fontFamily = QuicksandFont),
    titleLarge = TextStyle(fontFamily = QuicksandFont),
    titleMedium = TextStyle(fontFamily = QuicksandFont),
    titleSmall = TextStyle(fontFamily = QuicksandFont),
    bodyLarge = TextStyle(fontFamily = QuicksandFont),
    bodyMedium = TextStyle(fontFamily = QuicksandFont),
    bodySmall = TextStyle(fontFamily = QuicksandFont),
    labelLarge = TextStyle(fontFamily = QuicksandFont),
    labelMedium = TextStyle(fontFamily = QuicksandFont),
    labelSmall = TextStyle(fontFamily = QuicksandFont),
)
@Composable
fun VCompassTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        typography = AppTypography,
        content = content
    )
}