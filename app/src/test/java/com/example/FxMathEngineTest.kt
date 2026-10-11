package com.example

import com.example.core.finance.FxEngine
import com.example.core.math.CasEngine
import com.example.core.math.ExpressionEvaluator
import com.example.core.math.Matrix2x2
import com.example.core.math.Vector3D
import org.junit.Assert.*
import org.junit.Test

class FxMathEngineTest {

    @Test
    fun testOrderOfOperations() {
        val result = ExpressionEvaluator.evaluate("2 + 3 * 4").getOrThrow()
        assertEquals(14.0, result, 1e-9)

        val parenResult = ExpressionEvaluator.evaluate("(2 + 3) * 4").getOrThrow()
        assertEquals(20.0, parenResult, 1e-9)

        val exponentResult = ExpressionEvaluator.evaluate("2 ^ 3 + 1").getOrThrow()
        assertEquals(9.0, exponentResult, 1e-9)
    }

    @Test
    fun testScientificFunctions() {
        val sqrtVal = ExpressionEvaluator.evaluate("sqrt(16)").getOrThrow()
        assertEquals(4.0, sqrtVal, 1e-9)

        val sinVal = ExpressionEvaluator.evaluate("sin(90)", isDegrees = true).getOrThrow()
        assertEquals(1.0, sinVal, 1e-9)

        val factVal = ExpressionEvaluator.evaluate("5!").getOrThrow()
        assertEquals(120.0, factVal, 1e-9)
    }

    @Test
    fun testQuadraticSolver() {
        // x^2 - 5x + 6 = 0  => roots 3 and 2
        val sol = CasEngine.solveQuadratic(1.0, -5.0, 6.0)
        assertTrue(sol.isSuccess)
        assertTrue(sol.steps.size >= 3)
        assertTrue(sol.finalResult.contains("2") && sol.finalResult.contains("3"))
    }

    @Test
    fun testSystem2x2() {
        // 2x + 3y = 8 and 4x - y = 2 => x = 1, y = 2
        val sol = CasEngine.solveSystem2x2(2.0, 3.0, 8.0, 4.0, -1.0, 2.0)
        assertTrue(sol.isSuccess)
        assertTrue(sol.finalResult.contains("x = 1"))
        assertTrue(sol.finalResult.contains("y = 2"))
    }

    @Test
    fun testNumericalDerivative() {
        // f(x) = x^2, f'(2) should be 4
        val sol = CasEngine.numericalDerivative("x^2", 2.0)
        assertTrue(sol.isSuccess)
        assertTrue(sol.finalResult.contains("4"))
    }

    @Test
    fun testDefiniteIntegral() {
        // ∫[0, 2] x dx = 2.0
        val sol = CasEngine.definiteIntegral("x", 0.0, 2.0)
        assertTrue(sol.isSuccess)
        assertEquals(2.0, sol.finalResult.toDouble(), 0.05)
    }

    @Test
    fun testMatrix2x2Analysis() {
        val m = Matrix2x2(1.0, 2.0, 3.0, 4.0)
        // det = 1*4 - 2*3 = -2
        assertEquals(-2.0, m.determinant, 1e-9)
        val sol = CasEngine.matrix2x2Analysis(m)
        assertTrue(sol.finalResult.contains("-2"))
    }

    @Test
    fun testVectorOperations() {
        val u = Vector3D(1.0, 0.0, 0.0)
        val v = Vector3D(0.0, 1.0, 0.0)
        // dot product = 0 (perpendicular)
        assertEquals(0.0, u.dot(v), 1e-9)
        // cross product = (0, 0, 1)
        val cross = u.cross(v)
        assertEquals(0.0, cross.x, 1e-9)
        assertEquals(0.0, cross.y, 1e-9)
        assertEquals(1.0, cross.z, 1e-9)
    }

    @Test
    fun testCompoundInterest() {
        val res = FxEngine.calculateCompoundInterest(1000.0, 10.0, 2, monthlyDeposit = 0.0)
        assertTrue(res.futureValue > 1200.0)
        assertEquals(2, res.yearlyBreakdown.size)
    }
}
