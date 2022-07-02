package com.connorcode.universaltick;

import java.util.ArrayList;

public class TickInfo {
    public final ArrayList<Integer> tickHistory = new ArrayList<>();
    private long lastTick = System.currentTimeMillis();

    public void processTick() {
        // Shift this tick length into the tick history list
        tickHistory.add((int) (System.currentTimeMillis() - lastTick));
        while (tickHistory.size() > (UniversalTick.getTps() * 10)) tickHistory.remove(0);

        // Then reset the last tick value
        lastTick = System.currentTimeMillis();
    }

    public double getTps() {
        // Average the tps
        return tickHistory.stream().mapToDouble(d -> 1D / (float) d * 1000D).average().orElse(0.0);
    }
}
