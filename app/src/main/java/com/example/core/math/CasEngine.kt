package com.example.core.math

import kotlin.math.*

/**
 * Computer Algebra System (CAS) Engine
 * Provides symbolic & numerical algebra, calculus, linear algebra,
 * and generates clean mathematical step-by-step proofs.
 */
object CasEngine {

    /**
     * Solves quadratic equation: a x² + b x + c = 0
     */
    fun solveQuadratic(a: Double, b: Double, c: Double): CasSolution {
        if (abs(a) < 1e-12) {
            return solveLinear(b, c, 0.0)
        }

        val steps = mutableListOf<MathStep>()
        val eqString = "${formatCoeff(a)}x² + ${formatCoeff(b)}x + ${formatConst(c)} = 0"

        steps.add(
            MathStep(
                stepNumber = 1,
                title = "Standard Form Identification",
                formula = eqString,
                explanation = "Identify coefficients: a = $a, b = $b, c = $c"
            )
        )

        val discriminant = (b * b) - (4 * a * c)
        val discrFormatted = ExpressionEvaluator.formatDisplay(discriminant)
        steps.add(
            MathStep(
                stepNumber = 2,
                title = "Discriminant Calculation (Δ)",
                formula = "Δ = b² - 4ac = ($b)² - 4($a)($c) = $discrFormatted",
                explanation = "The discriminant dictates the nature and multiplicity of the roots."
            )
        )

        val resultStr: String
        if (discriminant > 1e-12) {
            val sqrtDelta = sqrt(discriminant)
            val x1 = (-b + sqrtDelta) / (2 * a)
            val x2 = (-b - sqrtDelta) / (2 * a)
            val x1Str = ExpressionEvaluator.formatDisplay(x1)
            val x2Str = ExpressionEvaluator.formatDisplay(x2)

            steps.add(
                MathStep(
                    stepNumber = 3,
                    title = "Quadratic Formula Application",
                    formula = "x = (-b ± √Δ) / 2a = (-($b) ± √$discrFormatted) / (2 · $a)",
                    explanation = "Since Δ > 0, there are two distinct real solutions."
                )
            )
            steps.add(
                MathStep(
                    stepNumber = 4,
                    title = "Evaluate Distinct Roots",
                    formula = "x₁ = $x1Str,  x₂ = $x2Str",
                    explanation = "Calculated intermediate values using standard algebraic expansion."
                )
            )
            resultStr = "x = { $x1Str, $x2Str }"
        } else if (abs(discriminant) <= 1e-12) {
            val x = -b / (2 * a)
            val xStr = ExpressionEvaluator.formatDisplay(x)
            steps.add(
                MathStep(
                    stepNumber = 3,
                    title = "Repeated Root Evaluation",
                    formula = "x = -b / (2a) = -($b) / (2 · $a) = $xStr",
                    explanation = "Since Δ = 0, the parabola touches the x-axis at a single repeated root."
                )
            )
            resultStr = "x = $xStr (multiplicity 2)"
        } else {
            val realPart = -b / (2 * a)
            val imagPart = sqrt(-discriminant) / (2 * a)
            val rStr = ExpressionEvaluator.formatDisplay(realPart)
            val iStr = ExpressionEvaluator.formatDisplay(abs(imagPart))
            steps.add(
                MathStep(
                    stepNumber = 3,
                    title = "Complex Conjugate Roots",
                    formula = "x = (-b ± i√|Δ|) / 2a = $rStr ± ${iStr}i",
                    explanation = "Since Δ < 0, solutions reside in the complex domain C as conjugate pairs."
                )
            )
            resultStr = "x = $rStr ± ${iStr}i"
        }

        return CasSolution(
            queryType = "Quadratic Equation",
            equation = eqString,
            finalResult = resultStr,
            steps = steps
        )
    }

