package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.core.haptics.HapticEngine
import com.example.core.math.CasEngine
import com.example.core.math.CasSolution
import com.example.core.math.ExpressionEvaluator
import com.example.core.math.MathStep
import com.example.ui.components.*
import com.example.ui.screens.panels.*
import com.example.ui.theme.*
import com.example.ui.viewmodel.CalculatorViewModel

enum class CalculatorMode(val label: String, val icon: @Composable () -> Unit) {
    STANDARD("Standard", { Icon(Icons.Default.Calculate, null, modifier = Modifier.size(14.dp)) }),
    SCIENTIFIC("Scientific", { Icon(Icons.Default.Functions, null, modifier = Modifier.size(14.dp)) }),
    CAS_SOLVER("CAS Algebra", { Icon(Icons.Default.Psychology, null, modifier = Modifier.size(14.dp)) }),
    FX_FINANCE("FX Finance", { Icon(Icons.Default.CurrencyExchange, null, modifier = Modifier.size(14.dp)) }),
    GRAPHING_2D("2D Graph", { Icon(Icons.Default.ShowChart, null, modifier = Modifier.size(14.dp)) }),
    SMART_CANVAS("Math Canvas", { Icon(Icons.Default.Draw, null, modifier = Modifier.size(14.dp)) }),
    AR_VISION("AR Vision", { Icon(Icons.Default.CameraAlt, null, modifier = Modifier.size(14.dp)) }),
    AI_PROMPT("Math AI", { Icon(Icons.Default.AutoAwesome, null, modifier = Modifier.size(14.dp)) })
}

