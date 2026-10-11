package com.example.ui.screens.panels

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
fun AiMathPromptPanel(
    onSolutionGenerated: (CasSolution) -> Unit,
    modifier: Modifier = Modifier
) {
    var prompt by remember { mutableStateOf("Solve quadratic equation 2x² - 8x + 6 = 0") }
    var isLoading by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    val presets = listOf(
        "Solve 2x² - 8x + 6 = 0",
        "Derivative of x³ - 5x at x=3",
        "Integrate 4x² from 0 to 5",
        "Compound $20k at 8% for 12 yrs"
    )

    GlassmorphicCard(
        modifier = modifier
            .fillMaxWidth()
            .testTag("ai_math_prompt_card"),
        shape = RoundedCornerShape(20.dp),
        accentGlowColor = LuxuryGold.copy(alpha = 0.3f)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Natural Language Math AI",
                        color = LuxuryGold,
                        fontSize = 15.sp,
                        fontFamily = SpaceGroteskFontFamily,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Type conversational word problems or formulas",
                        color = BrushedTitanium,
                        fontSize = 11.sp,
                        fontFamily = SpaceGroteskFontFamily
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(LuxuryGold.copy(alpha = 0.15f))
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = "REASONING AI",
                        color = LuxuryGold,
                        fontSize = 10.sp,
                        fontFamily = JetBrainsMonoFontFamily,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Presets row
            val scrollState = rememberScrollState()
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(scrollState),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                presets.forEach { preset ->
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(ObsidianElevated)
                            .clickable { prompt = preset }
                            .padding(horizontal = 10.dp, vertical = 5.dp)
                    ) {
                        Text(
                            text = preset,
                            color = TitaniumLight,
                            fontSize = 11.sp,
                            fontFamily = JetBrainsMonoFontFamily
                        )
                    }
                }
            }

            OutlinedTextField(
                value = prompt,
                onValueChange = { prompt = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Conversational Math Prompt", fontSize = 11.sp) },
                maxLines = 3,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = TitaniumLight,
                    unfocusedTextColor = TitaniumLight,
                    focusedBorderColor = LuxuryGold,
                    unfocusedBorderColor = GlassBorderHairline,
                    focusedContainerColor = ObsidianBase,
                    unfocusedContainerColor = ObsidianBase,
                    focusedLabelColor = LuxuryGold,
                    unfocusedLabelColor = BrushedTitanium
                )
            )

            Button(
                onClick = {
                    if (prompt.isNotBlank() && !isLoading) {
                        isLoading = true
                        scope.launch {
                            val sol = GeminiMathService.solveWordProblem(prompt)
                            isLoading = false
                            onSolutionGenerated(sol)
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = LuxuryGold,
                    contentColor = OledBlack
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("btn_solve_ai_prompt")
            ) {
                if (isLoading) {
                    CircularProgressIndicator(color = OledBlack, modifier = Modifier.size(16.dp), strokeWidth = 2.dp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Deriving Proof...", fontFamily = SpaceGroteskFontFamily, fontWeight = FontWeight.Bold)
                } else {
                    Icon(Icons.Default.AutoAwesome, null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Evaluate Word Problem with Proof", fontFamily = SpaceGroteskFontFamily, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
