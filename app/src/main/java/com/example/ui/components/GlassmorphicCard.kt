package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.ui.theme.GlassBorderHairline
import com.example.ui.theme.GlassSurface
import com.example.ui.theme.ObsidianElevated

@Composable
fun GlassmorphicCard(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(20.dp),
    borderWidth: Dp = 1.dp,
    borderColor: Color = GlassBorderHairline,
    accentGlowColor: Color? = null,
    content: @Composable BoxScope.() -> Unit
) {
    val backgroundBrush = Brush.verticalGradient(
        colors = listOf(
            GlassSurface,
            ObsidianElevated.copy(alpha = 0.85f)
        )
    )

    val borderBrush = if (accentGlowColor != null) {
        Brush.linearGradient(
            colors = listOf(
                accentGlowColor.copy(alpha = 0.7f),
                borderColor,
                accentGlowColor.copy(alpha = 0.2f)
            )
        )
    } else {
        Brush.verticalGradient(
            colors = listOf(
                borderColor.copy(alpha = 0.6f),
                borderColor.copy(alpha = 0.2f)
            )
        )
    }

    Box(
        modifier = modifier
            .clip(shape)
            .background(backgroundBrush)
            .border(borderWidth, borderBrush, shape),
        content = content
    )
}
