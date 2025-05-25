package com.example.reservationdemo.ui.custom_property

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.runtime.getValue

@Composable
fun Modifier.customShimmer(): Modifier {
    val infiniteTransition = rememberInfiniteTransition(label = "shimmer")
    val offset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmerOffset"
    )
    return this.background(
        brush = Brush.linearGradient(
            colors = listOf(
                Color(0xFFE0E0E0).copy(alpha = 0.4f), // Light gray, subtle
                Color(0xFFF5F5F5).copy(alpha = 0.8f), // Brighter highlight
                Color(0xFFE0E0E0).copy(alpha = 0.4f)  // Back to light gray
            ),
            start = Offset(-200f, 0f), // Start off-screen for smooth effect
            end = Offset(200f * offset, 200f * offset) // Move diagonally
        )
    )
}