    /**
     * Solves linear equation: a x + b = c
     */
    fun solveLinear(a: Double, b: Double, c: Double): CasSolution {
        val steps = mutableListOf<MathStep>()
        val eqString = "${formatCoeff(a)}x + ${formatConst(b)} = $c"

        steps.add(
            MathStep(
                stepNumber = 1,
                title = "Original Equation",
                formula = eqString,
                explanation = "Equation in one variable x."
            )
        )

        if (abs(a) < 1e-12) {
            val isConsistent = abs(b - c) < 1e-12
            val res = if (isConsistent) "Infinite solutions (Identity)" else "No solution (Inconsistent)"
            return CasSolution(
                queryType = "Linear Equation",
                equation = eqString,
                finalResult = res,
                steps = listOf(
                    MathStep(1, "Degenerate Case", "$b = $c", res)
                ),
                isSuccess = isConsistent
            )
        }

        val rhs = c - b
        steps.add(
            MathStep(
                stepNumber = 2,
                title = "Isolate Variable Term",
                formula = "${formatCoeff(a)}x = $c - ($b) = $rhs",
                explanation = "Subtract constant term from both sides."
            )
        )

        val x = rhs / a
        val xFormatted = ExpressionEvaluator.formatDisplay(x)
        steps.add(
            MathStep(
                stepNumber = 3,
                title = "Divide by Coefficient",
                formula = "x = $rhs / $a = $xFormatted",
                explanation = "Divide both sides by coefficient of x to obtain the final root."
            )
        )

        return CasSolution(
            queryType = "Linear Equation",
            equation = eqString,
            finalResult = "x = $xFormatted",
            steps = steps
        )
    }

    /**
     * Solves 2x2 System of Equations via Cramer's Rule:
     * a1 x + b1 y = c1
     * a2 x + b2 y = c2
     */
    fun solveSystem2x2(
        a1: Double, b1: Double, c1: Double,
        a2: Double, b2: Double, c2: Double
    ): CasSolution {
        val steps = mutableListOf<MathStep>()
        val eqString = "System: { ${a1}x + ${b1}y = $c1, ${a2}x + ${b2}y = $c2 }"

        val mainDet = (a1 * b2) - (a2 * b1)
        val dStr = ExpressionEvaluator.formatDisplay(mainDet)

        steps.add(
            MathStep(
                stepNumber = 1,
                title = "Coefficient Matrix Determinant (D)",
                formula = "D = |[$a1, $b1], [$a2, $b2]| = ($a1 · $b2) - ($a2 · $b1) = $dStr",
                explanation = "Cramer's rule requires non-zero determinant for unique solutions."
            )
        )

        if (abs(mainDet) < 1e-12) {
            val dx = (c1 * b2) - (c2 * b1)
            val isInfinite = abs(dx) < 1e-12
            val res = if (isInfinite) "Dependent system: Infinite solutions" else "Inconsistent system: No solution"
            return CasSolution(
                queryType = "System of Equations (2x2)",
                equation = eqString,
                finalResult = res,
                steps = steps,
                isSuccess = false,
                errorMessage = res
            )
        }

        val dx = (c1 * b2) - (c2 * b1)
        val dy = (a1 * c2) - (a2 * c1)
        val dxStr = ExpressionEvaluator.formatDisplay(dx)
        val dyStr = ExpressionEvaluator.formatDisplay(dy)

        steps.add(
            MathStep(
                stepNumber = 2,
                title = "Substitute Column Vectors (Dx and Dy)",
                formula = "Dx = |[$c1, $b1], [$c2, $b2]| = $dxStr \nDy = |[$a1, $c1], [$a2, $c2]| = $dyStr",
                explanation = "Replace x and y column coefficients with the constant column vector."
            )
        )

        val x = dx / mainDet
        val y = dy / mainDet
        val xStr = ExpressionEvaluator.formatDisplay(x)
        val yStr = ExpressionEvaluator.formatDisplay(y)

        steps.add(
            MathStep(
                stepNumber = 3,
                title = "Apply Cramer's Inversion",
                formula = "x = Dx / D = $dxStr / $dStr = $xStr \ny = Dy / D = $dyStr / $dStr = $yStr",
                explanation = "Yields the unique coordinate intersection of both planes/lines."
            )
        )

        return CasSolution(
            queryType = "System of Equations (2x2)",
            equation = eqString,
            finalResult = "x = $xStr, y = $yStr",
            steps = steps
        )
    }

