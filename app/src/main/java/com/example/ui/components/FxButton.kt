package com.example.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

enum class FxButtonVariant {
    NUMBER,
    OPERATOR,
    FUNCTION,
    ACTION,
    EQUAL
}

@Composable
fun FxButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    variant: FxButtonVariant = FxButtonVariant.NUMBER,
    fontSize: TextUnit = 20.sp,
    testTag: String = "btn_$text"
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.92f else 1.0f,
        animationSpec = spring(dampingRatio = 0.65f, stiffness = 800f),
        label = "button_scale"
    )

    val shape = RoundedCornerShape(16.dp)

    val backgroundBrush = when (variant) {
        FxButtonVariant.EQUAL -> Brush.verticalGradient(
            colors = if (isPressed) {
                listOf(LuxuryGoldLight, LuxuryGold)
            } else {
                listOf(LuxuryGold, LuxuryGoldDark)
            }
        )
        FxButtonVariant.OPERATOR -> Brush.verticalGradient(
            colors = if (isPressed) {
                listOf(GlassSurfaceHover, ObsidianElevated)
            } else {
                listOf(GlassSurface, ObsidianBase.copy(alpha = 0.9f))
            }
        )
        FxButtonVariant.FUNCTION -> Brush.verticalGradient(
            colors = if (isPressed) {
                listOf(Color(0x2E8E9297), ObsidianElevated)
            } else {
                listOf(Color(0x1A8E9297), ObsidianBase)
            }
        )
        FxButtonVariant.ACTION -> Brush.verticalGradient(
            colors = if (isPressed) {
                listOf(Color(0x33FF453A), ObsidianElevated)
            } else {
                listOf(Color(0x1AFF453A), ObsidianBase)
            }
        )
        FxButtonVariant.NUMBER -> Brush.verticalGradient(
            colors = if (isPressed) {
                listOf(GlassSurfaceHover, ObsidianElevated)
            } else {
                listOf(GlassSurface, ObsidianBase)
            }
        )
    }

    val textColor = when (variant) {
        FxButtonVariant.EQUAL -> OledBlack
        FxButtonVariant.OPERATOR -> LuxuryGold
        FxButtonVariant.FUNCTION -> WarmCopper
        FxButtonVariant.ACTION -> CrimsonRed
        FxButtonVariant.NUMBER -> TitaniumLight
    }

    val borderBrush = when (variant) {
        FxButtonVariant.EQUAL -> Brush.verticalGradient(
            listOf(LuxuryGoldLight, LuxuryGoldDark)
        )
        FxButtonVariant.OPERATOR -> Brush.verticalGradient(
            listOf(LuxuryGold.copy(alpha = 0.5f), GlassBorderHairline)
        )
        FxButtonVariant.ACTION -> Brush.verticalGradient(
            listOf(CrimsonRed.copy(alpha = 0.4f), GlassBorderHairline)
        )
        else -> Brush.verticalGradient(
            listOf(GlassBorderHairline, Color(0x10FFFFFF))
        )
    }

    Box(
        modifier = modifier
            .testTag(testTag)
            .scale(scale)
            .defaultMinSize(minWidth = 52.dp, minHeight = 52.dp)
            .clip(shape)
            .background(backgroundBrush)
            .border(1.dp, borderBrush, shape)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            )
            .padding(vertical = 12.dp, horizontal = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = textColor,
            fontSize = fontSize,
            fontFamily = if (variant == FxButtonVariant.NUMBER) JetBrainsMonoFontFamily else SpaceGroteskFontFamily,
            fontWeight = if (variant == FxButtonVariant.EQUAL) FontWeight.Bold else FontWeight.Medium
        )
    }
}
