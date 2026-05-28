package com.expanse.autopilot.domain.engine;

@kotlin.Metadata(mv = {2, 3, 0}, k = 1, xi = 48, d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\u0006\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\u0007\u00a2\u0006\u0004\b\u0002\u0010\u0003J\u000e\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u0005\u00a8\u0006\u0007"}, d2 = {"Lcom/expanse/autopilot/domain/engine/SweepEngine;", "", "<init>", "()V", "calculateSweepAmount", "", "purchaseAmount", "app"})
public final class SweepEngine {
    
    public SweepEngine() {
        super();
    }
    
    /**
     * Calculates the micro-savings sweep amount to the nearest multiple of 50.
     * If the purchase is already a perfect multiple of 50, sweep is 0.
     */
    public final double calculateSweepAmount(double purchaseAmount) {
        return 0.0;
    }
}