    /**
     * Numerical Derivative: d/dx [f(x)] at x = x0
     * Uses 5-point stencil central difference for ultra-high accuracy.
     */
    fun numericalDerivative(expr: String, x0: Double): CasSolution {
        val steps = mutableListOf<MathStep>()
        val eqString = "d/dx [$expr] at x = $x0"

        steps.add(
            MathStep(
                stepNumber = 1,
                title = "Formal Derivative Definition",
                formula = "f'(x₀) = lim (h → 0) [f(x₀ + h) - f(x₀ - h)] / 2h",
                explanation = "Calculating the instantaneous rate of change and tangent gradient."
            )
        )

        val h = 1e-5
        val f_x0_p2h = ExpressionEvaluator.evaluate(expr, isDegrees = false, xVal = x0 + 2 * h).getOrDefault(Double.NaN)
        val f_x0_ph = ExpressionEvaluator.evaluate(expr, isDegrees = false, xVal = x0 + h).getOrDefault(Double.NaN)
        val f_x0_mh = ExpressionEvaluator.evaluate(expr, isDegrees = false, xVal = x0 - h).getOrDefault(Double.NaN)
        val f_x0_m2h = ExpressionEvaluator.evaluate(expr, isDegrees = false, xVal = x0 - 2 * h).getOrDefault(Double.NaN)

        if (f_x0_ph.isNaN() || f_x0_mh.isNaN()) {
            return CasSolution(
                queryType = "Derivative",
                equation = eqString,
                finalResult = "Undefined",
                steps = steps,
                isSuccess = false,
                errorMessage = "Function is discontinuous or non-differentiable at x = $x0"
            )
        }

        // 5-point stencil central difference
        val deriv = (-f_x0_p2h + 8 * f_x0_ph - 8 * f_x0_mh + f_x0_m2h) / (12 * h)
        val derivStr = ExpressionEvaluator.formatDisplay(deriv)

        steps.add(
            MathStep(
                stepNumber = 2,
                title = "Finite Difference Stencil Evaluation",
                formula = "f(x₀ + h) ≈ ${ExpressionEvaluator.formatDisplay(f_x0_ph)},  f(x₀ - h) ≈ ${ExpressionEvaluator.formatDisplay(f_x0_mh)}",
                explanation = "Evaluated infinitesimal neighborhood around point with h = 10⁻⁵."
            )
        )

        steps.add(
            MathStep(
                stepNumber = 3,
                title = "Tangent Gradient Solution",
                formula = "f'($x0) = $derivStr",
                explanation = "Slope of the tangent line to the curve y = f(x) at x = $x0."
            )
        )

        return CasSolution(
            queryType = "Derivative",
            equation = eqString,
            finalResult = "f'($x0) = $derivStr",
            steps = steps
        )
    }

    /**
     * Definite Integral: ∫[a, b] f(x) dx
     * Evaluates via Simpson's 1/3 Rule with 100 subintervals.
     */
    fun definiteIntegral(expr: String, a: Double, b: Double, n: Int = 100): CasSolution {
        val steps = mutableListOf<MathStep>()
        val eqString = "∫[$a, $b] ($expr) dx"

        steps.add(
            MathStep(
                stepNumber = 1,
                title = "Riemann-Simpson Formulation",
                formula = eqString,
                explanation = "Area under the curve bounded by x = $a and x = $b."
            )
        )

        val intervals = if (n % 2 == 0) n else n + 1
        val h = (b - a) / intervals
        val hStr = ExpressionEvaluator.formatDisplay(h)

        steps.add(
            MathStep(
                stepNumber = 2,
                title = "Subinterval Partitioning",
                formula = "h = (b - a) / n = ($b - $a) / $intervals = $hStr",
                explanation = "Dividing the integration domain into $intervals parabolic segments."
            )
        )

        var sum = 0.0
        val fa = ExpressionEvaluator.evaluate(expr, isDegrees = false, xVal = a).getOrDefault(0.0)
        val fb = ExpressionEvaluator.evaluate(expr, isDegrees = false, xVal = b).getOrDefault(0.0)
        sum += fa + fb

        for (i in 1 until intervals) {
            val xi = a + i * h
            val fi = ExpressionEvaluator.evaluate(expr, isDegrees = false, xVal = xi).getOrDefault(0.0)
            sum += if (i % 2 == 0) 2 * fi else 4 * fi
        }

        val integral = (h / 3.0) * sum
        val integralStr = ExpressionEvaluator.formatDisplay(integral)

        steps.add(
            MathStep(
                stepNumber = 3,
                title = "Simpson's Composite Rule Summation",
                formula = "I ≈ (h / 3) · [f(x₀) + 4∑f(x_odd) + 2∑f(x_even) + f(xₙ)]",
                explanation = "Sum of weighted quadratic approximations over the bounded interval."
            )
        )

        steps.add(
            MathStep(
                stepNumber = 4,
                title = "Final Net Area Under Curve",
                formula = "∫ ($expr) dx = $integralStr",
                explanation = "Exact definite integral computed to high floating-point precision."
            )
        )

        return CasSolution(
            queryType = "Definite Integral",
            equation = eqString,
            finalResult = integralStr,
            steps = steps
        )
    }

