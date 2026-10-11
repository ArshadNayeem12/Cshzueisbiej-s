package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.math.ExpressionEvaluator
import com.example.ui.theme.*

@Composable
fun GraphCanvas(
    functionExpression: String,
    modifier: Modifier = Modifier
) {
    var scale by remember { mutableFloatStateOf(40f) } // Pixels per unit
    var offsetX by remember { mutableFloatStateOf(0f) }
    var offsetY by remember { mutableFloatStateOf(0f) }
    var probeX by remember { mutableStateOf<Double?>(null) }
    var probeY by remember { mutableStateOf<Double?>(null) }

    GlassmorphicCard(
        modifier = modifier
            .fillMaxWidth()
            .testTag("interactive_graph_card"),
        shape = RoundedCornerShape(24.dp),
        accentGlowColor = LaserCyan.copy(alpha = 0.3f)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp)
        ) {
            // Function Header & Probe Readout
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "f(x) = $functionExpression",
                        color = LaserCyan,
                        fontSize = 16.sp,
                        fontFamily = SpaceGroteskFontFamily,
                        fontWeight = FontWeight.Bold
                    )
                    if (probeX != null && probeY != null) {
                        Text(
                            text = "Root Trace: x = ${ExpressionEvaluator.formatDisplay(probeX!!)},  y = ${ExpressionEvaluator.formatDisplay(probeY!!)}",
                            color = LuxuryGold,
                            fontSize = 12.sp,
                            fontFamily = JetBrainsMonoFontFamily
                        )
                    } else {
                        Text(
                            text = "Drag along curve to trace roots & coordinates",
                            color = BrushedTitanium,
                            fontSize = 11.sp,
                            fontFamily = SpaceGroteskFontFamily
                        )
                    }
                }

                // Zoom & Reset Action Buttons
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    IconButton(
                        onClick = { scale = (scale * 1.25f).coerceAtMost(200f) },
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(Icons.Default.Add, "Zoom In", tint = TitaniumLight, modifier = Modifier.size(16.dp))
                    }
                    IconButton(
                        onClick = { scale = (scale * 0.8f).coerceAtLeast(10f) },
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(Icons.Default.Remove, "Zoom Out", tint = TitaniumLight, modifier = Modifier.size(16.dp))
                    }
                    IconButton(
                        onClick = {
                            scale = 40f
                            offsetX = 0f
                            offsetY = 0f
                            probeX = null
                            probeY = null
                        },
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(Icons.Default.Refresh, "Reset", tint = BrushedTitanium, modifier = Modifier.size(16.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Main Graphing Surface
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .clip(RoundedCornerShape(16.dp))
                    .background(ObsidianBase)
                    .pointerInput(Unit) {
                        detectTransformGestures { _, pan, zoom, _ ->
                            scale = (scale * zoom).coerceIn(10f, 250f)
                            offsetX += pan.x
                            offsetY += pan.y
                        }
                    }
                    .pointerInput(Unit) {
                        detectDragGestures(
                            onDragStart = { offset ->
                                val width = size.width
                                val height = size.height
                                val originX = width / 2f + offsetX
                                val originY = height / 2f + offsetY
                                val mathX = ((offset.x - originX) / scale).toDouble()
                                val mathY = ExpressionEvaluator.evaluate(functionExpression, isDegrees = false, xVal = mathX).getOrNull()
                                probeX = mathX
                                probeY = mathY
                            },
                            onDrag = { change, _ ->
                                change.consume()
                                val width = size.width
                                val height = size.height
                                val originX = width / 2f + offsetX
                                val originY = height / 2f + offsetY
                                val mathX = ((change.position.x - originX) / scale).toDouble()
                                val mathY = ExpressionEvaluator.evaluate(functionExpression, isDegrees = false, xVal = mathX).getOrNull()
                                probeX = mathX
                                probeY = mathY
                            }
                        )
                    }
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val w = size.width
                    val h = size.height
                    val originX = w / 2f + offsetX
                    val originY = h / 2f + offsetY

                    // 1. Draw Grid Lines
                    val gridStep = scale
                    val startX = (originX % gridStep)
                    var curX = startX
                    while (curX < w) {
                        drawLine(
                            color = Color(0x14FFFFFF),
                            start = Offset(curX, 0f),
                            end = Offset(curX, h),
                            strokeWidth = 1f
                        )
                        curX += gridStep
                    }

                    val startY = (originY % gridStep)
                    var curY = startY
                    while (curY < h) {
                        drawLine(
                            color = Color(0x14FFFFFF),
                            start = Offset(0f, curY),
                            end = Offset(w, curY),
                            strokeWidth = 1f
                        )
                        curY += gridStep
                    }

                    // 2. Draw Axes X and Y
                    drawLine(
                        color = Color(0x40FFFFFF),
                        start = Offset(0f, originY),
                        end = Offset(w, originY),
                        strokeWidth = 2f
                    )
                    drawLine(
                        color = Color(0x40FFFFFF),
                        start = Offset(originX, 0f),
                        end = Offset(originX, h),
                        strokeWidth = 2f
                    )

                    // 3. Plot Curve
                    val curvePath = Path()
                    var firstPoint = true
                    val pixelStep = 2 // Sample every 2 pixels for 120Hz smoothness

                    for (px in 0 until w.toInt() step pixelStep) {
                        val mathX = ((px - originX) / scale).toDouble()
                        val res = ExpressionEvaluator.evaluate(functionExpression, isDegrees = false, xVal = mathX)
                        val mathY = res.getOrNull()

                        if (mathY != null && !mathY.isNaN() && !mathY.isInfinite()) {
                            val py = originY - (mathY.toFloat() * scale)
                            if (py in -200f..(h + 200f)) {
                                if (firstPoint) {
                                    curvePath.moveTo(px.toFloat(), py)
                                    firstPoint = false
                                } else {
                                    curvePath.lineTo(px.toFloat(), py)
                                }
                            } else {
                                firstPoint = true
                            }
                        } else {
                            firstPoint = true
                        }
                    }

                    drawPath(
                        path = curvePath,
                        color = LaserCyan,
                        style = Stroke(width = 3.5f)
                    )

                    // 4. Draw Root Trace Probe Indicator
                    if (probeX != null && probeY != null && !probeY!!.isNaN()) {
                        val px = originX + (probeX!!.toFloat() * scale)
                        val py = originY - (probeY!!.toFloat() * scale)
                        drawCircle(
                            color = LuxuryGold,
                            radius = 7f,
                            center = Offset(px, py)
                        )
                        drawCircle(
                            color = Color.White,
                            radius = 3.5f,
                            center = Offset(px, py)
                        )
                    }
                }
            }
        }
    }
}
