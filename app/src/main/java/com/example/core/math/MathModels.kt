package com.example.core.math

/**
 * Representation of an individual mathematical derivation or proof step
 */
data class MathStep(
    val stepNumber: Int,
    val title: String,
    val formula: String,
    val explanation: String
)

/**
 * Complete CAS solution result with step-by-step mathematical reasoning
 */
data class CasSolution(
    val queryType: String,
    val equation: String,
    val finalResult: String,
    val steps: List<MathStep>,
    val isSuccess: Boolean = true,
    val errorMessage: String? = null
)

/**
 * 2x2 Matrix for linear algebra and Cramer's rule
 */
data class Matrix2x2(
    val a: Double, val b: Double,
    val c: Double, val d: Double
) {
    val determinant: Double get() = (a * d) - (b * c)

    fun inverse(): Matrix2x2? {
        val det = determinant
        if (kotlin.math.abs(det) < 1e-12) return null
        return Matrix2x2(
            a = d / det,
            b = -b / det,
            c = -c / det,
            d = a / det
        )
    }
}

/**
 * 3x3 Matrix
 */
data class Matrix3x3(
    val m: Array<DoubleArray>
) {
    val determinant: Double get() {
        val a = m[0][0]; val b = m[0][1]; val c = m[0][2]
        val d = m[1][0]; val e = m[1][1]; val f = m[1][2]
        val g = m[2][0]; val h = m[2][1]; val i = m[2][2]
        return a * (e * i - f * h) - b * (d * i - f * g) + c * (d * h - e * g)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Matrix3x3) return false
        return m.contentDeepEquals(other.m)
    }

    override fun hashCode(): Int {
        return m.contentDeepHashCode()
    }
}

/**
 * 3D Vector for dot and cross products
 */
data class Vector3D(
    val x: Double,
    val y: Double,
    val z: Double
) {
    val magnitude: Double get() = kotlin.math.sqrt(x * x + y * y + z * z)

    fun dot(other: Vector3D): Double = (x * other.x) + (y * other.y) + (z * other.z)

    fun cross(other: Vector3D): Vector3D = Vector3D(
        x = (y * other.z) - (z * other.y),
        y = (z * other.x) - (x * other.z),
        z = (x * other.y) - (y * other.x)
    )
}
