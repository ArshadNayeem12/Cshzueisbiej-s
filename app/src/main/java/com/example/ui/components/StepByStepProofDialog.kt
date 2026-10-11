package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.core.math.CasSolution
import com.example.core.math.MathStep
import com.example.ui.theme.*

@Composable
fun StepByStepProofDialog(
    solution: CasSolution,
    onDismiss: () -> Unit,
    onCopyProof: (String) -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.85f))
                .padding(horizontal = 16.dp, vertical = 24.dp),
            contentAlignment = Alignment.Center
        ) {
            GlassmorphicCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.92f)
                    .testTag("step_proof_dialog"),
                shape = RoundedCornerShape(24.dp),
                accentGlowColor = LuxuryGold
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp)
                ) {
                    // Header
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(LuxuryGold.copy(alpha = 0.2f))
                                        .padding(horizontal = 8.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = solution.queryType.uppercase(),
                                        color = LuxuryGold,
                                        fontSize = 10.sp,
                                        fontFamily = JetBrainsMonoFontFamily,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                Text(
                                    text = "Mathematical Proof",
                                    color = TitaniumLight,
                                    fontSize = 18.sp,
                                    fontFamily = SpaceGroteskFontFamily,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = solution.equation,
                                color = BrushedTitanium,
                                fontSize = 13.sp,
                                fontFamily = JetBrainsMonoFontFamily
                            )
                        }

                        IconButton(
                            onClick = onDismiss,
                            modifier = Modifier
                                .size(36.dp)
                                .testTag("btn_close_proof")
                        ) {
                            Icon(Icons.Default.Close, "Close", tint = TitaniumLight)
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Final Result Banner
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(
                                Brush.horizontalGradient(
                                    listOf(
                                        LuxuryGold.copy(alpha = 0.2f),
                                        ObsidianElevated
                                    )
                                )
                            )
                            .border(1.dp, LuxuryGold.copy(alpha = 0.4f), RoundedCornerShape(16.dp))
                            .padding(16.dp)
                    ) {
                        Column {
                            Text(
                                text = "FINAL VERIFIED RESULT",
                                color = LuxuryGold,
                                fontSize = 10.sp,
                                fontFamily = JetBrainsMonoFontFamily,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = solution.finalResult,
                                color = TitaniumLight,
                                fontSize = 24.sp,
                                fontFamily = JetBrainsMonoFontFamily,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "INTERMEDIATE DERIVATION STEPS",
                        color = BrushedTitanium,
                        fontSize = 11.sp,
                        fontFamily = JetBrainsMonoFontFamily,
                        letterSpacing = 1.sp
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // List of intermediate steps
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(solution.steps) { step ->
                            StepItemView(step)
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Actions Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Button(
                            onClick = {
                                val fullText = buildString {
                                    appendLine("=== FX Calculator CAS Proof ===")
                                    appendLine("Problem: ${solution.equation}")
                                    appendLine("Result: ${solution.finalResult}")
                                    appendLine("--- Steps ---")
                                    solution.steps.forEach { s ->
                                        appendLine("Step ${s.stepNumber}: ${s.title}")
                                        appendLine("Formula: ${s.formula}")
                                        appendLine("Explanation: ${s.explanation}")
                                    }
                                }
                                onCopyProof(fullText)
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = ObsidianElevated,
                                contentColor = LuxuryGold
                            ),
                            border = androidx.compose.foundation.BorderStroke(1.dp, LuxuryGold.copy(alpha = 0.5f)),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(Icons.Default.ContentCopy, null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Copy Complete Proof",
                                fontFamily = SpaceGroteskFontFamily,
                                fontSize = 13.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun StepItemView(step: MathStep) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(ObsidianBase)
            .border(1.dp, GlassBorderHairline, RoundedCornerShape(14.dp))
            .padding(14.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .size(28.dp)
                .clip(CircleShape)
                .background(LuxuryGold.copy(alpha = 0.2f))
                .border(1.dp, LuxuryGold, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "${step.stepNumber}",
                color = LuxuryGold,
                fontSize = 12.sp,
                fontFamily = JetBrainsMonoFontFamily,
                fontWeight = FontWeight.Bold
            )
        }

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = step.title,
                color = TitaniumLight,
                fontSize = 14.sp,
                fontFamily = SpaceGroteskFontFamily,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = step.formula,
                color = LaserCyan,
                fontSize = 13.sp,
                fontFamily = JetBrainsMonoFontFamily
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = step.explanation,
                color = BrushedTitanium,
                fontSize = 12.sp,
                fontFamily = SpaceGroteskFontFamily
            )
        }
    }
}
