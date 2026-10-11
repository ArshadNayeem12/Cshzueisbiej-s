package com.example.ui.screens.panels

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.math.CasEngine
import com.example.core.math.CasSolution
import com.example.core.math.Matrix2x2
import com.example.core.math.Vector3D
import com.example.ui.components.GlassmorphicCard
import com.example.ui.theme.*

enum class CasToolTab {
    QUADRATIC,
    SYSTEM_2X2,
    DERIVATIVE,
    INTEGRAL,
    MATRIX,
    VECTORS
}

@Composable
fun CasWorkbenchPanel(
    onSolutionGenerated: (CasSolution) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableStateOf(CasToolTab.QUADRATIC) }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Tab Pills Row
        val tabScroll = rememberScrollState()
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(tabScroll),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            CasToolTab.entries.forEach { tab ->
                val isSelected = tab == selectedTab
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(if (isSelected) LuxuryGold else ObsidianElevated)
                        .border(1.dp, if (isSelected) LuxuryGoldLight else GlassBorderHairline, RoundedCornerShape(10.dp))
                        .clickable { selectedTab = tab }
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = when (tab) {
                            CasToolTab.QUADRATIC -> "ax² + bx + c"
                            CasToolTab.SYSTEM_2X2 -> "System 2x2"
                            CasToolTab.DERIVATIVE -> "d/dx [f(x)]"
                            CasToolTab.INTEGRAL -> "∫ f(x) dx"
                            CasToolTab.MATRIX -> "Matrix 2x2"
                            CasToolTab.VECTORS -> "Vectors 3D"
                        },
                        color = if (isSelected) OledBlack else TitaniumLight,
                        fontSize = 11.sp,
                        fontFamily = JetBrainsMonoFontFamily,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                    )
                }
            }
        }

        // Active Tool Sub-Panel
        GlassmorphicCard(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("cas_workbench_card"),
            shape = RoundedCornerShape(20.dp),
            accentGlowColor = LuxuryGold.copy(alpha = 0.2f)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                when (selectedTab) {
                    CasToolTab.QUADRATIC -> QuadraticTool(onSolutionGenerated)
                    CasToolTab.SYSTEM_2X2 -> System2x2Tool(onSolutionGenerated)
                    CasToolTab.DERIVATIVE -> DerivativeTool(onSolutionGenerated)
                    CasToolTab.INTEGRAL -> IntegralTool(onSolutionGenerated)
                    CasToolTab.MATRIX -> MatrixTool(onSolutionGenerated)
                    CasToolTab.VECTORS -> VectorTool(onSolutionGenerated)
                }
            }
        }
    }
}

