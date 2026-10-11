package com.example.core.math

import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale
import java.util.Stack
import kotlin.math.*

/**
 * High-Precision Mathematical Expression Evaluator
 * Supports Shunting-Yard parsing, BODMAS/PEMDAS order of operations,
 * Trigonometric, Hyperbolic, Logarithmic, and Exponential functions.
 */
object ExpressionEvaluator {

    private const val PI_VAL = Math.PI
    private const val E_VAL = Math.E
    private const val PHI_VAL = 1.6180339887498948482 // Golden Ratio

    /**
     * Evaluates a mathematical expression string.
     * @param expr The expression string (e.g. "sin(45) + 3^2 - sqrt(16)")
     * @param isDegrees Whether trigonometric functions operate in degrees (true) or radians (false)
     * @param xVal Optional value for variable 'x' (useful for functions and graphing)
     */
    fun evaluate(
        expr: String,
        isDegrees: Boolean = true,
        xVal: Double? = null
    ): Result<Double> {
        val sanitized = sanitize(expr, xVal)
        if (sanitized.isBlank()) return Result.failure(IllegalArgumentException("Empty expression"))

        return try {
            val tokens = tokenize(sanitized)
            val rpn = toRpn(tokens)
            val value = evaluateRpn(rpn, isDegrees)
            if (value.isNaN()) {
                Result.failure(ArithmeticException("Undefined or NaN result"))
            } else if (value.isInfinite()) {
                Result.failure(ArithmeticException("Division by zero or infinite value"))
            } else {
                Result.success(value)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Formats a double value for luxury display (handles integer rounding,
     * thousands grouping, and scientific notation when needed).
     */
    fun formatDisplay(value: Double): String {
        if (value.isNaN()) return "Error: NaN"
        if (value.isInfinite()) return if (value > 0) "∞" else "-∞"

        val absVal = abs(value)
        if (absVal != 0.0 && (absVal >= 1e12 || absVal < 1e-7)) {
            val scFormatter = DecimalFormat("0.######E0", DecimalFormatSymbols(Locale.US))
            return scFormatter.format(value).replace("E", " × 10^")
        }

        // Check if nearly integer
        val rounded = value.roundToLong()
        if (abs(value - rounded) < 1e-10 && abs(value) < 1e14) {
            val intFormat = DecimalFormat("#,###", DecimalFormatSymbols(Locale.US))
            return intFormat.format(rounded)
        }

        val symbols = DecimalFormatSymbols(Locale.US)
        val df = DecimalFormat("#,##0.########", symbols)
        return df.format(value)
    }

    private fun sanitize(raw: String, xVal: Double?): String {
        var s = raw
            .replace("×", "*")
            .replace("÷", "/")
            .replace("−", "-")
            .replace("π", PI_VAL.toString())
            .replace("φ", PHI_VAL.toString())

        // Replace constant 'e' if not part of a function or number
        s = s.replace(Regex("(?<=[^a-zA-Z0-9]|^)e(?=[^a-zA-Z0-9]|$)"), E_VAL.toString())

        if (xVal != null) {
            val xStr = if (xVal < 0) "($xVal)" else xVal.toString()
            s = s.replace(Regex("(?<=[^a-zA-Z0-9]|^)x(?=[^a-zA-Z0-9]|$)"), xStr)
        }

        // Implicit multiplication: e.g., 2(3), (2)(3), 2sin(x)
        s = s.replace(Regex("(\\d+)\\s*\\("), "$1*(")
        s = s.replace(Regex("\\)\\s*\\("), ")*(")
        s = s.replace(Regex("\\)\\s*(\\d+)"), ")*$1")

        return s
    }

    private fun tokenize(expr: String): List<String> {
        val tokens = mutableListOf<String>()
        var i = 0
        val len = expr.length

        while (i < len) {
            val c = expr[i]

            if (c.isWhitespace()) {
                i++
                continue
            }

            if (c.isDigit() || c == '.') {
                val sb = StringBuilder()
                while (i < len && (expr[i].isDigit() || expr[i] == '.')) {
                    sb.append(expr[i])
                    i++
                }
                tokens.add(sb.toString())
                continue
            }

            if (c.isLetter()) {
                val sb = StringBuilder()
                while (i < len && expr[i].isLetter()) {
                    sb.append(expr[i])
                    i++
                }
                tokens.add(sb.toString().lowercase())
                continue
            }

            // Unary minus handling
            if (c == '-') {
                val isUnary = tokens.isEmpty() ||
                        tokens.last() == "(" ||
                        isOperator(tokens.last())
                if (isUnary) {
                    tokens.add("u-")
                    i++
                    continue
                }
            }

            if (c in "+-*/%^!()") {
                tokens.add(c.toString())
                i++
                continue
            }

            i++
        }

        return tokens
    }

    private fun isOperator(token: String): Boolean =
        token in listOf("+", "-", "*", "/", "%", "^", "u-")

    private fun isFunction(token: String): Boolean =
        token in listOf(
            "sin", "cos", "tan", "asin", "acos", "atan",
            "sinh", "cosh", "tanh", "ln", "log", "sqrt", "cbrt", "abs"
        )

    private fun precedence(op: String): Int = when (op) {
        "+", "-" -> 1
        "*", "/", "%" -> 2
        "u-" -> 3
        "^" -> 4
        else -> 0
    }

    private fun toRpn(tokens: List<String>): List<String> {
        val output = mutableListOf<String>()
        val stack = Stack<String>()

        for (token in tokens) {
            if (token.toDoubleOrNull() != null) {
                output.add(token)
            } else if (isFunction(token)) {
                stack.push(token)
            } else if (token == "(") {
                stack.push(token)
            } else if (token == ")") {
                while (stack.isNotEmpty() && stack.peek() != "(") {
                    output.add(stack.pop())
                }
                if (stack.isNotEmpty() && stack.peek() == "(") {
                    stack.pop() // Discard '('
                }
                if (stack.isNotEmpty() && isFunction(stack.peek())) {
                    output.add(stack.pop())
                }
            } else if (token == "!") {
                output.add("!")
            } else if (isOperator(token)) {
                val p1 = precedence(token)
                while (stack.isNotEmpty() && isOperator(stack.peek())) {
                    val p2 = precedence(stack.peek())
                    if ((token != "^" && p1 <= p2) || (token == "^" && p1 < p2)) {
                        output.add(stack.pop())
                    } else break
                }
                stack.push(token)
            }
        }

        while (stack.isNotEmpty()) {
            val top = stack.pop()
            if (top != "(" && top != ")") {
                output.add(top)
            }
        }

        return output
    }

    private fun evaluateRpn(rpn: List<String>, isDegrees: Boolean): Double {
        val stack = Stack<Double>()

        for (token in rpn) {
            val num = token.toDoubleOrNull()
            if (num != null) {
                stack.push(num)
                continue
            }

            if (token == "u-") {
                val a = stack.pop()
                stack.push(-a)
                continue
            }

            if (token == "!") {
                val a = stack.pop()
                stack.push(factorial(a))
                continue
            }

            if (isFunction(token)) {
                val a = stack.pop()
                val res = when (token) {
                    "sin" -> sin(if (isDegrees) Math.toRadians(a) else a)
                    "cos" -> cos(if (isDegrees) Math.toRadians(a) else a)
                    "tan" -> tan(if (isDegrees) Math.toRadians(a) else a)
                    "asin" -> {
                        val v = asin(a)
                        if (isDegrees) Math.toDegrees(v) else v
                    }
                    "acos" -> {
                        val v = acos(a)
                        if (isDegrees) Math.toDegrees(v) else v
                    }
                    "atan" -> {
                        val v = atan(a)
                        if (isDegrees) Math.toDegrees(v) else v
                    }
                    "sinh" -> sinh(a)
                    "cosh" -> cosh(a)
                    "tanh" -> tanh(a)
                    "ln" -> ln(a)
                    "log" -> log10(a)
                    "sqrt" -> sqrt(a)
                    "cbrt" -> cbrt(a)
                    "abs" -> abs(a)
                    else -> throw IllegalArgumentException("Unknown function: $token")
                }
                stack.push(res)
                continue
            }

            if (isOperator(token)) {
                val b = stack.pop()
                val a = stack.pop()
                val res = when (token) {
                    "+" -> a + b
                    "-" -> a - b
                    "*" -> a * b
                    "/" -> if (b == 0.0) throw ArithmeticException("Division by zero") else a / b
                    "%" -> a % b
                    "^" -> a.pow(b)
                    else -> throw IllegalArgumentException("Unknown operator: $token")
                }
                stack.push(res)
            }
        }

        return if (stack.isNotEmpty()) stack.pop() else 0.0
    }

    private fun factorial(n: Double): Double {
        if (n < 0 || n != floor(n)) throw IllegalArgumentException("Factorial is only defined for non-negative integers")
        val intN = n.toLong()
        if (intN > 170) return Double.POSITIVE_INFINITY // Exceeds 64-bit IEEE double
        var res = 1.0
        for (i in 2..intN) {
            res *= i
        }
        return res
    }
}
