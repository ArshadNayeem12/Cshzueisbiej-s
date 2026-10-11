package com.example.data

import com.example.core.math.CasSolution

/**
 * Calculation item saved to the infinite history tape
 */
data class HistoryTapeItem(
    val id: String = java.util.UUID.randomUUID().toString(),
    val timestamp: Long = System.currentTimeMillis(),
    val expression: String,
    val result: String,
    val mode: String = "Standard",
    val casSolution: CasSolution? = null
)
