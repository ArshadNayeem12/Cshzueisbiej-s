package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.ui.theme.*

@Composable
fun ArchitectureModal(
    onDismiss: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.88f))
                .padding(horizontal = 16.dp, vertical = 24.dp),
            contentAlignment = Alignment.Center
        ) {
            GlassmorphicCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.92f)
                    .testTag("architecture_blueprint_dialog"),
                shape = RoundedCornerShape(24.dp),
                accentGlowColor = LaserCyan
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
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(LaserCyan.copy(alpha = 0.15f))
                                    .padding(horizontal = 8.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = "SYSTEM ARCHITECTURE & BLUEPRINT",
                                    color = LaserCyan,
                                    fontSize = 10.sp,
                                    fontFamily = JetBrainsMonoFontFamily,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "FX Calculator Tech Stack",
                                color = TitaniumLight,
                                fontSize = 18.sp,
                                fontFamily = SpaceGroteskFontFamily,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        IconButton(
                            onClick = onDismiss,
                            modifier = Modifier
                                .size(36.dp)
                                .testTag("btn_close_architecture")
                        ) {
                            Icon(Icons.Default.Close, "Close", tint = TitaniumLight)
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        item {
                            BlueprintSection(
                                title = "1. Mobile Tech Stack Recommendation",
                                badge = "BENCHMARK & SPECS",
                                content = """
                                • Native Android (Kotlin + Jetpack Compose) / iOS (SwiftUI + Metal):
                                  - Unmatched 120Hz ProMotion smoothness with direct hardware VSYNC cadence.
                                  - Sub-millisecond latency for custom tactile haptic waveforms (VibrationEffect / CoreHaptics).
                                  - Zero-overhead CAS math computation with SIMD vectorization and native compilation.
                                  
                                • Cross-Platform Alternative (Flutter / Impeller):
                                  - Impeller Vulkan backend guarantees 120fps stutter-free shader execution and glassmorphic blur filters.
                                  - Dart FFI binds directly to C++ SymEngine or GiNaC CAS libraries.
                                  
                                • React Native (New Architecture / Fabric):
                                  - Viable with JSI C++ turbo modules, but prone to frame hitching during dense 2D graphing redraws.
                                  
                                • Verdict: Native Jetpack Compose + Kotlin Coroutines provides the absolute peak luxury timepiece feel, lowest memory footprint, and lowest battery drain on OLED panels.
                                """.trimIndent()
                            )
                        }

                        item {
                            BlueprintSection(
                                title = "2. Production Architecture & Directory Structure",
                                badge = "CLEAN MVVM",
                                content = """
                                ├── core/
                                │   ├── haptics/       # HapticEngine (ticks, clicks, thump)
                                │   ├── math/          # ExpressionEvaluator, Shunting-Yard, CAS Engine
                                │   ├── finance/       # FxEngine (forex, interest, loans, bills)
                                │   └── ai/            # GeminiMathService (multimodal vision + reasoning)
                                ├── data/
                                │   └── HistoryTape/   # Room database entities & repositories
                                ├── ui/
                                │   ├── theme/         # OLED pure black, Titanium & Gold palette
                                │   ├── components/    # FxButton, FxDisplay, GraphCanvas, SmartMathCanvas
                                │   └── screens/       # MainCalculatorScreen, Modes, Sheet dialogs
                                └── MainActivity.kt    # Edge-to-edge coordinator
                                """.trimIndent()
                            )
                        }

                        item {
                            BlueprintSection(
                                title = "3. Sensory & OLED Optimization Directives",
                                badge = "120HZ & SENSORY",
                                content = """
                                • Deep OLED Pure Black (#000000): Turn off pixels completely for zero power draw on dark areas.
                                • Mechanical Tactility: Micro-second haptic timing differentiates 8ms number ticks from 32ms execute thumps.
                                • Spring Physics: Compose spring(dampingRatio = 0.65f, stiffness = 800f) mimics titanium mechanical key recoil.
                                • Mathematical Rigor: Shunting-yard RPN parser handles full BODMAS/PEMDAS with automatic step-by-step proof generation.
                                """.trimIndent()
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun BlueprintSection(
    title: String,
    badge: String,
    content: String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(ObsidianBase)
            .border(1.dp, GlassBorderHairline, RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .background(LuxuryGold.copy(alpha = 0.15f))
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
                Text(
                    text = badge,
                    color = LuxuryGold,
                    fontSize = 9.sp,
                    fontFamily = JetBrainsMonoFontFamily,
                    fontWeight = FontWeight.Bold
                )
            }
            Text(
                text = title,
                color = TitaniumLight,
                fontSize = 15.sp,
                fontFamily = SpaceGroteskFontFamily,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = content,
            color = BrushedTitanium,
            fontSize = 12.sp,
            fontFamily = JetBrainsMonoFontFamily,
            lineHeight = 18.sp
        )
    }
}