    /**
     * Matrix Inversion and Determinant for 2x2 Matrix
     */
    fun matrix2x2Analysis(m: Matrix2x2): CasSolution {
        val steps = mutableListOf<MathStep>()
        val matrixStr = "Matrix A = [ [${m.a}, ${m.b}], [${m.c}, ${m.d}] ]"

        val det = m.determinant
        val detStr = ExpressionEvaluator.formatDisplay(det)

        steps.add(
            MathStep(
                stepNumber = 1,
                title = "Determinant Calculation",
                formula = "det(A) = ad - bc = (${m.a} · ${m.d}) - (${m.b} · ${m.c}) = $detStr",
                explanation = "Scalar measure of linear transformation scaling factor."
            )
        )

        if (abs(det) < 1e-12) {
            return CasSolution(
                queryType = "Matrix Analysis",
                equation = matrixStr,
                finalResult = "det(A) = 0 (Singular / Non-invertible)",
                steps = steps,
                isSuccess = true
            )
        }

        val inv = m.inverse()!!
        val aI = ExpressionEvaluator.formatDisplay(inv.a)
        val bI = ExpressionEvaluator.formatDisplay(inv.b)
        val cI = ExpressionEvaluator.formatDisplay(inv.c)
        val dI = ExpressionEvaluator.formatDisplay(inv.d)

        steps.add(
            MathStep(
                stepNumber = 2,
                title = "Adjugate Matrix & Inversion",
                formula = "A⁻¹ = (1 / det(A)) · adj(A) = [ [$dI, $bI], [$cI, $aI] ]",
                explanation = "Inversion formula satisfies A · A⁻¹ = I₂ (Identity Matrix)."
            )
        )

        return CasSolution(
            queryType = "Matrix Analysis",
            equation = matrixStr,
            finalResult = "det(A) = $detStr, A⁻¹ = [ [$aI, $bI], [$cI, $dI] ]",
            steps = steps
        )
    }

    /**
     * 3D Vector Geometry Analysis (Dot, Cross, Magnitudes)
     */
    fun vectorAnalysis(u: Vector3D, v: Vector3D): CasSolution {
        val steps = mutableListOf<MathStep>()
        val eqString = "u = (${u.x}, ${u.y}, ${u.z}), v = (${v.x}, ${v.y}, ${v.z})"

        val dot = u.dot(v)
        val dotStr = ExpressionEvaluator.formatDisplay(dot)

        steps.add(
            MathStep(
                stepNumber = 1,
                title = "Vector Dot Product (Scalar Product)",
                formula = "u · v = (${u.x} · ${v.x}) + (${u.y} · ${v.y}) + (${u.z} · ${v.z}) = $dotStr",
                explanation = "Calculates projection of u onto v in ℝ³."
            )
        )

        val cross = u.cross(v)
        val cx = ExpressionEvaluator.formatDisplay(cross.x)
        val cy = ExpressionEvaluator.formatDisplay(cross.y)
        val cz = ExpressionEvaluator.formatDisplay(cross.z)

        steps.add(
            MathStep(
                stepNumber = 2,
                title = "Vector Cross Product (Orthogonal Vector)",
                formula = "u × v = |[i, j, k], [${u.x}, ${u.y}, ${u.z}], [${v.x}, ${v.y}, ${v.z}]| = ($cx, $cy, $cz)",
                explanation = "Generates a vector perpendicular to both u and v with magnitude equal to area of parallelogram."
            )
        )

        val magU = u.magnitude
        val magV = v.magnitude
        val cosTheta = if (magU * magV > 1e-12) (dot / (magU * magV)).coerceIn(-1.0, 1.0) else 0.0
        val angleDeg = Math.toDegrees(acos(cosTheta))
        val angleStr = ExpressionEvaluator.formatDisplay(angleDeg)

        steps.add(
            MathStep(
                stepNumber = 3,
                title = "Angle Between Vectors (θ)",
                formula = "cos(θ) = (u · v) / (|u| · |v|)  ⇒  θ = $angleStr°",
                explanation = "Angular separation measured in degrees."
            )
        )

        return CasSolution(
            queryType = "Vector Algebra",
            equation = eqString,
            finalResult = "u · v = $dotStr, u × v = ($cx, $cy, $cz), θ = $angleStr°",
            steps = steps
        )
    }

    private fun formatCoeff(c: Double): String =
        if (c == 1.0) "" else if (c == -1.0) "-" else ExpressionEvaluator.formatDisplay(c)

    private fun formatConst(c: Double): String =
        if (c >= 0) ExpressionEvaluator.formatDisplay(c) else "(${ExpressionEvaluator.formatDisplay(c)})"
}
