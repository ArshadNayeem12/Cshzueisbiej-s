package com.example.ui.screens.panels

import android.graphics.Bitmap
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.CropFree
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.ai.GeminiMathService
import com.example.core.math.CasSolution
import com.example.ui.components.GlassmorphicCard
import com.example.ui.theme.*
import kotlinx.coroutines.launch

@Composable
fun ArScannerPanel(
    onSolutionScanned: (CasSolution) -> Unit,
    modifier: Modifier = Modifier
) {
    val scope = rememberCoroutineScope()
    var isScanning by remember { mutableStateOf(false) }

    val infiniteTransition = rememberInfiniteTransition(label = "laser_scan")
    val laserY by infiniteTransition.animateFloat(
        initialValue = 0.1f,
        targetValue = 0.9f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "laser_y"
    )

    GlassmorphicCard(
        modifier = modifier
            .fillMaxWidth()
            .testTag("ar_scanner_card"),
        shape = RoundedCornerShape(20.dp),
        accentGlowColor = LaserCyan.copy(alpha = 0.35f)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "AR Math Vision Scanner",
                        color = LaserCyan,
                        fontSize = 15.sp,
                        fontFamily = SpaceGroteskFontFamily,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Point viewfinder at printed or handwritten equations",
                        color = BrushedTitanium,
                        fontSize = 11.sp,
                        fontFamily = SpaceGroteskFontFamily
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(LaserCyan.copy(alpha = 0.15f))
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = "AI VISION 4.0",
                        color = LaserCyan,
                        fontSize = 10.sp,
                        fontFamily = JetBrainsMonoFontFamily,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // High-Tech Viewfinder Canvas
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(ObsidianBase)
                    .border(1.dp, GlassBorderHairline, RoundedCornerShape(14.dp)),
                contentAlignment = Alignment.Center
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val w = size.width
                    val h = size.height

                    // Corner brackets
                    val bracketLen = 30f
                    val strokeW = 4f
                    val bracketColor = LaserCyan

                    // Top-Left
                    drawLine(bracketColor, Offset(20f, 20f), Offset(20f + bracketLen, 20f), strokeW)
                    drawLine(bracketColor, Offset(20f, 20f), Offset(20f, 20f + bracketLen), strokeW)
                    // Top-Right
                    drawLine(bracketColor, Offset(w - 20f, 20f), Offset(w - 20f - bracketLen, 20f), strokeW)
                    drawLine(bracketColor, Offset(w - 20f, 20f), Offset(w - 20f, 20f + bracketLen), strokeW)
                    // Bottom-Left
                    drawLine(bracketColor, Offset(20f, h - 20f), Offset(20f + bracketLen, h - 20f), strokeW)
                    drawLine(bracketColor, Offset(20f, h - 20f), Offset(20f, h - 20f - bracketLen), strokeW)
                    // Bottom-Right
                    drawLine(bracketColor, Offset(w - 20f, h - 20f), Offset(w - 20f - bracketLen, h - 20f), strokeW)
                    drawLine(bracketColor, Offset(w - 20f, h - 20f), Offset(w - 20f, h - 20f - bracketLen), strokeW)

                    // Laser Scanning Bar
                    val currentLaserY = h * laserY
                    drawLine(
                        color = LaserCyan,
                        start = Offset(20f, currentLaserY),
                        end = Offset(w - 20f, currentLaserY),
                        strokeWidth = 2.5f
                    )
                    drawLine(
                        color = LaserCyan.copy(alpha = 0.25f),
                        start = Offset(20f, currentLaserY - 4),
                        end = Offset(w - 20f, currentLaserY - 4),
                        strokeWidth = 6f
                    )
                }

                // Sample equation in reticle
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "3x² - 12x + 9 = 0",
                        color = LuxuryGoldLight,
                        fontSize = 20.sp,
                        fontFamily = JetBrainsMonoFontFamily,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "TARGET ACQUIRED",
                        color = EmeraldGreen,
                        fontSize = 10.sp,
                        fontFamily = JetBrainsMonoFontFamily,
                        letterSpacing = 1.sp
                    )
                }
            }

            // Capture & Scan Action Button
            Button(
                onClick = {
                    isScanning = true
                    scope.launch {
                        val dummyBitmap = Bitmap.createBitmap(400, 300, Bitmap.Config.ARGB_8888)
                        val sol = GeminiMathService.scanAndSolveImage(dummyBitmap)
                        isScanning = false
                        onSolutionScanned(sol)
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = LaserCyan,
                    contentColor = OledBlack
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("btn_capture_ar_scan")
            ) {
                if (isScanning) {
                    CircularProgressIndicator(color = OledBlack, modifier = Modifier.size(16.dp), strokeWidth = 2.dp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Optical Character Recognition...", fontFamily = SpaceGroteskFontFamily, fontWeight = FontWeight.Bold)
                } else {
                    Icon(Icons.Default.CameraAlt, null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Capture & Project Solution Overlay", fontFamily = SpaceGroteskFontFamily, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
