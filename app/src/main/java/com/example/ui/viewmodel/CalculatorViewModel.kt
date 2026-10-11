package com.example.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.core.math.CasSolution
import com.example.core.math.ExpressionEvaluator
import com.example.core.math.MathStep
import com.example.data.HistoryTapeItem
import com.example.ui.screens.CalculatorMode

class CalculatorViewModel : ViewModel() {

    var currentMode by mutableStateOf(CalculatorMode.STANDARD)
        private set

    var expression by mutableStateOf("")
        private set

    var resultText by mutableStateOf("0")
        private set

    var isDegrees by mutableStateOf(true)
        private set

    private val _historyItems = mutableStateListOf<HistoryTapeItem>()
    val historyItems: List<HistoryTapeItem> get() = _historyItems

    var activeProofSolution by mutableStateOf<CasSolution?>(null)
        private set

    var showHistorySheet by mutableStateOf(false)
        private set

    var showArchitectureModal by mutableStateOf(false)
        private set

    fun setMode(mode: CalculatorMode) {
        currentMode = mode
    }

    fun toggleAngleMode() {
        isDegrees = !isDegrees
    }

    fun onDigit(digit: String) {
        expression += digit
    }

    fun onOperator(op: String) {
        expression += " $op "
    }

    fun onAppend(token: String) {
        expression += token
    }

    fun onClear() {
        expression = ""
        resultText = "0"
    }

    fun onDelete() {
        if (expression.isNotEmpty()) {
            expression = expression.dropLast(1).trimEnd()
        }
    }

    fun onParentheses() {
        val openCount = expression.count { it == '(' }
        val closeCount = expression.count { it == ')' }
        expression += if (openCount > closeCount) ")" else "("
    }

    fun onPercent() {
        expression += "%"
    }

    fun executeCalculation(): Result<Double> {
        if (expression.isBlank()) return Result.failure(IllegalArgumentException("Expression is empty"))
        val eval = ExpressionEvaluator.evaluate(expression, isDegrees)
        eval.onSuccess { value ->
            val formatted = ExpressionEvaluator.formatDisplay(value)
            resultText = formatted

            val solution = CasSolution(
                queryType = "Arithmetic Expression",
                equation = expression,
                finalResult = formatted,
                steps = listOf(
                    MathStep(1, "Expression Tokenization", expression, "Parsed mathematical operators according to BODMAS / PEMDAS hierarchy."),
                    MathStep(2, "Order of Operations Evaluation", "Evaluated operators with 64-bit IEEE precision.", "Completed reduction without loss of precision."),
                    MathStep(3, "Final Output Value", formatted, "Formatted with dynamic engineering display notation.")
                )
            )

            _historyItems.add(
                HistoryTapeItem(
                    expression = expression,
                    result = formatted,
                    mode = currentMode.label,
                    casSolution = solution
                )
            )
        }.onFailure {
            resultText = "Error"
        }
        return eval
    }

    fun handleCasSolution(solution: CasSolution) {
        resultText = solution.finalResult
        activeProofSolution = solution
        _historyItems.add(
            HistoryTapeItem(
                expression = solution.equation,
                result = solution.finalResult,
                mode = currentMode.label,
                casSolution = solution
            )
        )
    }

    fun reuseResult(pastVal: String) {
        expression += pastVal
        showHistorySheet = false
    }

    fun setActiveProof(solution: CasSolution?) {
        activeProofSolution = solution
    }

    fun setShowHistory(show: Boolean) {
        showHistorySheet = show
    }

    fun setShowArchitecture(show: Boolean) {
        showArchitectureModal = show
    }

    fun clearHistory() {
        _historyItems.clear()
    }
}
