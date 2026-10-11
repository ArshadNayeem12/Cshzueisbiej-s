package com.example.ui.components

import android.graphics.Bitmap
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Undo
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

data class DrawnStroke(
    val points: List<Offset>,
    val color: Color = LuxuryGold,
    val strokeWidth: Float = 6f
)

@Composable
fun SmartMathCanvas(
    onSolveCanvas: (Bitmap?) -> Unit,
    modifier: Modifier = Modifier
) {
    val strokes = remember { mutableStateListOf<DrawnStroke>() }
    var currentPoints by remember { mutableStateOf<List<Offset>>(emptyList()) }

    GlassmorphicCard(
        modifier = modifier
            .fillMaxWidth()
            .testTag("smart_math_canvas_card"),
        shape = RoundedCornerShape(24.dp),
        accentGlowColor = LuxuryGold.copy(alpha = 0.3f)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(14.dp)
        ) {
            // Header Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Smart Math Canvas",
                        color = LuxuryGold,
                        fontSize = 16.sp,
                        fontFamily = SpaceGroteskFontFamily,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Write or sketch formulas with stylus/touch",
                        color = BrushedTitanium,
                        fontSize = 11.sp,
                        fontFamily = SpaceGroteskFontFamily
                    )
                }

                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    IconButton(
                        onClick = {
                            if (strokes.isNotEmpty()) strokes.removeAt(strokes.lastIndex)
                        },
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(Icons.AutoMirrored.Filled.Undo, "Undo", tint = TitaniumLight, modifier = Modifier.size(18.dp))
                    }
                    IconButton(
                        onClick = {
                            strokes.clear()
                            currentPoints = emptyList()
                        },
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(Icons.Default.Clear, "Clear Canvas", tint = CrimsonRed, modifier = Modifier.size(18.dp))
                    }
                    Button(
                        onClick = {
                            // Synthesize bitmap for OCR or solve
                            val bitmap = Bitmap.createBitmap(800, 500, Bitmap.Config.ARGB_8888)
                            onSolveCanvas(bitmap)
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = LuxuryGold,
                            contentColor = OledBlack
                        ),
                        shape = RoundedCornerShape(12.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                        modifier = Modifier.testTag("btn_solve_canvas")
                    ) {
                        Icon(Icons.Default.AutoAwesome, null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Solve",
                            fontSize = 12.sp,
                            fontFamily = SpaceGroteskFontFamily,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Ink Drawing Area
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .clip(RoundedCornerShape(16.dp))
                    .background(ObsidianBase)
                    .pointerInput(Unit) {
                        detectDragGestures(
                            onDragStart = { offset ->
                                currentPoints = listOf(offset)
                            },
                            onDrag = { change, _ ->
                                change.consume()
                                currentPoints = currentPoints + change.position
                            },
                            onDragEnd = {
                                if (currentPoints.isNotEmpty()) {
                                    strokes.add(DrawnStroke(points = currentPoints))
                                    currentPoints = emptyList()
                                }
                            },
                            onDragCancel = {
                                currentPoints = emptyList()
                            }
                        )
                    }
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    // Draw subtle grid dots
                    val dotStep = 30f
                    for (x in 0 until size.width.toInt() step dotStep.toInt()) {
                        for (y in 0 until size.height.toInt() step dotStep.toInt()) {
                            drawCircle(
                                color = Color(0x10FFFFFF),
                                radius = 1f,
                                center = Offset(x.toFloat(), y.toFloat())
                            )
                        }
                    }

                    // Render existing strokes
                    for (stroke in strokes) {
                        if (stroke.points.size > 1) {
                            val path = Path()
                            path.moveTo(stroke.points.first().x, stroke.points.first().y)
                            for (p in stroke.points.drop(1)) {
                                path.lineTo(p.x, p.y)
                            }
                            drawPath(
                                path = path,
                                color = stroke.color,
                                style = Stroke(
                                    width = stroke.strokeWidth,
                                    cap = StrokeCap.Round,
                                    join = StrokeJoin.Round
                                )
                            )
                        }
                    }

                    // Render currently active stroke
                    if (currentPoints.size > 1) {
                        val path = Path()
                        path.moveTo(currentPoints.first().x, currentPoints.first().y)
                        for (p in currentPoints.drop(1)) {
                            path.lineTo(p.x, p.y)
                        }
                        drawPath(
                            path = path,
                            color = LuxuryGoldLight,
                            style = Stroke(
                                width = 6f,
                                cap = StrokeCap.Round,
                                join = StrokeJoin.Round
                            )
                        )
                    }
                }

                if (strokes.isEmpty() && currentPoints.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "✍ Write formula here (e.g. ∫ x² dx,  2x + 5 = 11)",
                            color = BrushedTitanium.copy(alpha = 0.5f),
                            fontSize = 14.sp,
                            fontFamily = SpaceGroteskFontFamily
                        )
                    }
                }
            }
        }
    }
}
