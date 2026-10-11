package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@Composable
fun FxDisplay(
    expression: String,
    result: String,
    livePreview: String?,
    isDegrees: Boolean,
    activeModeName: String,
    onToggleAngleMode: () -> Unit,
    onOpenHistory: () -> Unit,
    onOpenProof: () -> Unit,
    onCopyResult: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    GlassmorphicCard(
        modifier = modifier
            .fillMaxWidth()
            .testTag("fx_display_card"),
        shape = RoundedCornerShape(24.dp),
        accentGlowColor = LuxuryGold.copy(alpha = 0.25f)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            // Timepiece Mechanical Status Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Mode indicator badge
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(LuxuryGold.copy(alpha = 0.15f))
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = activeModeName.uppercase(),
                            color = LuxuryGold,
                            fontSize = 10.sp,
                            fontFamily = JetBrainsMonoFontFamily,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                    }

                    // Angle Mode Switcher (DEG / RAD)
                    Box(
                        modifier = Modifier
                            .testTag("toggle_deg_rad")
                            .clip(RoundedCornerShape(6.dp))
                            .background(ObsidianElevated)
                            .clickable(onClick = onToggleAngleMode)
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = if (isDegrees) "DEG" else "RAD",
                            color = LaserCyan,
                            fontSize = 10.sp,
                            fontFamily = JetBrainsMonoFontFamily,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                    }
                }

                // Telemetry & Quick Action Icons
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    IconButton(
                        onClick = onOpenProof,
                        modifier = Modifier
                            .size(32.dp)
                            .testTag("btn_open_proof")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Psychology,
                            contentDescription = "Step-by-step CAS proof",
                            tint = LuxuryGold,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    IconButton(
                        onClick = onOpenHistory,
                        modifier = Modifier
                            .size(32.dp)
                            .testTag("btn_open_history")
                    ) {
                        Icon(
                            imageVector = Icons.Default.History,
                            contentDescription = "History tape",
                            tint = BrushedTitanium,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    IconButton(
                        onClick = onCopyResult,
                        modifier = Modifier
                            .size(32.dp)
                            .testTag("btn_copy_result")
                    ) {
                        Icon(
                            imageVector = Icons.Default.ContentCopy,
                            contentDescription = "Copy result",
                            tint = BrushedTitanium,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Upper Expression Stream (Formula)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(scrollState, reverseScrolling = true),
                horizontalArrangement = Arrangement.End
            ) {
                Text(
                    text = if (expression.isEmpty()) "0" else expression,
                    color = if (expression.isEmpty()) BrushedTitanium.copy(alpha = 0.5f) else TitaniumLight,
                    fontSize = 20.sp,
                    fontFamily = JetBrainsMonoFontFamily,
                    fontWeight = FontWeight.Normal,
                    textAlign = TextAlign.End
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Live Evaluation Preview (if available)
            AnimatedVisibility(
                visible = livePreview != null && livePreview != result,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Text(
                    text = "≈ $livePreview",
                    color = LuxuryGold.copy(alpha = 0.85f),
                    fontSize = 14.sp,
                    fontFamily = JetBrainsMonoFontFamily,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.End
                )
            }

            // Primary Readout (Final Evaluated Result)
            Text(
                text = result,
                color = if (result.startsWith("Error")) CrimsonRed else TitaniumLight,
                fontSize = if (result.length > 10) 34.sp else 42.sp,
                fontFamily = JetBrainsMonoFontFamily,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                textAlign = TextAlign.End,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("fx_primary_result_text")
            )
        }
    }
}
