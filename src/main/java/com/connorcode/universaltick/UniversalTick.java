package com.connorcode.universaltick;

import net.fabricmc.api.ModInitializer;

public class UniversalTick implements ModInitializer {
    public static long targetMSPT = 50;

    @Override
    public void onInitialize() {
        // Init server side command system
       new Commands().initCommands();
    }


    public static void setTps(float tps) {
        targetMSPT = (long) ((1.0 / tps) * 1000);
    }
}
