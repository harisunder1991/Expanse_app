package com.expanse.autopilot.domain.engine

import kotlin.math.ceil

class SweepEngine {

    /**
     * Calculates the micro-savings sweep amount to the nearest multiple of 50.
     * If the purchase is already a perfect multiple of 50, sweep is 0.
     */
    fun calculateSweepAmount(purchaseAmount: Double): Double {
        if (purchaseAmount <= 0.0) return 0.0
        
        val factor = 50.0
        val target = ceil(purchaseAmount / factor) * factor
        val sweep = target - purchaseAmount
        
        // Float precision error mitigation
        return if (sweep < 0.01) 0.0 else Math.round(sweep * 100.0) / 100.0
    }
}
