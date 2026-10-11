package com.example.ui.screens.panels

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.ui.components.FxButton
import com.example.ui.components.FxButtonVariant

@Composable
fun StandardKeypad(
    onDigit: (String) -> Unit,
    onOperator: (String) -> Unit,
    onClear: () -> Unit,
    onDelete: () -> Unit,
    onParentheses: () -> Unit,
    onPercent: () -> Unit,
    onEquals: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        // Row 1
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            FxButton("AC", onClear, Modifier.weight(1f).fillMaxHeight(), FxButtonVariant.ACTION)
            FxButton("( )", onParentheses, Modifier.weight(1f).fillMaxHeight(), FxButtonVariant.FUNCTION)
            FxButton("%", onPercent, Modifier.weight(1f).fillMaxHeight(), FxButtonVariant.FUNCTION)
            FxButton("÷", { onOperator("÷") }, Modifier.weight(1f).fillMaxHeight(), FxButtonVariant.OPERATOR)
        }

        // Row 2
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            FxButton("7", { onDigit("7") }, Modifier.weight(1f).fillMaxHeight(), FxButtonVariant.NUMBER)
            FxButton("8", { onDigit("8") }, Modifier.weight(1f).fillMaxHeight(), FxButtonVariant.NUMBER)
            FxButton("9", { onDigit("9") }, Modifier.weight(1f).fillMaxHeight(), FxButtonVariant.NUMBER)
            FxButton("×", { onOperator("×") }, Modifier.weight(1f).fillMaxHeight(), FxButtonVariant.OPERATOR)
        }

        // Row 3
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            FxButton("4", { onDigit("4") }, Modifier.weight(1f).fillMaxHeight(), FxButtonVariant.NUMBER)
            FxButton("5", { onDigit("5") }, Modifier.weight(1f).fillMaxHeight(), FxButtonVariant.NUMBER)
            FxButton("6", { onDigit("6") }, Modifier.weight(1f).fillMaxHeight(), FxButtonVariant.NUMBER)
            FxButton("−", { onOperator("−") }, Modifier.weight(1f).fillMaxHeight(), FxButtonVariant.OPERATOR)
        }

        // Row 4
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            FxButton("1", { onDigit("1") }, Modifier.weight(1f).fillMaxHeight(), FxButtonVariant.NUMBER)
            FxButton("2", { onDigit("2") }, Modifier.weight(1f).fillMaxHeight(), FxButtonVariant.NUMBER)
            FxButton("3", { onDigit("3") }, Modifier.weight(1f).fillMaxHeight(), FxButtonVariant.NUMBER)
            FxButton("+", { onOperator("+") }, Modifier.weight(1f).fillMaxHeight(), FxButtonVariant.OPERATOR)
        }

        // Row 5
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            FxButton("0", { onDigit("0") }, Modifier.weight(1f).fillMaxHeight(), FxButtonVariant.NUMBER)
            FxButton(".", { onDigit(".") }, Modifier.weight(1f).fillMaxHeight(), FxButtonVariant.NUMBER)
            FxButton("DEL", onDelete, Modifier.weight(1f).fillMaxHeight(), FxButtonVariant.FUNCTION)
            FxButton("=", onEquals, Modifier.weight(1f).fillMaxHeight(), FxButtonVariant.EQUAL)
        }
    }
}