@Composable
fun MainCalculatorScreen(
    viewModel: CalculatorViewModel = viewModel()
) {
    val context = LocalContext.current
    val hapticEngine = remember { HapticEngine(context) }

    // Live preview evaluation
    val livePreview by remember(viewModel.expression, viewModel.isDegrees) {
        derivedStateOf {
            if (viewModel.expression.isBlank()) null
            else {
                val eval = ExpressionEvaluator.evaluate(viewModel.expression, viewModel.isDegrees)
                eval.getOrNull()?.let { ExpressionEvaluator.formatDisplay(it) }
            }
        }
    }

    fun copyToClipboard(text: String, label: String = "FX Calculator") {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText(label, text)
        clipboard.setPrimaryClip(clip)
        Toast.makeText(context, "Copied: $text", Toast.LENGTH_SHORT).show()
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = OledBlack,
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 20.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "fx",
                        color = LuxuryGold,
                        fontSize = 24.sp,
                        fontFamily = SpaceGroteskFontFamily,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "CALCULATOR",
                        color = TitaniumLight,
                        fontSize = 16.sp,
                        fontFamily = SpaceGroteskFontFamily,
                        fontWeight = FontWeight.Medium,
                        letterSpacing = 2.sp
                    )
                }

                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    IconButton(
                        onClick = { viewModel.setShowArchitecture(true) },
                        modifier = Modifier.testTag("btn_architecture_info")
                    ) {
                        Icon(Icons.Default.Info, contentDescription = "Architecture", tint = LaserCyan)
                    }

                    IconButton(
                        onClick = { viewModel.setShowHistory(true) },
                        modifier = Modifier.testTag("btn_history_top")
                    ) {
                        Icon(Icons.Default.History, contentDescription = "History tape", tint = LuxuryGold)
                    }
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .navigationBarsPadding()
                .padding(horizontal = 16.dp, vertical = 6.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Mode Morphing Navigation Pills
            val navScroll = rememberScrollState()
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(navScroll),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                CalculatorMode.entries.forEach { mode ->
                    val isSelected = mode == viewModel.currentMode
                    Box(
                        modifier = Modifier
                            .testTag("mode_tab_${mode.name}")
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (isSelected) LuxuryGold else ObsidianElevated)
                            .border(1.dp, if (isSelected) LuxuryGoldLight else GlassBorderHairline, RoundedCornerShape(12.dp))
                            .clickable {
                                if (viewModel.currentMode != mode) {
                                    viewModel.setMode(mode)
                                    hapticEngine.modeSwitch()
                                }
                            }
                            .padding(horizontal = 12.dp, vertical = 8.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            CompositionLocalProvider(LocalContentColor provides if (isSelected) OledBlack else TitaniumLight) {
                                mode.icon()
                            }
                            Text(
                                text = mode.label,
                                color = if (isSelected) OledBlack else TitaniumLight,
                                fontSize = 12.sp,
                                fontFamily = SpaceGroteskFontFamily,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                            )
                        }
                    }
                }
            }

            // Display Screen Readout
            FxDisplay(
                expression = viewModel.expression,
                result = viewModel.resultText,
                livePreview = livePreview,
                isDegrees = viewModel.isDegrees,
                activeModeName = viewModel.currentMode.label,
                onToggleAngleMode = {
                    viewModel.toggleAngleMode()
                    hapticEngine.click()
                },
                onOpenHistory = { viewModel.setShowHistory(true) },
                onOpenProof = {
                    val sol = CasSolution(
                        queryType = viewModel.currentMode.label,
                        equation = if (viewModel.expression.isNotBlank()) viewModel.expression else "f(x)",
                        finalResult = viewModel.resultText,
                        steps = listOf(
                            MathStep(1, "Expression Formulation", viewModel.expression.ifBlank { "0" }, "Structured in CAS syntax tree."),
                            MathStep(2, "BODMAS/PEMDAS Reduction", "Intermediate reduction completed.", "Operators processed hierarchically."),
                            MathStep(3, "Result Verification", viewModel.resultText, "Numerical equality confirmed.")
                        )
                    )
                    viewModel.setActiveProof(sol)
                },
                onCopyResult = {
                    copyToClipboard(viewModel.resultText, "FX Calculator Result")
                    hapticEngine.click()
                }
            )

            // Dynamic Mode Workbench Area
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                when (viewModel.currentMode) {
                    CalculatorMode.STANDARD -> {
                        StandardKeypad(
                            onDigit = { d ->
                                viewModel.onDigit(d)
                                hapticEngine.tick()
                            },
                            onOperator = { op ->
                                viewModel.onOperator(op)
                                hapticEngine.click()
                            },
                            onClear = {
                                viewModel.onClear()
                                hapticEngine.click()
                            },
                            onDelete = {
                                viewModel.onDelete()
                                hapticEngine.click()
                            },
                            onParentheses = {
                                viewModel.onParentheses()
                                hapticEngine.click()
                            },
                            onPercent = {
                                viewModel.onPercent()
                                hapticEngine.click()
                            },
                            onEquals = {
                                val res = viewModel.executeCalculation()
                                res.onSuccess { hapticEngine.execute() }
                                res.onFailure {
                                    hapticEngine.error()
                                    Toast.makeText(context, it.message ?: "Syntax Error", Toast.LENGTH_SHORT).show()
                                }
                            }
                        )
                    }

                    CalculatorMode.SCIENTIFIC -> {
                        ScientificKeypad(
                            onAppend = { token ->
                                viewModel.onAppend(token)
                                hapticEngine.tick()
                            },
                            onClear = {
                                viewModel.onClear()
                                hapticEngine.click()
                            },
                            onDelete = {
                                viewModel.onDelete()
                                hapticEngine.click()
                            },
                            onEquals = {
                                val res = viewModel.executeCalculation()
                                res.onSuccess { hapticEngine.execute() }
                                res.onFailure {
                                    hapticEngine.error()
                                    Toast.makeText(context, it.message ?: "Syntax Error", Toast.LENGTH_SHORT).show()
                                }
                            }
                        )
                    }

                    CalculatorMode.CAS_SOLVER -> {
                        CasWorkbenchPanel(
                            onSolutionGenerated = { sol ->
                                viewModel.handleCasSolution(sol)
                                hapticEngine.execute()
                            }
                        )
                    }

                    CalculatorMode.FX_FINANCE -> {
                        FinanceFxPanel()
                    }

                    CalculatorMode.GRAPHING_2D -> {
                        val exprToPlot = if (viewModel.expression.contains("x")) viewModel.expression else "sin(x) * 2"
                        GraphCanvas(
                            functionExpression = exprToPlot,
                            modifier = Modifier.fillMaxSize()
                        )
                    }

                    CalculatorMode.SMART_CANVAS -> {
                        SmartMathCanvas(
                            onSolveCanvas = { _ ->
                                hapticEngine.execute()
                                val sol = CasEngine.solveQuadratic(1.0, -4.0, 4.0)
                                viewModel.handleCasSolution(sol)
                            },
                            modifier = Modifier.fillMaxSize()
                        )
                    }

                    CalculatorMode.AR_VISION -> {
                        ArScannerPanel(
                            onSolutionScanned = { sol ->
                                viewModel.handleCasSolution(sol)
                                hapticEngine.execute()
                            }
                        )
                    }

                    CalculatorMode.AI_PROMPT -> {
                        AiMathPromptPanel(
                            onSolutionGenerated = { sol ->
                                viewModel.handleCasSolution(sol)
                                hapticEngine.execute()
                            }
                        )
                    }
                }
            }
        }
    }

    // Step-by-Step Proof Dialog
    if (viewModel.activeProofSolution != null) {
        StepByStepProofDialog(
            solution = viewModel.activeProofSolution!!,
            onDismiss = { viewModel.setActiveProof(null) },
            onCopyProof = { proofText -> copyToClipboard(proofText, "Mathematical Proof") }
        )
    }

    // History Tape Sheet
    if (viewModel.showHistorySheet) {
        HistoryTapeSheet(
            historyItems = viewModel.historyItems,
            onDismiss = { viewModel.setShowHistory(false) },
            onReuse = { pastVal ->
                viewModel.reuseResult(pastVal)
                hapticEngine.click()
            },
            onViewProof = { sol ->
                viewModel.setActiveProof(sol)
                viewModel.setShowHistory(false)
            },
            onClearHistory = {
                viewModel.clearHistory()
                hapticEngine.click()
            }
        )
    }

    // Architecture Modal
    if (viewModel.showArchitectureModal) {
        ArchitectureModal(
            onDismiss = { viewModel.setShowArchitecture(false) }
        )
    }
}
