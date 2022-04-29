package com.connorcode.universaltick;

import net.fabricmc.api.ModInitializer;

public class UniversalTick implements ModInitializer {
    public static long targetMSPT = 50;

    public static float getTps() {
        return (1F / ((float) targetMSPT * 1000F));
    }

    public static void setTps(float tps) {
        targetMSPT = (long) ((1.0 / tps) * 1000);
    }

    @Override
    public void onInitialize() {
        // Init server side command system
        new Commands().initCommands();
    }
}
