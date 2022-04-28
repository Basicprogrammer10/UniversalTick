package com.connorcode.universaltick;

import net.fabricmc.api.ModInitializer;

public class UniversalTick implements ModInitializer {
    @Override
    public void onInitialize() {
        // Init server side command system
       new Commands().initCommands();
    }
}
