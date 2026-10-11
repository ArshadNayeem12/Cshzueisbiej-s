package com.example.core.haptics

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager

/**
 * Custom Haptic Feedback Engine for FX Calculator
 * Emulates the tactile mechanical sensation of luxury timepiece pushers
 * and precision Casio fx keystrokes.
 */
class HapticEngine(private val context: Context) {

    private val vibrator: Vibrator? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val manager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
        manager?.defaultVibrator
    } else {
        @Suppress("DEPRECATION")
        context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
    }

    /** Subtle 120Hz micro-pulse for number keypresses */
    fun tick() {
        vibrate(durationMs = 8, amplitude = 70)
    }

    /** Crisp mechanical click for operator presses (+, -, *, /) */
    fun click() {
        vibrate(durationMs = 15, amplitude = 140)
    }

    /** Heavy, satisfying execution thump for Equals (=) and Solve */
    fun execute() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            try {
                vibrator?.vibrate(VibrationEffect.createPredefined(VibrationEffect.EFFECT_HEAVY_CLICK))
                return
            } catch (_: Exception) {}
        }
        vibrate(durationMs = 32, amplitude = 230)
    }

    /** Double-tap vibration for mode morphing (CAS, FX, Graph, Canvas) */
    fun modeSwitch() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            try {
                val timings = longArrayOf(0, 10, 35, 12)
                val amplitudes = intArrayOf(0, 100, 0, 160)
                vibrator?.vibrate(VibrationEffect.createWaveform(timings, amplitudes, -1))
                return
            } catch (_: Exception) {}
        }
        vibrate(durationMs = 20, amplitude = 120)
    }

    /** Warning buzz on syntax errors or math errors */
    fun error() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            try {
                val timings = longArrayOf(0, 25, 40, 35)
                val amplitudes = intArrayOf(0, 180, 0, 220)
                vibrator?.vibrate(VibrationEffect.createWaveform(timings, amplitudes, -1))
                return
            } catch (_: Exception) {}
        }
        vibrate(durationMs = 50, amplitude = 200)
    }

    private fun vibrate(durationMs: Long, amplitude: Int) {
        try {
            if (vibrator?.hasVibrator() == true) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    vibrator.vibrate(
                        VibrationEffect.createOneShot(
                            durationMs,
                            amplitude.coerceIn(1, 255)
                        )
                    )
                } else {
                    @Suppress("DEPRECATION")
                    vibrator.vibrate(durationMs)
                }
            }
        } catch (_: Exception) {
            // Graceful fallback if vibration permission or device lacks haptic motor
        }
    }
}
