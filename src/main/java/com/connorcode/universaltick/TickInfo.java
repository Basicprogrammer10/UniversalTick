package com.connorcode.universaltick;

import java.util.ArrayList;

public class TickInfo {
    private static long lastUpdate = System.currentTimeMillis();
    private static final ArrayList<Integer> tickHistory = new ArrayList<>();
    private static int timeTicks = 0;
    private static final int historyLen = 15;

    public static void processTick() {
        // Count this tick
        timeTicks++;

        // If its been a second shift the tick count into the list
        // Then reset the counter and last update
        if (lastUpdate - System.currentTimeMillis() > 1000) {
            while (tickHistory.size() > historyLen) tickHistory.remove(0);
            tickHistory.add(timeTicks);
            lastUpdate = System.currentTimeMillis();
            timeTicks = 0;
        }
    }

    public static double getTps() {
        // Average the tps
        // For loops are supposedly faster than stream stuff
        double sum = 0.0;
        for (Integer integer : tickHistory) sum += integer;
        return sum / historyLen;
    }
}
