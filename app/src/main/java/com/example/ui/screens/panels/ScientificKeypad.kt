package com.example.ui.screens.panels

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.FxButton
import com.example.ui.components.FxButtonVariant

@Composable
fun ScientificKeypad(
    onAppend: (String) -> Unit,
    onClear: () -> Unit,
    onDelete: () -> Unit,
    onEquals: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        val rows = listOf(
            listOf("sin" to "sin(", "cos" to "cos(", "tan" to "tan(", "π" to "π", "e" to "e"),
            listOf("asin" to "asin(", "acos" to "acos(", "atan" to "atan(", "ln" to "ln(", "log" to "log("),
            listOf("x²" to "^2", "xʸ" to "^", "√" to "sqrt(", "!" to "!", "φ" to "φ"),
            listOf("AC" to "AC", "(" to "(", ")" to ")", "%" to "%", "÷" to "÷"),
            listOf("7" to "7", "8" to "8", "9" to "9", "DEL" to "DEL", "×" to "×"),
            listOf("4" to "4", "5" to "5", "6" to "6", "abs" to "abs(", "−" to "−"),
            listOf("1" to "1", "2" to "2", "3" to "3", "+" to "+", "=" to "="),
            listOf("0" to "0", "." to ".", "1/x" to "1/(", "" to "", "" to "")
        )

        // Sci Row 1
        Row(modifier = Modifier.fillMaxWidth().weight(1f), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            FxButton("sin", { onAppend("sin(") }, Modifier.weight(1f).fillMaxHeight(), FxButtonVariant.FUNCTION, 12.sp)
            FxButton("cos", { onAppend("cos(") }, Modifier.weight(1f).fillMaxHeight(), FxButtonVariant.FUNCTION, 12.sp)
            FxButton("tan", { onAppend("tan(") }, Modifier.weight(1f).fillMaxHeight(), FxButtonVariant.FUNCTION, 12.sp)
            FxButton("π", { onAppend("π") }, Modifier.weight(1f).fillMaxHeight(), FxButtonVariant.FUNCTION, 13.sp)
            FxButton("e", { onAppend("e") }, Modifier.weight(1f).fillMaxHeight(), FxButtonVariant.FUNCTION, 13.sp)
        }
        // Sci Row 2
        Row(modifier = Modifier.fillMaxWidth().weight(1f), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            FxButton("asin", { onAppend("asin(") }, Modifier.weight(1f).fillMaxHeight(), FxButtonVariant.FUNCTION, 11.sp)
            FxButton("acos", { onAppend("acos(") }, Modifier.weight(1f).fillMaxHeight(), FxButtonVariant.FUNCTION, 11.sp)
            FxButton("atan", { onAppend("atan(") }, Modifier.weight(1f).fillMaxHeight(), FxButtonVariant.FUNCTION, 11.sp)
            FxButton("ln", { onAppend("ln(") }, Modifier.weight(1f).fillMaxHeight(), FxButtonVariant.FUNCTION, 13.sp)
            FxButton("log", { onAppend("log(") }, Modifier.weight(1f).fillMaxHeight(), FxButtonVariant.FUNCTION, 12.sp)
        }
        // Sci Row 3
        Row(modifier = Modifier.fillMaxWidth().weight(1f), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            FxButton("x²", { onAppend("^2") }, Modifier.weight(1f).fillMaxHeight(), FxButtonVariant.FUNCTION, 12.sp)
            FxButton("xʸ", { onAppend("^") }, Modifier.weight(1f).fillMaxHeight(), FxButtonVariant.FUNCTION, 12.sp)
            FxButton("√", { onAppend("sqrt(") }, Modifier.weight(1f).fillMaxHeight(), FxButtonVariant.FUNCTION, 13.sp)
            FxButton("!", { onAppend("!") }, Modifier.weight(1f).fillMaxHeight(), FxButtonVariant.FUNCTION, 13.sp)
            FxButton("φ", { onAppend("φ") }, Modifier.weight(1f).fillMaxHeight(), FxButtonVariant.FUNCTION, 13.sp)
        }
        // Row 4
        Row(modifier = Modifier.fillMaxWidth().weight(1f), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            FxButton("AC", onClear, Modifier.weight(1f).fillMaxHeight(), FxButtonVariant.ACTION, 12.sp)
            FxButton("(", { onAppend("(") }, Modifier.weight(1f).fillMaxHeight(), FxButtonVariant.FUNCTION, 13.sp)
            FxButton(")", { onAppend(")") }, Modifier.weight(1f).fillMaxHeight(), FxButtonVariant.FUNCTION, 13.sp)
            FxButton("%", { onAppend("%") }, Modifier.weight(1f).fillMaxHeight(), FxButtonVariant.FUNCTION, 13.sp)
            FxButton("÷", { onAppend("÷") }, Modifier.weight(1f).fillMaxHeight(), FxButtonVariant.OPERATOR, 15.sp)
        }
        // Row 5
        Row(modifier = Modifier.fillMaxWidth().weight(1f), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            FxButton("7", { onAppend("7") }, Modifier.weight(1f).fillMaxHeight(), FxButtonVariant.NUMBER, 15.sp)
            FxButton("8", { onAppend("8") }, Modifier.weight(1f).fillMaxHeight(), FxButtonVariant.NUMBER, 15.sp)
            FxButton("9", { onAppend("9") }, Modifier.weight(1f).fillMaxHeight(), FxButtonVariant.NUMBER, 15.sp)
            FxButton("DEL", onDelete, Modifier.weight(1f).fillMaxHeight(), FxButtonVariant.FUNCTION, 11.sp)
            FxButton("×", { onAppend("×") }, Modifier.weight(1f).fillMaxHeight(), FxButtonVariant.OPERATOR, 15.sp)
        }
        // Row 6
        Row(modifier = Modifier.fillMaxWidth().weight(1f), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            FxButton("4", { onAppend("4") }, Modifier.weight(1f).fillMaxHeight(), FxButtonVariant.NUMBER, 15.sp)
            FxButton("5", { onAppend("5") }, Modifier.weight(1f).fillMaxHeight(), FxButtonVariant.NUMBER, 15.sp)
            FxButton("6", { onAppend("6") }, Modifier.weight(1f).fillMaxHeight(), FxButtonVariant.NUMBER, 15.sp)
            FxButton("abs", { onAppend("abs(") }, Modifier.weight(1f).fillMaxHeight(), FxButtonVariant.FUNCTION, 11.sp)
            FxButton("−", { onAppend("−") }, Modifier.weight(1f).fillMaxHeight(), FxButtonVariant.OPERATOR, 15.sp)
        }
        // Row 7
        Row(modifier = Modifier.fillMaxWidth().weight(1f), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            FxButton("1", { onAppend("1") }, Modifier.weight(1f).fillMaxHeight(), FxButtonVariant.NUMBER, 15.sp)
            FxButton("2", { onAppend("2") }, Modifier.weight(1f).fillMaxHeight(), FxButtonVariant.NUMBER, 15.sp)
            FxButton("3", { onAppend("3") }, Modifier.weight(1f).fillMaxHeight(), FxButtonVariant.NUMBER, 15.sp)
            FxButton("+", { onAppend("+") }, Modifier.weight(1f).fillMaxHeight(), FxButtonVariant.OPERATOR, 15.sp)
            FxButton("=", onEquals, Modifier.weight(1f).fillMaxHeight(), FxButtonVariant.EQUAL, 16.sp)
        }
        // Row 8
        Row(modifier = Modifier.fillMaxWidth().weight(1f), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            FxButton("0", { onAppend("0") }, Modifier.weight(2f).fillMaxHeight(), FxButtonVariant.NUMBER, 15.sp)
            FxButton(".", { onAppend(".") }, Modifier.weight(1f).fillMaxHeight(), FxButtonVariant.NUMBER, 15.sp)
            FxButton("1/x", { onAppend("1/(") }, Modifier.weight(2f).fillMaxHeight(), FxButtonVariant.FUNCTION, 11.sp)
        }
    }
}