@Composable
private fun QuadraticTool(onSolve: (CasSolution) -> Unit) {
    var aStr by remember { mutableStateOf("1") }
    var bStr by remember { mutableStateOf("-5") }
    var cStr by remember { mutableStateOf("6") }

    Text("Solve Quadratic Polynomial: ax² + bx + c = 0", color = LuxuryGold, fontSize = 13.sp, fontFamily = SpaceGroteskFontFamily, fontWeight = FontWeight.Bold)

    Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = aStr,
            onValueChange = { aStr = it },
            label = { Text("a", color = BrushedTitanium, fontSize = 11.sp) },
            modifier = Modifier.weight(1f),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            colors = customFieldColors()
        )
        OutlinedTextField(
            value = bStr,
            onValueChange = { bStr = it },
            label = { Text("b", color = BrushedTitanium, fontSize = 11.sp) },
            modifier = Modifier.weight(1f),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            colors = customFieldColors()
        )
        OutlinedTextField(
            value = cStr,
            onValueChange = { cStr = it },
            label = { Text("c", color = BrushedTitanium, fontSize = 11.sp) },
            modifier = Modifier.weight(1f),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            colors = customFieldColors()
        )
    }

    Button(
        onClick = {
            val a = aStr.toDoubleOrNull() ?: 1.0
            val b = bStr.toDoubleOrNull() ?: 0.0
            val c = cStr.toDoubleOrNull() ?: 0.0
            val sol = CasEngine.solveQuadratic(a, b, c)
            onSolve(sol)
        },
        colors = ButtonDefaults.buttonColors(containerColor = LuxuryGold, contentColor = OledBlack),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Icon(Icons.Default.AutoAwesome, null, modifier = Modifier.size(16.dp))
        Spacer(modifier = Modifier.width(6.dp))
        Text("Solve & Generate Mathematical Proof", fontFamily = SpaceGroteskFontFamily, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun System2x2Tool(onSolve: (CasSolution) -> Unit) {
    var a1 by remember { mutableStateOf("2") }
    var b1 by remember { mutableStateOf("3") }
    var c1 by remember { mutableStateOf("8") }
    var a2 by remember { mutableStateOf("4") }
    var b2 by remember { mutableStateOf("-1") }
    var c2 by remember { mutableStateOf("2") }

    Text("Solve 2x2 Linear System (Cramer's Rule)", color = LuxuryGold, fontSize = 13.sp, fontFamily = SpaceGroteskFontFamily, fontWeight = FontWeight.Bold)

    Row(horizontalArrangement = Arrangement.spacedBy(6.dp), modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(value = a1, onValueChange = { a1 = it }, label = { Text("a1", fontSize = 10.sp) }, modifier = Modifier.weight(1f), colors = customFieldColors())
        OutlinedTextField(value = b1, onValueChange = { b1 = it }, label = { Text("b1", fontSize = 10.sp) }, modifier = Modifier.weight(1f), colors = customFieldColors())
        OutlinedTextField(value = c1, onValueChange = { c1 = it }, label = { Text("= c1", fontSize = 10.sp) }, modifier = Modifier.weight(1f), colors = customFieldColors())
    }
    Row(horizontalArrangement = Arrangement.spacedBy(6.dp), modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(value = a2, onValueChange = { a2 = it }, label = { Text("a2", fontSize = 10.sp) }, modifier = Modifier.weight(1f), colors = customFieldColors())
        OutlinedTextField(value = b2, onValueChange = { b2 = it }, label = { Text("b2", fontSize = 10.sp) }, modifier = Modifier.weight(1f), colors = customFieldColors())
        OutlinedTextField(value = c2, onValueChange = { c2 = it }, label = { Text("= c2", fontSize = 10.sp) }, modifier = Modifier.weight(1f), colors = customFieldColors())
    }

    Button(
        onClick = {
            val sol = CasEngine.solveSystem2x2(
                a1.toDoubleOrNull() ?: 2.0, b1.toDoubleOrNull() ?: 3.0, c1.toDoubleOrNull() ?: 8.0,
                a2.toDoubleOrNull() ?: 4.0, b2.toDoubleOrNull() ?: -1.0, c2.toDoubleOrNull() ?: 2.0
            )
            onSolve(sol)
        },
        colors = ButtonDefaults.buttonColors(containerColor = LuxuryGold, contentColor = OledBlack),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Icon(Icons.Default.Calculate, null, modifier = Modifier.size(16.dp))
        Spacer(modifier = Modifier.width(6.dp))
        Text("Solve System with Cramer's Rule", fontFamily = SpaceGroteskFontFamily, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun DerivativeTool(onSolve: (CasSolution) -> Unit) {
    var func by remember { mutableStateOf("3x^2 + 5x - 7") }
    var x0 by remember { mutableStateOf("2") }

    Text("Numerical & Symbolic Derivative: d/dx [f(x)]", color = LaserCyan, fontSize = 13.sp, fontFamily = SpaceGroteskFontFamily, fontWeight = FontWeight.Bold)

    OutlinedTextField(value = func, onValueChange = { func = it }, label = { Text("f(x)", fontSize = 10.sp) }, modifier = Modifier.fillMaxWidth(), colors = customFieldColors())
    OutlinedTextField(value = x0, onValueChange = { x0 = it }, label = { Text("Point x₀", fontSize = 10.sp) }, modifier = Modifier.fillMaxWidth(), colors = customFieldColors())

    Button(
        onClick = {
            val sol = CasEngine.numericalDerivative(func, x0.toDoubleOrNull() ?: 2.0)
            onSolve(sol)
        },
        colors = ButtonDefaults.buttonColors(containerColor = LaserCyan, contentColor = OledBlack),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Text("Evaluate Tangent Slope f'(x₀)", fontFamily = SpaceGroteskFontFamily, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun IntegralTool(onSolve: (CasSolution) -> Unit) {
    var func by remember { mutableStateOf("x^2") }
    var lowerA by remember { mutableStateOf("0") }
    var upperB by remember { mutableStateOf("3") }

    Text("Definite Integral (Simpson's 1/3 Rule): ∫[a,b] f(x) dx", color = WarmCopper, fontSize = 13.sp, fontFamily = SpaceGroteskFontFamily, fontWeight = FontWeight.Bold)

    OutlinedTextField(value = func, onValueChange = { func = it }, label = { Text("Integrand f(x)", fontSize = 10.sp) }, modifier = Modifier.fillMaxWidth(), colors = customFieldColors())
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(value = lowerA, onValueChange = { lowerA = it }, label = { Text("Lower bound a", fontSize = 10.sp) }, modifier = Modifier.weight(1f), colors = customFieldColors())
        OutlinedTextField(value = upperB, onValueChange = { upperB = it }, label = { Text("Upper bound b", fontSize = 10.sp) }, modifier = Modifier.weight(1f), colors = customFieldColors())
    }

    Button(
        onClick = {
            val a = lowerA.toDoubleOrNull() ?: 0.0
            val b = upperB.toDoubleOrNull() ?: 3.0
            val sol = CasEngine.definiteIntegral(func, a, b)
            onSolve(sol)
        },
        colors = ButtonDefaults.buttonColors(containerColor = WarmCopper, contentColor = OledBlack),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Text("Calculate Definite Area Under Curve", fontFamily = SpaceGroteskFontFamily, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun MatrixTool(onSolve: (CasSolution) -> Unit) {
    var m11 by remember { mutableStateOf("4") }
    var m12 by remember { mutableStateOf("7") }
    var m21 by remember { mutableStateOf("2") }
    var m22 by remember { mutableStateOf("6") }

    Text("Matrix 2x2: Determinant & Adjugate Inversion", color = LuxuryGold, fontSize = 13.sp, fontFamily = SpaceGroteskFontFamily, fontWeight = FontWeight.Bold)

    Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(value = m11, onValueChange = { m11 = it }, label = { Text("a11", fontSize = 10.sp) }, modifier = Modifier.weight(1f), colors = customFieldColors())
        OutlinedTextField(value = m12, onValueChange = { m12 = it }, label = { Text("a12", fontSize = 10.sp) }, modifier = Modifier.weight(1f), colors = customFieldColors())
    }
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(value = m21, onValueChange = { m21 = it }, label = { Text("a21", fontSize = 10.sp) }, modifier = Modifier.weight(1f), colors = customFieldColors())
        OutlinedTextField(value = m22, onValueChange = { m22 = it }, label = { Text("a22", fontSize = 10.sp) }, modifier = Modifier.weight(1f), colors = customFieldColors())
    }

    Button(
        onClick = {
            val m = Matrix2x2(
                m11.toDoubleOrNull() ?: 4.0, m12.toDoubleOrNull() ?: 7.0,
                m21.toDoubleOrNull() ?: 2.0, m22.toDoubleOrNull() ?: 6.0
            )
            onSolve(CasEngine.matrix2x2Analysis(m))
        },
        colors = ButtonDefaults.buttonColors(containerColor = LuxuryGold, contentColor = OledBlack),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Text("Invert Matrix & Calculate det(A)", fontFamily = SpaceGroteskFontFamily, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun VectorTool(onSolve: (CasSolution) -> Unit) {
    var ux by remember { mutableStateOf("1") }
    var uy by remember { mutableStateOf("2") }
    var uz by remember { mutableStateOf("3") }
    var vx by remember { mutableStateOf("4") }
    var vy by remember { mutableStateOf("5") }
    var vz by remember { mutableStateOf("6") }

    Text("3D Vectors: Dot Product, Cross Product & Angle", color = LaserCyan, fontSize = 13.sp, fontFamily = SpaceGroteskFontFamily, fontWeight = FontWeight.Bold)

    Row(horizontalArrangement = Arrangement.spacedBy(6.dp), modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(value = ux, onValueChange = { ux = it }, label = { Text("ux", fontSize = 10.sp) }, modifier = Modifier.weight(1f), colors = customFieldColors())
        OutlinedTextField(value = uy, onValueChange = { uy = it }, label = { Text("uy", fontSize = 10.sp) }, modifier = Modifier.weight(1f), colors = customFieldColors())
        OutlinedTextField(value = uz, onValueChange = { uz = it }, label = { Text("uz", fontSize = 10.sp) }, modifier = Modifier.weight(1f), colors = customFieldColors())
    }
    Row(horizontalArrangement = Arrangement.spacedBy(6.dp), modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(value = vx, onValueChange = { vx = it }, label = { Text("vx", fontSize = 10.sp) }, modifier = Modifier.weight(1f), colors = customFieldColors())
        OutlinedTextField(value = vy, onValueChange = { vy = it }, label = { Text("vy", fontSize = 10.sp) }, modifier = Modifier.weight(1f), colors = customFieldColors())
        OutlinedTextField(value = vz, onValueChange = { vz = it }, label = { Text("vz", fontSize = 10.sp) }, modifier = Modifier.weight(1f), colors = customFieldColors())
    }

    Button(
        onClick = {
            val u = Vector3D(ux.toDoubleOrNull() ?: 1.0, uy.toDoubleOrNull() ?: 2.0, uz.toDoubleOrNull() ?: 3.0)
            val v = Vector3D(vx.toDoubleOrNull() ?: 4.0, vy.toDoubleOrNull() ?: 5.0, vz.toDoubleOrNull() ?: 6.0)
            onSolve(CasEngine.vectorAnalysis(u, v))
        },
        colors = ButtonDefaults.buttonColors(containerColor = LaserCyan, contentColor = OledBlack),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Text("Compute Dot, Cross Product & θ", fontFamily = SpaceGroteskFontFamily, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun customFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedTextColor = TitaniumLight,
    unfocusedTextColor = TitaniumLight,
    focusedBorderColor = LuxuryGold,
    unfocusedBorderColor = GlassBorderHairline,
    focusedContainerColor = ObsidianBase,
    unfocusedContainerColor = ObsidianBase,
    focusedLabelColor = LuxuryGold,
    unfocusedLabelColor = BrushedTitanium
)
